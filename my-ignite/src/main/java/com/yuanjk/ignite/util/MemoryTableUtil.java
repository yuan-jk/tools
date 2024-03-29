package com.yuanjk.ignite.util;

import com.yuanjk.ignite.MemoryTablePropertyType;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.util.*;

/**
 * @author yuanjk
 * @version 22/1/17
 */
public class MemoryTableUtil {

    public static final String CIM_STUDY = "CIM_STUDY";
    public static final String ATOMICITY = "ATOMIC";
    public static final int BACKUP_NUMBER = 2;


    public static String buildMemoryTableStructureSQL(Map<String, MemoryTablePropertyType> fieldsDefinitionMap,
                                                      List<String> primaryKeysList) {

        List<String> primaryKeys = new ArrayList<>();
        if (primaryKeysList != null && primaryKeysList.size() > 0) {
            primaryKeys.addAll(primaryKeysList);
        } else {
            primaryKeys.add("CIM_AutoGeneratedPrimaryKey");
            fieldsDefinitionMap.put("CIM_AutoGeneratedPrimaryKey", MemoryTablePropertyType.INT);
        }

        if (fieldsDefinitionMap != null && fieldsDefinitionMap.size() > 0) {

            StringBuffer sb = new StringBuffer();
            Set<String> fieldNameSet = fieldsDefinitionMap.keySet();

            for (String currentFieldName : fieldNameSet) {
                MemoryTablePropertyType currentPropertyType = fieldsDefinitionMap.get(currentFieldName);
                switch (currentPropertyType) {
                    case INT:
                        sb.append(currentFieldName + " " + "INT, ");
                        break;
                    case BYTE:
                        sb.append(currentFieldName + " " + "TINYINT, ");
                        break;
                    case DATE:
                        sb.append(currentFieldName + " " + "TIMESTAMP, ");
                        break;
                    case LONG:
                        sb.append(currentFieldName + " " + "BIGINT, ");
                        break;
                    case FLOAT:
                        sb.append(currentFieldName + " " + "REAL, ");
                        break;
                    case SHORT:
                        sb.append(currentFieldName + " " + "SMALLINT, ");
                        break;
                    case BINARY:
                        sb.append(currentFieldName + " " + "BINARY, ");
                        break;
                    case DOUBLE:
                        sb.append(currentFieldName + " " + "DOUBLE, ");
                        break;
                    case STRING:
                        sb.append(currentFieldName + " " + "VARCHAR, ");
                        break;
                    case BOOLEAN:
                        sb.append(currentFieldName + " " + "BOOLEAN, ");
                        break;
                    case DECIMAL:
                        sb.append(currentFieldName + " " + "DECIMAL, ");
                        break;
                    case GEOMETRY:
                        sb.append(currentFieldName + " " + "GEOMETRY, ");
                        break;
                    case UUID:
                        sb.append(currentFieldName + " " + "UUID, ");
                        break;
                }
            }
            sb.append("PRIMARY KEY (");
            if (primaryKeys.size() > 0) {
                for (int i = 0; i < primaryKeys.size(); i++) {
                    String currentPK = primaryKeys.get(i);
                    sb.append(currentPK);
                    if (i < primaryKeys.size() - 1) {
                        sb.append(",");
                    }
                }
            } else {
                sb.append(fieldNameSet.iterator().next());
            }
            sb.append(")");
            return sb.toString();
        }
        return null;
    }

//    private String generateMemoryTableCreateSentence(String memoryTableName,
//                                                     Map<String, MemoryTablePropertyType> propertiesDefinitionMap,
//                                                     List<String> primaryKeysList, String templateType) {
//        String dataStoreBackupNumberStr = PropertyHandler.getConfigPropertyValue("igniteDataBackupsNumber");
//        String dataStoreAtomicityModeStr = PropertyHandler.getConfigPropertyValue("igniteAtomicityMode");
//        String dataStoreRegionNameStr = PropertyHandler.getConfigPropertyValue("igniteRegionName");
//        String propertiesStructureSQL = MemoryTableUtil.buildMemoryTableStructureSQL(propertiesDefinitionMap,
//                primaryKeysList);
//        String createSentence = "CREATE TABLE " + memoryTableName + " (" + propertiesStructureSQL + ") " +
//                "WITH \"CACHE_NAME=" + memoryTableName +
//                ",DATA_REGION=" + dataStoreRegionNameStr +
//                ",BACKUPS=" + dataStoreBackupNumberStr +
//                ",ATOMICITY=" + dataStoreAtomicityModeStr +
//                ",TEMPLATE=" + templateType + "\"";
//        return createSentence;
//    }

    private static String generateMemoryTableCreateSentence(String memoryTableName,
                                                            Map<String, MemoryTablePropertyType> propertiesDefinitionMap,
                                                            List<String> primaryKeysList) {
        String propertiesStructureSQL = MemoryTableUtil.buildMemoryTableStructureSQL(propertiesDefinitionMap,
                primaryKeysList);

        String createSentence = "CREATE TABLE " + memoryTableName + " (" + propertiesStructureSQL + ") " +
                "WITH \"CACHE_NAME=" + memoryTableName +
                ",DATA_REGION=Main_DataStore_Region" +
                ",BACKUPS=" + BACKUP_NUMBER +
                ",ATOMICITY=" + ATOMICITY +
                ",TEMPLATE=partitioned" + "\"";
        return createSentence;
    }

    public static void createMemoryTable(Ignite ignite, String memoryTableName,
                                         Map<String, MemoryTablePropertyType> propertiesDefinitionMap,
                                         List<String> primaryKeysList) {
        Collection<String> cacheNames = ignite.cacheNames();
        if (cacheNames.contains(memoryTableName)) {
            ignite.destroyCache(memoryTableName);
        }

        IgniteCache cache = ignite.getOrCreateCache(CIM_STUDY);


        cache.query(new SqlFieldsQuery(
                generateMemoryTableCreateSentence(memoryTableName, propertiesDefinitionMap, primaryKeysList)).setSchema(CIM_STUDY)).getAll();
    }


    public static boolean insertDataRecordOperation(Ignite ignite, String tableName,
                                                    Map<String, Object> dataPropertiesValue) {
        Set<String> dataPropertyNameSet = dataPropertiesValue.keySet();

        String[] dataPropertiesNameArray = dataPropertyNameSet.stream().toArray(n -> new String[n]);
        StringBuffer propertiesNameSb = new StringBuffer();
        StringBuffer propertiesValuePlaceHolderSb = new StringBuffer();
        propertiesNameSb.append("(");
        propertiesValuePlaceHolderSb.append("(");

        Object[] propertiesValueArray = new Object[dataPropertyNameSet.size()];

        for (int i = 0; i < dataPropertiesNameArray.length; i++) {
            String currentDataPropertyName = dataPropertiesNameArray[i];
            propertiesNameSb.append(currentDataPropertyName);
            propertiesValuePlaceHolderSb.append("?");

            if (i < dataPropertiesNameArray.length - 1) {
                propertiesNameSb.append(",");
                propertiesValuePlaceHolderSb.append(",");
            }
            propertiesValueArray[i] = dataPropertiesValue.get(currentDataPropertyName);
        }
        propertiesNameSb.append(")");
        propertiesValuePlaceHolderSb.append(")");

        String sqlFieldsQuerySQL = "INSERT INTO" + " " + tableName + " " + propertiesNameSb.toString() + " VALUES " + propertiesValuePlaceHolderSb.toString();
        SqlFieldsQuery qry = new SqlFieldsQuery(sqlFieldsQuerySQL);
        try {
            List<List<?>> cursor = ignite.getOrCreateCache(tableName).query(qry.setArgs(propertiesValueArray)).getAll();
            for (List next : cursor) {
                for (Object item : next) {
                    if (item instanceof Long) {
                        Long result = (Long) item;
                        if (result == 1) {
                            return true;
                        }
                    }
                }
            }
        } catch (javax.cache.CacheException e) {
            System.out.println(e.toString());
        }
        return false;
    }


}
