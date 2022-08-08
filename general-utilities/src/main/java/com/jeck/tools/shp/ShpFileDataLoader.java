package com.jeck.tools.shp;

import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceFeature.BusinessLogicConstant;
import com.glodon.pcop.cim.engine.dataServiceFeature.util.PropertyEntity;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.DatasetVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.InfoObjectTypeVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.PropertyTypeVO;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.model.*;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.transferVO.InfoObjectValue;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceModelRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceUserException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.shp.PinyinUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orientechnologies.orient.core.metadata.schema.OType;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.WKTWriter2;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShpFileDataLoader {
    private static final Logger log = LoggerFactory.getLogger(ShpFileDataLoader.class);
    public static final String THE_GEOM = "geom";

    public static void contentInsert(InfoObjectDef infoObjectDef, String shpFile) throws IOException {//NOSONAR

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        Gson gson = gsonBuilder.create();

        File file = new File(shpFile);
        FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
        ((ShapefileDataStore) dataStore).setCharset(Charset.forName("UTF-8"));
        SimpleFeatureSource featureSource = dataStore.getFeatureSource();
        SimpleFeatureType featureType = featureSource.getSchema();
        Query query = new Query(featureType.getTypeName());
        query.setMaxFeatures(Integer.MAX_VALUE);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures(query);

        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            int rowNum = 0;
            while (features.hasNext()) {
                Map<String, Object> dataValues = new HashMap<>();
                SimpleFeature feature = features.next();
                //add geometry property
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                dataValues.put(BusinessLogicConstant.GeographicInformation_PROPERTY_TYPE_ID,
                        (new WKTWriter2()).writeFormatted(geometry).replaceAll("\n", ""));
                //非空间类型
                //add other properties
                for (Property property : feature.getProperties()) {
                    String propertyName = property.getName().toString();
                    propertyName = PinyinUtils.getPinYinWithoutSpecialChar(propertyName);
                    if (propertyName.equalsIgnoreCase("id")) {
                        propertyName = "ID";
                    }
                    if (THE_GEOM.toLowerCase().equals(propertyName.toLowerCase())) {
                        continue;
                    }
                    Object propertyValue = property.getValue();
                    dataValues.put(propertyName, propertyValue);
                    log.debug("property name=[{}], property value=[{}]", propertyName, propertyValue);
                }
                InfoObjectValue infoObjectValue = new InfoObjectValue();
                infoObjectValue.setBaseDatasetPropertiesValue(dataValues);
                log.debug("infoObjectValue=[{}]", gson.toJson(infoObjectValue));
                infoObjectDef.newObject(infoObjectValue, false);
            }
        } catch (DataServiceUserException e) {
            log.error("loader shp file data into cim data base failed");
        } finally {
            if (dataStore != null) {
                dataStore.dispose();
            }
        }
    }


    public static InfoObjectDef createObjectTypeAndDataSet(String tenantId, String objectTypeId,
                                                           Map<String, String> propertiesMap,
                                                           CIMModelCore modelCore)
            throws DataServiceModelRuntimeException {
        InfoObjectDefs objectDefs = modelCore.getInfoObjectDefs();
        InfoObjectDef objectDef;
        CimDataSpace cds = modelCore.getCimDataSpace();
        if (cds.hasInheritFactType(objectTypeId)) {
            objectDef = objectDefs.getInfoObjectDef(objectTypeId);
        } else {
            cds.flushUncommitedData();
            InfoObjectTypeVO objectTypeVO = new InfoObjectTypeVO();
            objectTypeVO.setObjectId(objectTypeId);
            objectTypeVO.setObjectName(objectTypeId);
            objectTypeVO.setTenantId(tenantId);
            objectDef = objectDefs.addRootInfoObjectDef(objectTypeVO);
            log.info("add object type: {}", objectTypeId);
        }

        if (objectDef != null) {
            String dataSetId = objectTypeId + "DataSet";
            DatasetDef datasetDef = objectDef.getDatasetDef(dataSetId);
            if (datasetDef == null) {
                log.info("add data set: {}", dataSetId);
                DatasetDefs datasetDefs = modelCore.getDatasetDefs();
                DatasetVO datasetVO = new DatasetVO();
                datasetVO.setDatasetName(dataSetId);
                datasetVO.setDatasetDesc(dataSetId);
                datasetVO.setDatasetStructure(DatasetDef.DatasetStructure.SINGLE);
                datasetVO.setDataSetType(BusinessLogicConstant.DatasetType.INSTANCE);
                datasetVO.setDatasetClassify("通用属性集");
                datasetVO.setInheritDataset(false);
                datasetVO.setHasDescendant(false);

                datasetDef = datasetDefs.addDatasetDef(datasetVO);
                objectDef.linkDatasetDef(datasetDef.getDatasetRID());

                PropertyTypeDefs propertyTypeDefs = modelCore.getPropertyTypeDefs();
                for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                    log.info("add property: {}", entry.getValue());
                    PropertyTypeVO propertyTypeVO = new PropertyTypeVO();
                    propertyTypeVO.setPropertyTypeName(entry.getValue().trim());
                    propertyTypeVO.setPropertyTypeDesc(entry.getValue().trim());
                    propertyTypeVO.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

                    PropertyTypeDef propertyTypeDef = propertyTypeDefs.addPropertyTypeDef(propertyTypeVO);
                    datasetDef.addPropertyTypeDef(propertyTypeDef.getPropertyTypeRID());
                }
            }
        }
        return objectDef;
    }

    public static Map<String, String> readShpFileStructure(String shpFile) throws IOException {
        File file = new File(shpFile);
        FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = dataStore.getFeatureSource();
        ((ShapefileDataStore) dataStore).setCharset(Charset.forName("UTF-8"));
        SimpleFeatureType featureType = featureSource.getSchema();

        List<AttributeDescriptor> attributeDescriptorList = featureType.getAttributeDescriptors();

        Map<String, String> structureMap = new HashMap<>();
        structureMap.put(BusinessLogicConstant.GeographicInformation_PROPERTY_TYPE_ID,
                BusinessLogicConstant.GeographicInformation_PROPERTY_TYPE_ID);
        for (AttributeDescriptor attributeDescriptor : attributeDescriptorList) {
            OType oType = OType.getTypeByClass(attributeDescriptor.getType().getBinding());
            String propertyName = attributeDescriptor.getName().toString();
            propertyName = PinyinUtils.getPinYinWithoutSpecialChar(propertyName);

            structureMap.put(propertyName, propertyName);
        }
        return structureMap;
    }


    public static Map<String, String> readShpGeoInfo(String shpFile, String mapKey) throws IOException {
        Map<String, String> osmIdGeoInfoMap = new HashMap<>();

        File file = new File(shpFile);
        FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
        ((ShapefileDataStore) dataStore).setCharset(Charset.forName("UTF-8"));
        SimpleFeatureSource featureSource = dataStore.getFeatureSource();
        SimpleFeatureType featureType = featureSource.getSchema();
        Query query = new Query(featureType.getTypeName());
        query.setMaxFeatures(Integer.MAX_VALUE);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures(query);
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            int rowNum = 0;
            while (features.hasNext()) {
                Map<String, Object> dataValues = new HashMap<>();
                SimpleFeature feature = features.next();
                String key = "";
                if (StringUtils.isBlank(mapKey)) {
                    key = rowNum + "";
                } else {
                    for (Property property : feature.getProperties()) {
                        String propertyName = property.getName().toString();
                        if (propertyName.equalsIgnoreCase(mapKey)) {
                            key = property.getValue().toString();
                        }
                    }
                }
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                String value = (new WKTWriter2()).writeFormatted(geometry).replaceAll("\n", "");
                osmIdGeoInfoMap.put(key, value);
                rowNum++;
            }
            log.info("read record size: [{}]", rowNum);
        } finally {
            if (dataStore != null) {
                dataStore.dispose();
            }
        }
        return osmIdGeoInfoMap;
    }

}
