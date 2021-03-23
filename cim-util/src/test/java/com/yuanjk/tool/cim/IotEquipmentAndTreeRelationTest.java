package com.yuanjk.tool.cim;

import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.config.PropertyHandler;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineDataMartException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineInfoExploreException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineRuntimeException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.factory.CimDataEngineComponentFactory;
import com.glodon.pcop.cim.engine.dataServiceFeature.util.OrientdbConfigUtil;
import junit.framework.TestCase;

public class IotEquipmentAndTreeRelationTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

        PropertyHandler.map = OrientdbConfigUtil.getParameters();
        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_SERVICE_LOCATION, "remote:192.168.200.124/");
//        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_SERVICE_LOCATION, "remote:39.106.23.94:11452/");
//        PropertyHandler.map.put(PropertyHandler.DISCOVER_ENGINE_SERVICE_LOCATION, "remote:localhost/");
    }

    public void tearDown() throws Exception {
    }

    public void testAddRelationType() throws CimDataEngineDataMartException {
        String spaceName = "gyd";
//        String spaceName = "gdc";
        String relationType = "physical_equipment_and_tree_relation_type";
        CimDataSpace cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
        try {
            IotEquipmentAndTreeRelation.addRelationType(cds, relationType);
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }
    }

    public void testCreateRelation() throws CimDataEngineRuntimeException, CimDataEngineInfoExploreException {
        String sourceType = "PhysicEquipment";
        String targetType = "singleTree";
        String spaceName = "gyd";
        String relationType = "physical_equipment_and_tree_relation_type";
        CimDataSpace cds = CimDataEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
        try {
            IotEquipmentAndTreeRelation.createRelation(cds, sourceType, targetType, relationType);
        } finally {
            if (cds != null) {
                cds.closeSpace();
            }
        }
    }
}