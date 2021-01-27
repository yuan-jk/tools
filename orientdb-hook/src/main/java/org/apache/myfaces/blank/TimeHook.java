package org.apache.myfaces.blank;


import com.orientechnologies.orient.core.hook.ODocumentHookAbstract;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.Date;
import java.util.Set;


public class TimeHook extends ODocumentHookAbstract {

    public TimeHook() {
        setIncludeClasses("GLD_IH_FACT");
    }


    @Override
    public DISTRIBUTED_EXECUTION_MODE getDistributedExecutionMode() {
        return DISTRIBUTED_EXECUTION_MODE.BOTH;
    }


    @Override
    public RESULT onRecordBeforeCreate(ODocument iDocument) {
        System.out.println("before create hook");
        Set<String> names = iDocument.getPropertyNames();
        for (String nm : names) {
            System.out.println("before create hook property name: " + nm + ", value: " + iDocument.field(nm));

        }
//		if ((iDocument.getClassName().startsWith(TimeConstants.GLD_IH_FACT_))
//				|| (iDocument.getClassName().startsWith(TimeConstants.GLD_DIMENSION_))) {
        Date now = new Date();
        iDocument.field(TimeConstants.CREATE_TIME, now);
        iDocument.field(TimeConstants.UPDATE_TIME, now);
//        iDocument.setProperty(TimeConstants.CREATE_TIME, now);
//        iDocument.setProperty(TimeConstants.UPDATE_TIME, now);
        return RESULT.RECORD_CHANGED;
//        return RESULT.RECORD_REPLACED;
//		}else{
//			return RESULT.RECORD_NOT_CHANGED;
//		}
    }


    @Override
    public void onRecordAfterCreate(ODocument iDocument) {
        System.out.println("after create hook");
        System.out.println(iDocument.getIdentity());
        Set<String> names = iDocument.getPropertyNames();
        for (String nm : names) {
            System.out.println("before create hook property name: " + nm + ", value: " + iDocument.field(nm));
        }
//		if ((iDocument.getClassName().startsWith(TimeConstants.GLD_IH_FACT_))
//				|| (iDocument.getClassName().startsWith(TimeConstants.GLD_DIMENSION_))) {
        Date now = new Date();
        iDocument.save();
        System.out.println("===" + iDocument.getIdentity());
//        iDocument.field(TimeConstants.CREATE_TIME + "1", now);
//        iDocument.field(TimeConstants.UPDATE_TIME + "1", now);
//        iDocument.setProperty(TimeConstants.CREATE_TIME, now);
//        iDocument.setProperty(TimeConstants.UPDATE_TIME, now);
//		return RESULT.RECORD_CHANGED;
//		}else{
//			return RESULT.RECORD_NOT_CHANGED;
//		}
    }

//	@Override
//	public RESULT onRecordBeforeCreate(ODocument iDocument) {
//		System.out.println("create hook");
//		System.out.println(iDocument.getIdentity());
////		if ((iDocument.getClassName().startsWith(TimeConstants.GLD_IH_FACT_))
////				|| (iDocument.getClassName().startsWith(TimeConstants.GLD_DIMENSION_))) {
//		Date now = new Date();
//		iDocument.field(TimeConstants.CREATE_TIME, now);
//		iDocument.field(TimeConstants.UPDATE_TIME, now);
//		return RESULT.RECORD_CHANGED;
////		}else{
////			return RESULT.RECORD_NOT_CHANGED;
////		}
//	}

//	@Override
//	public RESULT onRecordBeforeUpdate(ODocument iDocument) {
//		System.out.println("update hook");
//		System.out.println(iDocument.getIdentity());
//
////		if ((iDocument.getClassName().startsWith(TimeConstants.GLD_IH_FACT_))
////				|| (iDocument.getClassName().startsWith(TimeConstants.GLD_DIMENSION_))) {
//			iDocument.field(TimeConstants.UPDATE_TIME, new Date());
//			return RESULT.RECORD_CHANGED;
////		}else{
////			return RESULT.RECORD_NOT_CHANGED;
////		}
//	}


}