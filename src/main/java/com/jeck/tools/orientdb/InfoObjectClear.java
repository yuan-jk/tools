package com.jeck.tools.orientdb;

import com.glodon.pcop.cim.common.util.CimConstants;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataMart.Dimension;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataMart.Fact;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataWarehouse.ExploreParameters;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataWarehouse.InformationExplorer;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataWarehouse.InformationFiltering.FilteringItem;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineInfoExploreException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.factory.CimDataEngineComponentFactory;
import com.glodon.pcop.cim.engine.dataServiceFeature.BusinessLogicConstant;
import com.glodon.pcop.cim.engine.dataServiceFeature.feature.DatasetFeatures;
import com.glodon.pcop.cim.engine.dataServiceFeature.feature.IndustryTypeFeatures;
import com.glodon.pcop.cim.engine.dataServiceFeature.feature.InfoObjectFeatures;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.IndustryTypeVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.InfoObjectTypeVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.PropertyTypeRestrictVO;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.PropertyTypeVO;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.model.*;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceModelRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.factory.ModelAPIComponentFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.info.InfoEndpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoObjectClear {

    private static Logger log = LoggerFactory.getLogger(InfoObjectClear.class);

    private static String spaceName = "pcop-qingdao";

    public static final String defaultIndustryName = "其他";
    public static final String defaultCreator = "pcop";

    public static void listObjectTypes(String objectTypeId) {

        CimDataSpace cds = null;
        try {
            cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(spaceName);

            boolean hasInheritFactType = cds.hasInheritFactType(objectTypeId);

            if (hasInheritFactType) {
                System.out.println("has inherit fact type: " + objectTypeId);

                InformationExplorer ie = cds.getInformationExplorer();
                ExploreParameters ep = new ExploreParameters();
                ep.setType(objectTypeId);
                List<Fact> factList = ie.discoverInheritFacts(ep);
                if (factList != null) {
                    for (Fact fact : factList) {
                        System.out.println("fact type=" + fact.getType() + "fact id=" + fact.getId());
                    }
                } else {
                    System.out.println("no fact of " + objectTypeId + " is found");
                }
            } else {
                System.out.println("does not has inherit fact type: " + objectTypeId);
            }
        } catch (CimDataEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (CimDataEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }
    }


    public static int removeAllInheritFact(CimDataSpace cds, String objectTypeId) throws CimDataEngineRuntimeException, CimDataEngineInfoExploreException {
        int count = 0;
        log.info("start to clear inherit fact type of {}", objectTypeId);
        if (cds.hasInheritFactType(objectTypeId)) {
            ExploreParameters ep = new ExploreParameters();
            ep.setType(objectTypeId);
            InformationExplorer ie = cds.getInformationExplorer();
            List<Fact> factList = ie.discoverInheritFacts(ep);
            log.info("total inherit fact count: {}", factList.size());
            if (factList != null && factList.size() > 0) {
                for (Fact fact : factList) {
                    cds.removeFact(fact.getId());
                    log.info("fact of {} is removed", fact.getId());
                }
            } else {
                log.info("no fact of {} is found", objectTypeId);
            }
        } else {
            log.info("inherit fact type of {} not found", objectTypeId);
        }
        return count;
    }

    public static int removeAllFact(CimDataSpace cds, String objectTypeId) throws CimDataEngineRuntimeException, CimDataEngineInfoExploreException {
        int count = 0;
        log.info("start to clear fact type of {}", objectTypeId);
        if (cds.hasFlatFactType(objectTypeId)) {
            ExploreParameters ep = new ExploreParameters();
            ep.setType(objectTypeId);
            InformationExplorer ie = cds.getInformationExplorer();
            List<Fact> factList = ie.discoverFacts(ep);
            log.info("total fact count: {}", factList.size());
            if (factList != null && factList.size() > 0) {
                for (Fact fact : factList) {
                    // cds.removeFact(fact.getId());
                    log.info("fact of {} is removed", fact.getId());
                }
            } else {
                log.info("no fact of inherit fact type = {} is found", objectTypeId);
            }
        } else {
            log.info("fact type of {} not found", objectTypeId);
        }
        return count;
    }

    public static int removeAllDimension(CimDataSpace cds, String objectTypeId) throws CimDataEngineRuntimeException, CimDataEngineInfoExploreException {
        int count = 0;
        log.info("start to clear dimension type of {}", objectTypeId);
        if (cds.hasDimensionType(objectTypeId)) {
            ExploreParameters ep = new ExploreParameters();
            ep.setType(objectTypeId);
            InformationExplorer ie = cds.getInformationExplorer();
            List<Dimension> dimensionList = ie.discoverDimensions(ep);
            log.info("total dimension count: {}", dimensionList.size());
            if (dimensionList != null && dimensionList.size() > 0) {
                for (Dimension dimension : dimensionList) {
                    // cds.removeFact(fact.getId());
                    log.info("dimension of {} is removed", dimension.getId());
                }
            } else {
                log.info("no dimension of {} is found", objectTypeId);
            }
        } else {
            log.info("dimension type of {} not found", objectTypeId);
        }
        return count;
    }


    public static Map<String, String> deleteObjectType(CimDataSpace cds, String objectTypeId) throws CimDataEngineRuntimeException, CimDataEngineInfoExploreException {
        Map<String, String> dataSetIds = new HashMap<>();
        //删除数据
        removeAllInheritFact(cds, objectTypeId);

        //删除data set mapping，属性集定义，属性定义
        InformationExplorer ie = cds.getInformationExplorer();
        ExploreParameters ep = new ExploreParameters();
        ep.setType(BusinessLogicConstant.INFOOBJECTTYPE_DATASET_MAPPING_FACT_TYPE_NAME);
        FilteringItem item = new EqualFilteringItem("infoObjectTypeName", objectTypeId);
        ep.setDefaultFilteringItem(item);
        List<Fact> factList = ie.discoverFacts(ep);
        if (factList != null) {
            for (Fact fact : factList) {
                String dsId = fact.getProperty("datasetId").getPropertyValue().toString();
                String infoObjectTypeName = fact.getProperty("infoObjectTypeName").getPropertyValue().toString();
                log.info("infoObjectTypeName={}, datasetId={}", infoObjectTypeName, dsId);
                dataSetIds.put(dsId, infoObjectTypeName);
                deleteDataSet(cds, dsId);
                cds.removeFact(fact.getId());
                log.info("data set and object type mapping of {} is removed", fact.getId());
            }
        }

        //删除object status
        ep = new ExploreParameters();
        ep.setType(BusinessLogicConstant.INFO_OBJECT_TYPE_STATUS_FACT_TYPE_NAME);
        ep.setDefaultFilteringItem(new EqualFilteringItem("infoObjectTypeName", objectTypeId));
        List<Fact> linkedMappingList = ie.discoverFacts(ep);
        if (linkedMappingList != null) {
            for (Fact fact : linkedMappingList) {
                cds.removeFact(fact.getId());
                log.info("object type status of {} is removed", fact.getId());
            }
        }

        return dataSetIds;
    }

    public static Map<String, String> deleteDataSet(CimDataSpace cds, String dataSetId) throws CimDataEngineRuntimeException {
        Map<String, String> propertyIds = new HashMap<>();
        Fact fact = cds.getFactById(dataSetId);

        if (fact != null) {
            List<PropertyTypeVO> propertyTypeVOList = DatasetFeatures.getLinkedPropertyTypes(cds, dataSetId);
            if (propertyTypeVOList != null) {
                for (PropertyTypeVO typeVO : propertyTypeVOList) {
                    if (typeVO.getRestrictVO() != null) {
                        PropertyTypeRestrictVO restrictVO = typeVO.getRestrictVO();
                        if (StringUtils.isNotBlank(restrictVO.getPropertyTypeRestrictId())) {
                            cds.removeFact(restrictVO.getPropertyTypeRestrictId());
                            log.info("property type restrict of {} is removed", restrictVO.getPropertyTypeRestrictId());
                        }
                    }
                    cds.removeFact(typeVO.getPropertyTypeId());
                    log.info("property type of {} is removed", typeVO.getPropertyTypeId());
                }
            } else {
                log.error("no property is linked with data set dataSetId = {}", dataSetId);
            }
            cds.removeFact(dataSetId);
            log.info("data set of {} is removed", dataSetId);
        } else {
            log.info("data set fact of {} not found", dataSetId);
        }

        return propertyIds;
    }

    public static String addIndustryType(String tenantId, String spaceName, IndustryTypeVO industryTypeVO) throws DataServiceModelRuntimeException {
        CIMModelCore cimModelCore = ModelAPIComponentFactory.getCIMModelCore(spaceName, tenantId);
        IndustryTypes industryTypes = cimModelCore.getIndustryTypes();
        IndustryType industryType;
        String parentIndustryTypeId = industryTypeVO.getParentIndustryTypeId();
        if (StringUtils.isBlank(parentIndustryTypeId)) {
            industryType = industryTypes.addRootIndustryType(industryTypeVO);
        } else {
            industryType = industryTypes.addChildIndustryType(industryTypeVO, parentIndustryTypeId);
        }

        return industryType.getIndustryTypeRID();
    }

    public static String addObjectType(String tenantId, InfoObjectTypeVO objectTypeVO) throws DataServiceModelRuntimeException {
        CIMModelCore cimModelCore = ModelAPIComponentFactory.getCIMModelCore(CimConstants.defauleSpaceName, tenantId);

        InfoObjectDefs objectDefs = cimModelCore.getInfoObjectDefs();
        InfoObjectDef objectDef;
        if (StringUtils.isBlank(objectTypeVO.getParentObjectTypeName())) {
            objectDef = objectDefs.addRootInfoObjectDef(objectTypeVO);
        } else {
            objectDef = objectDefs.addChildInfoObjectDef(objectTypeVO, objectTypeVO.getParentObjectTypeName());
        }
        if (StringUtils.isNotBlank(objectTypeVO.getIndustryTypeId())) {
            objectDef.linkIndustryType(objectTypeVO.getIndustryTypeId());
        } else {
            IndustryTypeVO defaultIndustry = IndustryTypeFeatures.getIndustryTypeByName(CimConstants.defauleSpaceName, defaultIndustryName);
            if (defaultIndustry != null) {
                objectDef.linkIndustryType(defaultIndustry.getIndustryTypeId());
            } else {
                log.error("默认行业分类：{}，不存在", defaultIndustryName);
            }
        }
        log.info("object type of {} is add", objectDef.getObjectTypeName());
        return objectDef.getObjectTypeName();
    }

    public static Map<String, String> addIndustryTypesQD() throws DataServiceModelRuntimeException {
        IndustryTypeVO typeVOa = new IndustryTypeVO();
        typeVOa.setIndustryTypeName("城市部件");
        typeVOa.setIndustryTypeDesc("城市部件");
        typeVOa.setCreatorId(defaultCreator);

        IndustryTypeVO typeVOb = new IndustryTypeVO();
        typeVOb.setIndustryTypeName("建筑");
        typeVOb.setIndustryTypeDesc("建筑");
        typeVOb.setCreatorId(defaultCreator);

        IndustryTypeVO typeVOc = new IndustryTypeVO();
        typeVOc.setIndustryTypeName("道路");
        typeVOc.setIndustryTypeDesc("道路");
        typeVOc.setCreatorId(defaultCreator);

        IndustryTypeVO typeVOd = new IndustryTypeVO();
        typeVOd.setIndustryTypeName("其他");
        typeVOd.setIndustryTypeDesc("其他");
        typeVOd.setCreatorId(defaultCreator);

        List<IndustryTypeVO> typeVOList = new ArrayList<>();
        typeVOList.add(typeVOa);
        typeVOList.add(typeVOb);
        typeVOList.add(typeVOc);
        typeVOList.add(typeVOd);

        Map<String, String> idMap = new HashMap<>();

        for (IndustryTypeVO typeVO : typeVOList) {
            String rid = addIndustryType("2", spaceName, typeVO);
            idMap.put(typeVO.getIndustryTypeName(), rid);
        }

        log.info("industry type RID: {}", idMap);
        return idMap;
    }


    public static void addObjectTypesQD() throws DataServiceModelRuntimeException {
        InfoObjectTypeVO typeVOa = new InfoObjectTypeVO();
        typeVOa.setObjectTypeName("QDLittleComponents");
        typeVOa.setObjectTypeDesc("小品");

        InfoObjectTypeVO typeVOb = new InfoObjectTypeVO();
        typeVOb.setObjectTypeName("QDTrees");
        typeVOb.setObjectTypeDesc("树木");

        InfoObjectTypeVO typeVOc = new InfoObjectTypeVO();
        typeVOc.setObjectTypeName("QDRivers");
        typeVOc.setObjectTypeDesc("河流");

        List<InfoObjectTypeVO> typeVOList = new ArrayList<>();
        typeVOList.add(typeVOa);
        typeVOList.add(typeVOb);
        typeVOList.add(typeVOc);

        for (InfoObjectTypeVO typeVO : typeVOList) {
            addObjectType("2", typeVO);
        }

    }

    public static void linkObjectWithIndustry(CimDataSpace cds, String industryRid, String[] objectTypeIds) throws CimDataEngineRuntimeException, CimDataEngineInfoExploreException {
        for (String id : objectTypeIds) {
            boolean flag = InfoObjectFeatures.linkInfoObjectTypeWithIndustryType(cds, id, industryRid);
            if (flag) {
                log.info("object type of {} is link to industry of {}", id, industryRid);
            } else {
                log.info("object type of {} is not link to industry of {}", id, industryRid);
            }
        }
    }


    public static void main(String[] args) throws CimDataEngineInfoExploreException, CimDataEngineRuntimeException {
        // listObjectTypes("cccc");

        log.info("====");

        CimDataSpace cds = null;
        try {
            // cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            // removeAllDimension(cds, "CIM_BUILDIN_INDUSTRYTYPE");
            // deleteObjectType(cds, "cccc");

            addIndustryTypesQD();
            addObjectTypesQD();

            String[] bj = {"LampPostDev", "envmonitordevice", "ledDev", "publicBroadcastingDev", "wifiDev", "cameraDev", "guideDisplay", "parking"};

            String[] jz = {"businessBuilding"};

            String[] dl = {"Road"};

            String[] qt = {"QDLittleComponents","QDTrees","QDRivers"};

        } catch (DataServiceModelRuntimeException e) {

            e.printStackTrace();
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }


    }


}
