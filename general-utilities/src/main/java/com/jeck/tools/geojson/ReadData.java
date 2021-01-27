package com.jeck.tools.geojson;

import com.alibaba.fastjson.JSON;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.config.PropertyHandler;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.factory.CimDataEngineComponentFactory;
import com.glodon.pcop.cim.engine.dataServiceFeature.BusinessLogicConstant;
import com.glodon.pcop.cim.engine.dataServiceFeature.util.PropertyEntity;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.DatasetVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.InfoObjectTypeVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.PropertyTypeVO;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.model.*;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.transferVO.InfoObjectValue;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceModelRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.factory.ModelAPIComponentFactory;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.shp.PinyinUtils;
import com.jeck.tools.util.DateUtil;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class ReadData {

    private static final String OBJ_SUFFIX = "_0731";

    public static void main(String[] args) throws IOException {
//        String filePath = "G:\\data\\geospatial_data\\china\\WANGTJ\\output\\建筑物.json";

        Path basePath = Paths.get("G:\\data\\geospatial_data\\china\\WANGTJ\\output-copy");
//        String location = "remote:localhost/";
        String location = "remote:10.0.197.168/";
        String account = "root";
        String pass_word = "wyc";

        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_SERVICE_LOCATION, location);
        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_ADMIN_ACCOUNT, account);
        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_ADMIN_PWD, pass_word);
        CimDataSpace cds = null;
        try {
            String spaceName = "pcopcim";
            String tenantId = "1";
            cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            CIMModelCore modelCore = ModelAPIComponentFactory.getCIMModelCore(spaceName, tenantId);
            modelCore.setCimDataSpace(cds);

            try (DirectoryStream<Path> entries = Files.newDirectoryStream(basePath, "*.json")) {
                for (Path entry : entries) {
                    String file = entry.toAbsolutePath().toString();
                    System.out.println("start to load file [" + file + "]");
                    InfoObjectDef infoObjectDef = createObjectType(modelCore, file);
                    loadGeoJsonData(infoObjectDef, file);
                    System.out.println("completed load file [" + file + "]");
                }
            } catch (DataServiceModelRuntimeException e) {
                e.printStackTrace();
            }
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }

    }

    public static void loadGeoJsonData(InfoObjectDef infoObjectDef, String filePath) throws IOException {
        FeatureJSON featureJSON = new FeatureJSON();
        FeatureCollection featureCollection = featureJSON.readFeatureCollection(new FileInputStream(filePath));
        System.out.println("feature collection schema: [" + featureCollection.getSchema() + "]");
        System.out.println("feature collection size: [" + featureCollection.size() + "]");
        FeatureIterator<Feature> featureIterator = featureCollection.features();
        List<InfoObjectValue> infoObjectValueList = new ArrayList<>();
        while (featureIterator.hasNext()) {
            Map<String, Object> values = new HashMap<>();
            Feature feature = featureIterator.next();
            GeometryAttribute geometryAttribute = feature.getDefaultGeometryProperty();
            values.put("CIM_GeographicInformation", ((Geometry) geometryAttribute.getValue()).toText());
            Collection<Property> propertyCollection = feature.getProperties();
            for (Property p : propertyCollection) {
                if (p.getName().toString().equalsIgnoreCase("geometry")) {
                    continue;
                }

                values.put(p.getName().toString(), p.getValue());

//                if (p.getName().toString().equalsIgnoreCase("ID")) {
//                    values.put("ID", p.getValue());
//                } else {
//                    values.put(p.getName().toString(), p.getValue());
//                }
            }

            InfoObjectValue objectValue = new InfoObjectValue();
            objectValue.setBaseDatasetPropertiesValue(values);
            infoObjectValueList.add(objectValue);
//            System.out.println("add one record: " + values.get("OBJECTID"));
        }
        infoObjectDef.newObjects(infoObjectValueList, false);
    }

    public static InfoObjectDef createObjectType(CIMModelCore modelCore, String filePath)
            throws IOException, DataServiceModelRuntimeException {
        Set<String> propertyNames = new HashSet<>();
        FeatureJSON featureJSON = new FeatureJSON();
        FeatureCollection featureCollection = featureJSON.readFeatureCollection(new FileInputStream(filePath));
        System.out.println("feature collection schema: [" + featureCollection.getSchema() + "]");
        FeatureType featureType = featureCollection.getSchema();
        for (PropertyDescriptor descriptor : featureType.getDescriptors()) {
            Name name = descriptor.getName();
//            System.out.println("property type: " + name.getLocalPart());
            propertyNames.add(name.getLocalPart());
        }
        System.out.println("property names: " + JSON.toJSONString(propertyNames));

        InfoObjectDefs defs = modelCore.getInfoObjectDefs();
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf('.'));
        String objectTypeId = PinyinUtils.getPinYinWithoutSpecialChar(fileName) + OBJ_SUFFIX;
        System.out.println("file name: [" + fileName + "], object type id: [" + objectTypeId + "]");
        InfoObjectDef infoObjectDef = defs.getInfoObjectDef(objectTypeId);
        if (infoObjectDef == null) {
            InfoObjectTypeVO typeVO = new InfoObjectTypeVO();
            typeVO.setObjectTypeName(objectTypeId);
            typeVO.setObjectTypeDesc(fileName);
            infoObjectDef = defs.addRootInfoObjectDef(typeVO);
        }

        Set<String> properties = new HashSet<>();
        for (String pro : propertyNames) {
            if (infoObjectDef.getPropertyTypeDefOfGeneralDatasets(pro) != null) {
                System.out.println("property of [" + pro + "] is already exist!");
                continue;
            } else {
                properties.add(pro);
            }
        }

        if (properties.isEmpty()) {
            System.out.println("no property should be add");
            return infoObjectDef;
        }

        DatasetVO datasetVO = new DatasetVO();
        datasetVO.setDatasetStructure(DatasetDef.DatasetStructure.SINGLE);
        datasetVO.setDataSetType(BusinessLogicConstant.DatasetType.INSTANCE);
        datasetVO.setDatasetClassify("通用属性集");
        datasetVO.setInheritDataset(false);
        datasetVO.setHasDescendant(false);
        String dataSetName = objectTypeId + "DS_" + DateUtil.getCurrentDateReadable();
        datasetVO.setDatasetName(dataSetName);
        datasetVO.setDatasetDesc(dataSetName);

        DatasetDefs datasetDefs = modelCore.getDatasetDefs();
        DatasetDef datasetDef = datasetDefs.addDatasetDef(datasetVO);
        infoObjectDef.linkDatasetDef(datasetDef.getDatasetRID());

        PropertyTypeDefs propertyTypeDefs = modelCore.getPropertyTypeDefs();
        for (String p : properties) {
            PropertyTypeVO propertyTypeVO = new PropertyTypeVO();
            propertyTypeVO.setPropertyTypeName(p);
            propertyTypeVO.setPropertyTypeDesc(p);
            propertyTypeVO.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());
            PropertyTypeDef propertyTypeDef = propertyTypeDefs.addPropertyTypeDef(propertyTypeVO);
            datasetDef.addPropertyTypeDef(propertyTypeDef);
        }
        return infoObjectDef;
    }

}
