package com.jeck.tools.gis;

import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineInfoExploreException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.factory.CimDataEngineComponentFactory;
import com.glodon.pcop.cim.engine.dataServiceFeature.BusinessLogicConstant;
import com.glodon.pcop.cim.engine.dataServiceFeature.feature.DatasetFeatures;
import com.glodon.pcop.cim.engine.dataServiceFeature.feature.PropertyTypeFeatures;
import com.glodon.pcop.cim.engine.dataServiceFeature.feature.PropertyTypeRestrictFeatures;
import com.glodon.pcop.cim.engine.dataServiceFeature.util.PropertyEntity;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.DatasetVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.InfoObjectTypeVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.PropertyTypeRestrictVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.PropertyTypeVO;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.model.*;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceModelRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceUserException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.factory.ModelAPIComponentFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DatasetFeaturesUtil {

    private static Gson gson = new Gson();
    private static String tenantId = "1";
    private static String dataSpaceName = "pcopcim";

    public static void main(String[] args) {
        CimDataSpace cds = null;
        try {
            cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace("pcopcim");

            CIMModelCore modelCore = ModelAPIComponentFactory.getCIMModelCore("pcopcim", "1");
            modelCore.setCimDataSpace(cds);

            String objectTypeId = "china_all_admin_0708";

            InfoObjectDefs defs = modelCore.getInfoObjectDefs();
            if (!cds.hasInheritFactType(objectTypeId)) {
                InfoObjectTypeVO typeVO = new InfoObjectTypeVO();
                typeVO.setObjectId(objectTypeId);
                typeVO.setObjectName(objectTypeId);
                defs.addRootInfoObjectDef(typeVO);
            }

            DatasetDef datasetDef = addDataSetAndProperty();

            InfoObjectDef infoObjectDef = defs.getInfoObjectDef(objectTypeId);
            infoObjectDef.linkDatasetDef(datasetDef.getDatasetRID());
        } catch (DataServiceModelRuntimeException e) {
            e.printStackTrace();
        } catch (CimDataEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }

    }

    public void addPropertyTypeLink() throws CimDataEngineRuntimeException {
        // System.out.println(DatasetFeatures.addPropertyTypeLink(CimConstants.defauleSpaceName, "#92:0", "#111:224"));
        // System.out.println(DatasetFeatures.addPropertyTypeLink(CimConstants.defauleSpaceName, "#92:0", "#112:222"));
        // String baseDataSetId = "#137:0";
        String baseDataSetId = "#138:138";
        CimDataSpace cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(dataSpaceName);
        PropertyTypeVO propertyTypeVO12 = new PropertyTypeVO();
        propertyTypeVO12.setPropertyTypeName("integrateId");
        propertyTypeVO12.setPropertyFieldDataClassify("STRING");
        propertyTypeVO12.setPropertyTypeDesc("bimface integrateId");

        // PropertyTypeVO propertyTypeVO13 = new PropertyTypeVO();
        // propertyTypeVO13.setPropertyTypeName("bimfaceId");
        // propertyTypeVO13.setPropertyFieldDataClassify("STRING");
        // propertyTypeVO13.setPropertyTypeDesc("bimface fileId or integrationId: ");

        PropertyTypeVO tmpPropertyTypeVO = PropertyTypeFeatures.addPropertyType(cds, propertyTypeVO12);
        DatasetFeatures.addPropertyTypeLink(cds, baseDataSetId, tmpPropertyTypeVO.getPropertyTypeId());

        // tmpPropertyTypeVO = PropertyTypeFeatures.addPropertyType(cds, propertyTypeVO13);
        // DatasetFeatures.addPropertyTypeLink(cds, baseDataSetId, tmpPropertyTypeVO.getPropertyTypeId());
        // System.out.println("Add property type: id=" + tmpPropertyTypeVO.getPropertyTypeId() + ", alias=" +
        // tmpPropertyTypeVO.getPropertyTypeDesc());
    }

    public static DatasetDef addDataSetAndProperty() throws DataServiceModelRuntimeException, CimDataEngineRuntimeException {
        List<PropertyTypeVO> linkedProperties = new ArrayList<>();

        PropertyTypeVO pro1 = new PropertyTypeVO();
        pro1.setPropertyTypeName("level");
        pro1.setPropertyTypeDesc("level");
        pro1.setPropertyFieldDataClassify(PropertyEntity.DataTypes.INT.toString());

        PropertyTypeRestrictVO restrictVO1 = new PropertyTypeRestrictVO();
        restrictVO1.setNull(false);
        restrictVO1.setPrimaryKey(false);
        restrictVO1.setDefaultValue("-");
        // restrictVO.setEditable(false);
        pro1.setRestrictVO(restrictVO1);
        linkedProperties.add(pro1);

        PropertyTypeVO pro2 = new PropertyTypeVO();
        pro2.setPropertyTypeName("nameabbrevation");
        pro2.setPropertyTypeDesc("nameabbrevation");
        pro2.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

        PropertyTypeRestrictVO restrictBean2 = new PropertyTypeRestrictVO();
        restrictBean2.setNull(false);
        restrictBean2.setPrimaryKey(false);
        restrictBean2.setDefaultValue("-");
        // restrictBean2.setEditable(true);
        pro2.setRestrictVO(restrictBean2);

        linkedProperties.add(pro2);


        PropertyTypeVO pro3 = new PropertyTypeVO();
        pro3.setPropertyTypeName("name");
        pro3.setPropertyTypeDesc("name");
        pro3.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

        PropertyTypeRestrictVO restrictBean3 = new PropertyTypeRestrictVO();
        restrictBean3.setNull(false);
        restrictBean3.setPrimaryKey(false);
        restrictBean3.setDefaultValue("-");
        // restrictBean3.setEditable(true);
        pro3.setRestrictVO(restrictBean3);

        linkedProperties.add(pro3);

        PropertyTypeVO pro4 = new PropertyTypeVO();
        pro4.setPropertyTypeName("adminType");
        pro4.setPropertyTypeDesc("adminType");
        pro4.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

        PropertyTypeRestrictVO restrictBean4 = new PropertyTypeRestrictVO();
        restrictBean4.setNull(false);
        restrictBean4.setPrimaryKey(false);
        restrictBean4.setDefaultValue("-");
        // restrictBean4.setEditable(false);
        pro4.setRestrictVO(restrictBean4);

        linkedProperties.add(pro4);

        PropertyTypeVO pro5 = new PropertyTypeVO();
        pro5.setPropertyTypeName("cityCode");
        pro5.setPropertyTypeDesc("cityCode");
        pro5.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

        PropertyTypeRestrictVO restrictBean5 = new PropertyTypeRestrictVO();
        restrictBean5.setNull(false);
        restrictBean5.setPrimaryKey(false);
        restrictBean5.setDefaultValue("-");
        // restrictBean5.setEditable(false);
        pro5.setRestrictVO(restrictBean5);

        linkedProperties.add(pro5);

        PropertyTypeVO pro6 = new PropertyTypeVO();
        pro6.setPropertyTypeName("englishabbrevation");
        pro6.setPropertyTypeDesc("englishabbrevation");
        pro6.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

        PropertyTypeRestrictVO restrictBean6 = new PropertyTypeRestrictVO();
        restrictBean6.setNull(false);
        restrictBean6.setPrimaryKey(false);
        restrictBean6.setDefaultValue("-");
        // restrictBean6.setEditable(false);
        pro6.setRestrictVO(restrictBean6);

        linkedProperties.add(pro6);

        PropertyTypeVO pro7 = new PropertyTypeVO();
        pro7.setPropertyTypeName("english");
        pro7.setPropertyTypeDesc("english");
        pro7.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

        PropertyTypeRestrictVO restrictBean7 = new PropertyTypeRestrictVO();
        restrictBean7.setNull(false);
        restrictBean7.setPrimaryKey(false);
        restrictBean7.setDefaultValue("-");
        // restrictBean7.setEditable(false);
        pro7.setRestrictVO(restrictBean7);

        linkedProperties.add(pro7);

        PropertyTypeVO pro8 = new PropertyTypeVO();
        pro8.setPropertyTypeName("lnt");
        pro8.setPropertyTypeDesc("lnt");
        pro8.setPropertyFieldDataClassify(PropertyEntity.DataTypes.DOUBLE.toString());

        PropertyTypeRestrictVO restrictBean8 = new PropertyTypeRestrictVO();
        restrictBean8.setNull(false);
        restrictBean8.setPrimaryKey(false);
        restrictBean8.setDefaultValue("-");
        // restrictBean8.setEditable(true);
        pro8.setRestrictVO(restrictBean8);

        linkedProperties.add(pro8);

        PropertyTypeVO pro9 = new PropertyTypeVO();
        pro9.setPropertyTypeName("lat");
        pro9.setPropertyTypeDesc("lat");
        pro9.setPropertyFieldDataClassify(PropertyEntity.DataTypes.DOUBLE.toString());

        PropertyTypeRestrictVO restrictBean9 = new PropertyTypeRestrictVO();
        restrictBean9.setNull(false);
        restrictBean9.setPrimaryKey(false);
        restrictBean9.setDefaultValue("-");
        pro9.setRestrictVO(restrictBean9);

        linkedProperties.add(pro9);

        PropertyTypeVO pro10 = new PropertyTypeVO();
        pro10.setPropertyTypeName("parentCityCode");
        pro10.setPropertyTypeDesc("parentCityCode");
        pro10.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

        PropertyTypeRestrictVO restrictBean10 = new PropertyTypeRestrictVO();
        restrictBean10.setNull(false);
        restrictBean10.setPrimaryKey(false);
        restrictBean10.setDefaultValue("-");
        pro10.setRestrictVO(restrictBean10);

        linkedProperties.add(pro10);

        PropertyTypeVO pro11 = new PropertyTypeVO();
        pro11.setPropertyTypeName("bound");
        pro11.setPropertyTypeDesc("bound");
        pro11.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

        PropertyTypeRestrictVO restrictBean11 = new PropertyTypeRestrictVO();
        restrictBean11.setNull(false);
        restrictBean11.setPrimaryKey(false);
        restrictBean11.setDefaultValue("-");
        pro11.setRestrictVO(restrictBean11);

        linkedProperties.add(pro11);

        PropertyTypeVO pro12 = new PropertyTypeVO();
        pro12.setPropertyTypeName("points");
        pro12.setPropertyTypeDesc("points");
        pro12.setPropertyFieldDataClassify(PropertyEntity.DataTypes.STRING.toString());

        PropertyTypeRestrictVO restrictBean12 = new PropertyTypeRestrictVO();
        restrictBean12.setNull(false);
        restrictBean12.setPrimaryKey(false);
        restrictBean12.setDefaultValue("-");
        pro12.setRestrictVO(restrictBean12);

        linkedProperties.add(pro12);

        DatasetVO datasetVO = new DatasetVO();
        datasetVO.setDatasetName("china_all_admin_0708ds");
        datasetVO.setDatasetDesc("china_all_admin_0708ds");
        datasetVO.setDatasetStructure(DatasetDef.DatasetStructure.SINGLE);
        datasetVO.setDataSetType(BusinessLogicConstant.DatasetType.INSTANCE);
        datasetVO.setDatasetClassify("通用属性集");
        datasetVO.setInheritDataset(false);
        datasetVO.setHasDescendant(false);

        datasetVO.setLinkedPropertyTypes(linkedProperties);

        CIMModelCore cimModelCore = ModelAPIComponentFactory.getCIMModelCore(dataSpaceName, tenantId);
        //新增属性集定义
        DatasetDefs datasetDefs = cimModelCore.getDatasetDefs();
        DatasetDef datasetDef = datasetDefs.addDatasetDef(datasetVO);
        // DatasetDef datasetDef = datasetDefs.getDatasetDef("##137:0");

        if (datasetVO.getLinkedPropertyTypes() != null) {
            //新增属性集关联的属性
            PropertyTypeDefs propertyTypeDefs = cimModelCore.getPropertyTypeDefs();
            for (PropertyTypeVO propertyTypeVO : datasetVO.getLinkedPropertyTypes()) {
                PropertyTypeDef tmpPropertyTypeDef = propertyTypeDefs.addPropertyTypeDef(propertyTypeVO);
                if (tmpPropertyTypeDef != null) {
                    //属性与属性集关联
                    datasetDef.addPropertyTypeDef(tmpPropertyTypeDef);
                    //新增属性关联的限制条件
                    if (propertyTypeVO.getRestrictVO() != null) {
                        PropertyTypeRestrictVO restrictVO = propertyTypeVO.getRestrictVO();
                        restrictVO.setDatasetId(datasetDef.getDatasetRID());
                        restrictVO.setPropertyTypeId(tmpPropertyTypeDef.getPropertyTypeRID());
                        PropertyTypeRestrictFeatures.addRestrictToDataSet(dataSpaceName,
                                datasetDef.getDatasetRID(), restrictVO);
                    }
                }
            }
        }
        return datasetDef;
    }

}