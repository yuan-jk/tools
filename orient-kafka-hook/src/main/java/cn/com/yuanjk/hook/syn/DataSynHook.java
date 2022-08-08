package cn.com.yuanjk.hook.syn;

import com.orientechnologies.orient.core.hook.ORecordHookAbstract;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.config.OServerParameterConfiguration;

/**
 * @author yuanjk
 * @version 21/6/3
 */
public class DataSynHook extends ORecordHookAbstract {

    private String kafkaUrl;

    public void config(OServerParameterConfiguration[] iParams) {
        for (OServerParameterConfiguration param : iParams) {
            System.out.println("###configuration name=[" + param.name + "], value=[" + param.value + "]");
            if (param.name.equalsIgnoreCase("kafkaUrl")) {
                kafkaUrl = param.value;
            }
        }
    }

    @Override
    public DISTRIBUTED_EXECUTION_MODE getDistributedExecutionMode() {
        return DISTRIBUTED_EXECUTION_MODE.BOTH;
    }


    @Override
    public void onRecordAfterCreate(final ORecord iRecord) {
        System.out.println("===record is created: [" + iRecord.getIdentity() + "]");
    }

    @Override
    public void onRecordAfterRead(final ORecord iRecord) {
//        System.out.println("===record is read: [" + iRecord.getIdentity() + "]");
    }

    @Override
    public void onRecordFinalizeCreation(final ORecord record) {
        System.out.println("===onRecordFinalizeCreation: [" + record.getIdentity() + "]");
    }

    @Override
    public void onRecordCreateReplicated(final ORecord iRecord) {
        System.out.println("===onRecordCreateReplicated: [" + iRecord.getIdentity() + "]");

    }
}
