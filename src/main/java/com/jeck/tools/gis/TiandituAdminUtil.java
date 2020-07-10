package com.jeck.tools.gis;

import com.glodon.pcop.cim.engine.dataServiceEngine.dataMart.RelationDirection;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.config.PropertyHandler;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.factory.CimDataEngineComponentFactory;
import com.glodon.pcop.cim.engine.dataServiceFeature.util.OrientdbConfigUtil;
import com.glodon.pcop.cim.engine.dataServiceFeature.vo.InfoObjectTypeVO;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.model.*;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.transferVO.InfoObjectValue;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.transferVO.UniversalDimensionAttachInfo;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceModelRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceUserException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.factory.ModelAPIComponentFactory;
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

            loadData();
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


    public static void jsonParser(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File(filePath));
        saveNode(jsonNode.get("data").get(0));

        System.out.println("all node number: [" + cnt + "]");
    }

    public static void saveNode(JsonNode jsonNode) {
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
                saveNode(childJsonNode);
            }
        }
    }

    public static void loadData() throws DataServiceUserException, DataServiceModelRuntimeException {
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


}
