package com.yuanjk.ignite;

import com.yuanjk.ignite.util.MemoryTableUtil;
import com.yuanjk.ignite.util.SpatialDataInfo;
import com.yuanjk.ignite.util.SpatialDataMaintainUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterState;

import java.io.File;

/**
 * @author yuanjk
 * @version 22/1/17
 */
public class CreateTableUtil {

    public static void main(String[] args) {
        Ignite ignite = Ignition.start("cim-config.xml");

        IgniteCluster igniteCluster = ignite.cluster();
        if (!igniteCluster.state().active()) {
            igniteCluster.state(ClusterState.ACTIVE);
        }
//        ignite.active(true);
//        IgniteCluster igniteCluster = IgniteClust
//        IgniteCache cache = ignite.getOrCreateCache("CIM_STUDY");
        String tableName = "test_table";
        String shpFile = "G:\\data\\geospatial_data\\广阳岛\\danmu\\ta_danmu_202101181350.shp";
        SpatialDataMaintainUtil dataMaintainUtil = new SpatialDataMaintainUtil();
        SpatialDataInfo spatialDataInfo = dataMaintainUtil.parseSHPData(new File(shpFile), null);

        System.out.println("data size: " + spatialDataInfo.spatialDataValue().size());

        MemoryTableUtil.createMemoryTable(ignite, tableName, spatialDataInfo.spatialDataPropertiesDefinition(), null);
        dataMaintainUtil.duplicateSpatialDataInfoToMemoryTable(ignite, spatialDataInfo, tableName, true, null);


    }


}
