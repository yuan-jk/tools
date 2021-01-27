package com.jeck.tools.gis;

import com.glodon.pcop.cim.engine.dataServiceEngine.dataMart.Fact;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataMart.RelationDirection;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataWarehouse.ExploreParameters;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.config.PropertyHandler;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineInfoExploreException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.factory.CimDataEngineComponentFactory;
import com.glodon.pcop.cim.engine.dataServiceFeature.util.OrientdbConfigUtil;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.InfoObjectTypeVO;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.model.*;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.transferVO.InfoObjectValue;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.transferVO.UniversalDimensionAttachInfo;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceModelRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceUserException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.factory.ModelAPIComponentFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TiandituAdminUtil {

    public static int cnt = 0;
    private static List<Map<String, Object>> allNodes = new ArrayList<>();
    private static InfoObjectDef infoObjectDef;
    private static Map<String, String> cityCode2Rid = new HashMap<>();
    private static String relationType = "china_admin_relations_type";

    public static void main(String[] args) throws IOException {
        String filePath = "G:\\data\\geospatial_data\\北京行政区\\中华人民共和国 - 副本.json";
        jsonParser(filePath);
        //loadData();

        Map<String, String> locMap = updateProjectLocationInfo();
        String objectTypeId = "project";
        String spaceName = "gdc1";

        updateProjectLocationInfo(objectTypeId, spaceName, locMap);
    }


    public static void jsonParser(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File(filePath));
        readRecordsIntoList(jsonNode.get("data").get(0));

        System.out.println("all node number: [" + cnt + "]");
    }

    public static void readRecordsIntoList(JsonNode jsonNode) {
        cnt++;
        int level = jsonNode.get("level").getIntValue();
        String nameabbrevation = jsonNode.get("nameabbrevation").getTextValue();
        String name = jsonNode.get("name").getTextValue();
        String adminType = jsonNode.get("adminType").getTextValue();
        String cityCode = jsonNode.get("cityCode").getTextValue();
        String englishabbrevation = jsonNode.get("englishabbrevation").getTextValue();
        String english = jsonNode.get("english").getTextValue();
        String bound = jsonNode.get("bound").getTextValue();
        double lnt = jsonNode.get("lnt").getDoubleValue();
        double lat = jsonNode.get("lat").getDoubleValue();
        String points = "";
        if (jsonNode.has("points")) {
            points = jsonNode.get("points").toString();
        }
        String parentCityCode = "";
        if (jsonNode.has("parents")) {
            JsonNode parentNodes = jsonNode.get("parents");

            JsonNode pn = parentNodes.get("country");

            if (parentNodes.has("city")) {
                pn = parentNodes.get("city");
            } else if (parentNodes.has("province")) {
                pn = parentNodes.get("province");
            }
            parentCityCode = pn.get("cityCode").getTextValue();
        }

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("level", level);
        valueMap.put("nameabbrevation", nameabbrevation);
        valueMap.put("name", name);
        valueMap.put("adminType", adminType);
        valueMap.put("cityCode", cityCode);
        valueMap.put("englishabbrevation", englishabbrevation);
        valueMap.put("english", english);
        valueMap.put("lnt", lnt);
        valueMap.put("lat", lat);
        valueMap.put("parentCityCode", parentCityCode);
        valueMap.put("bound", bound);
        valueMap.put("points", points);
        valueMap.put("ID", cityCode);

        System.out.println(valueMap);
        allNodes.add(valueMap);


        if (jsonNode.has("child")) {
            Iterator<JsonNode> jsonNodeIterator = jsonNode.get("child").getElements();
            while (jsonNodeIterator.hasNext()) {
                JsonNode childJsonNode = jsonNodeIterator.next();
                readRecordsIntoList(childJsonNode);
            }
        }
    }

    public static void insertRecordIntoDB() throws DataServiceUserException, DataServiceModelRuntimeException {
        for (Map<String, Object> value : allNodes) {
            System.out.println("===" + value);
            InfoObjectValue infoObjectValue = new InfoObjectValue();
            infoObjectValue.setBaseDatasetPropertiesValue(value);
            InfoObject infoObject = infoObjectDef.newObject(infoObjectValue, false);
            cityCode2Rid.put(value.get("cityCode").toString(), infoObject.getObjectInstanceRID());

            if (value.containsKey("parentCityCode") && StringUtils.isNotBlank(value.get("parentCityCode").toString())) {
                String parentCityCode = value.get("parentCityCode").toString();
                String rid = cityCode2Rid.get(parentCityCode);
                UniversalDimensionAttachInfo attachInfo = new UniversalDimensionAttachInfo(relationType, RelationDirection.FROM, null);
                infoObject.attachInfoObject(attachInfo, rid);
            }
        }
    }

    public static void loadData() {
        PropertyHandler.map = OrientdbConfigUtil.getParameters();
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

            infoObjectDef = defs.getInfoObjectDef(objectTypeId);

            RelationTypeDefs relationTypeDefs = modelCore.getRelationTypeDefs();
            if (relationTypeDefs.getRelationTypeDef(relationType) == null) {
                relationTypeDefs.addRelationTypeDef(relationType, relationType);
            }

            insertRecordIntoDB();
        } catch (DataServiceModelRuntimeException e) {
            e.printStackTrace();
        } catch (DataServiceUserException e) {
            e.printStackTrace();
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }
    }

    public static Map<String, String> updateProjectLocationInfo() {
        Map<String, String> nameToLocationMap = new HashMap<>();
        Map<String, String> cityCodeToName = new HashMap<>();
        for (Map<String, Object> value : allNodes) {
            cityCodeToName.put(value.get("cityCode").toString(), value.get("name").toString());
            String complexName = value.get("name").toString();
            String parentCityCode = value.get("parentCityCode").toString();
            if (StringUtils.isNotBlank(parentCityCode)) {
                complexName = cityCodeToName.get(parentCityCode) + "_" + complexName;
            }

            StringBuilder locSb = new StringBuilder();
            locSb.append("POINT (").append(value.get("lnt")).append(" ").append(value.get("lat")).append(")");

            nameToLocationMap.put(complexName, locSb.toString());
        }

        System.out.println("all admin == " + nameToLocationMap);
        return nameToLocationMap;
    }

    public static void updateProjectLocationInfo(String objectTypeId, String spaceName, Map<String, String> locMap) {
        PropertyHandler.map = OrientdbConfigUtil.getParameters();
        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_SERVICE_LOCATION, "remote:10.2.23.57/");
        CimDataSpace cds = null;
        int updateCount = 0;
        try {
            cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(spaceName);

            ExploreParameters ep = new ExploreParameters();
            ep.setType(objectTypeId);
            ep.setResultNumber(Integer.MAX_VALUE);

            List<Fact> factList = cds.getInformationExplorer().discoverInheritFacts(ep);
            if (CollectionUtils.isNotEmpty(factList)) {
                for (Fact fact : factList) {
                    String city = "";
                    if (fact.hasProperty("city") && fact.getProperty("city") != null) {
                        city = fact.getProperty("city").getPropertyValue().toString().trim();
                    } else {
                        System.out.println("no city info record is omitted [" + fact.getId() + "]");
                        continue;
                    }

                    String district = "";
                    if (fact.hasProperty("district") && fact.getProperty("district") != null) {
                        district = fact.getProperty("district").getPropertyValue().toString().trim();
                    } else {
                        System.out.println("no district info record is omitted [" + fact.getId() + "]");
                        continue;
                    }

                    String location = locMap.get(city + "_" + district);
                    if (StringUtils.isNotBlank(location)) {
//                        fact.updateProperty("CIM_GeographicInformation", location);
                        System.out.println("update location info of [" + fact.getId() + "]" + ", [" + location + "]");
                        updateCount++;
                    } else {
                        System.out.println("no location info record is omitted [" + fact.getId() + "]");
                        continue;
                    }
                }
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
        System.out.println("update records count: [" + updateCount + "]");
    }

}
