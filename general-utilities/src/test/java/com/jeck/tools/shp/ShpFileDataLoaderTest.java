package com.jeck.tools.shp;

import ch.qos.logback.classic.Level;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataMart.Fact;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureauImpl.OrientDBCimDataSpaceImpl;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataWarehouse.ExploreParameters;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataWarehouse.InformationExplorer;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.config.PropertyHandler;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineInfoExploreException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.factory.CimDataEngineComponentFactory;
import com.glodon.pcop.cim.engine.dataServiceFeature.BusinessLogicConstant;
import com.glodon.pcop.cim.engine.dataServiceFeature.util.OrientdbConfigUtil;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.model.CIMModelCore;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.model.InfoObjectDef;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.transferVO.InfoObjectRetrieveResult;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.exception.DataServiceModelRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceModelAPI.util.factory.ModelAPIComponentFactory;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ShpFileDataLoaderTest {

    //    private static String tenantId = "1";
    private static String tenantId = null;
    private static String cimSpaceName = "gyd";
//    private static String objectTypeId = "gyd_road_center_lines_0816";
    private static String objectTypeId = "gyd_road_center_lines";
    //    private static String shpFile = "G:\\data\\geospatial_data\\广阳岛\\测试数据\\physical_equipments.shp";
    private static String shpFile = "G:\\data\\geospatial_data\\广阳岛\\道路中心线\\gyd_road_center_lines.shp";

    @Before
    public void setUp() throws Exception {
        PropertyHandler.map = OrientdbConfigUtil.getParameters();
//        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_SERVICE_LOCATION, "remote:10.2.112.43/");
//        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_SERVICE_LOCATION, "remote:39.106.23.94:11452/");
        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_SERVICE_LOCATION, "remote:192.168.200.124/");
        Logger rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        ((ch.qos.logback.classic.Logger) rootLogger).setLevel(Level.INFO);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void contentInsert() {
        String[] a1 = {"abc"};
        System.out.println(a1.getClass());
        Object[] a2 = a1;
        System.out.println(a2.getClass());
//        a2[0] = new Integer(17);
        a2[0] = "123";
//        a2[0] = new Object();
        String s = a1[0];
        System.out.println(s);
    }

    @Test
    public void createObjectTypeAndDataSet() {
        CimDataSpace cds = null;
        try {
            cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(cimSpaceName);
            CIMModelCore cimModelCore = ModelAPIComponentFactory.getCIMModelCore(cimSpaceName, tenantId);
            cimModelCore.setCimDataSpace(cds);

            Map<String, String> structureMap = ShpFileDataLoader.readShpFileStructure(shpFile);
            System.out.println("Structure map: " + (new Gson()).toJson(structureMap));

            InfoObjectDef infoObjectDef = ShpFileDataLoader.createObjectTypeAndDataSet(tenantId, objectTypeId + "v1", structureMap, cimModelCore);
        } catch (IOException | DataServiceModelRuntimeException e) {
            e.printStackTrace();
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }
    }

    @Test
    public void readShpFileStructure() {
        Map<String, String> structureMap = null;
        try {
            structureMap = ShpFileDataLoader.readShpFileStructure(shpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Structure map: " + (new Gson()).toJson(structureMap));
    }

    @Test
    public void shpLoader() {
        CimDataSpace cds = null;
        try {
            cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(cimSpaceName);
            CIMModelCore cimModelCore = ModelAPIComponentFactory.getCIMModelCore(cimSpaceName, tenantId);
            cimModelCore.setCimDataSpace(cds);


            Map<String, String> structureMap = ShpFileDataLoader.readShpFileStructure(shpFile);
            System.out.println("Structure map: " + (new Gson()).toJson(structureMap));

            InfoObjectDef infoObjectDef = ShpFileDataLoader.createObjectTypeAndDataSet(tenantId, objectTypeId, structureMap, cimModelCore);

//            infoObjectDef

            ((OrientDBCimDataSpaceImpl) cds).getGraph().getVertexType("").truncate();

            ShpFileDataLoader.contentInsert(infoObjectDef, shpFile);

        } catch (IOException | DataServiceModelRuntimeException e) {
            e.printStackTrace();
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }
    }

    @Test
    public void updateGeoInfo() {
        CimDataSpace cds = null;
        try {
//            String spaceName = "pcopcim";
//            String objectId = "KZX_ZX_test";
            String spaceName = "gdc";
            String objectId = "project";

            String shpFile = "G:\\data\\geospatial_data\\china\\points\\points.shp";
            Map<String, String> geoInfo = ShpFileDataLoader.readShpGeoInfo(shpFile, "osm_id");
            List<String> geoValueList = new ArrayList<>(geoInfo.values());
            int geoValueSize = geoValueList.size();
            if (geoInfo.isEmpty()) {
                System.out.println("no geo info is read from file");
            } else {
                cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(spaceName);

                ExploreParameters ep = new ExploreParameters();
                ep.setResultNumber(Integer.MAX_VALUE);
                ep.setType(objectId);

                InformationExplorer ie = cds.getInformationExplorer();
                List<Fact> factList = ie.discoverInheritFacts(ep);
                System.out.println("searched fact size: " + factList.size());
                int factSize = factList.size();
                for (int i = 0; i < factSize; i++) {
                    Fact fact = factList.get(i);
                    String geo = geoValueList.get(i % geoValueSize);

                    System.out.println("fact rid: " + fact.getId() + ": geo info: " + geo);
                    if (fact.hasProperty(BusinessLogicConstant.GeographicInformation_PROPERTY_TYPE_ID)) {
                        fact.updateProperty(BusinessLogicConstant.GeographicInformation_PROPERTY_TYPE_ID, geo);
                    } else {
                        fact.addProperty(BusinessLogicConstant.GeographicInformation_PROPERTY_TYPE_ID, geo);
                    }
                }
            }
        } catch (CimDataEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (CimDataEngineRuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }
    }

    @Test
    public void queryTest() {
        String objectTypeId = "projectV1";

        CimDataSpace cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace("pcopcim");
        try {
            CIMModelCore modelCore = ModelAPIComponentFactory.getCIMModelCore("pcopcim", "1");
            modelCore.setCimDataSpace(cds);

            InfoObjectDef objectDef = modelCore.getInfoObjectDef(objectTypeId);

            ExploreParameters ep = new ExploreParameters();
            ep.setResultNumber(10);

            InfoObjectRetrieveResult retrieveResult = objectDef.getObjects(ep);
            System.out.println("result size: [" + retrieveResult.getInfoObjects().size() + "]");
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }
    }


    @Test
    public void readShpGeoInfoTest() throws IOException {
        String shpFile = "G:\\tmp\\全岛区划结果\\gongnengqu.shp";
        String mapKey = "OBJECTID";

        ShpFileDataLoader.readShpGeoInfo(shpFile, mapKey);


    }


}