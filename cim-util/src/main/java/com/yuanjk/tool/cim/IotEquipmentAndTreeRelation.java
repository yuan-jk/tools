package com.yuanjk.tool.cim;

import com.glodon.pcop.cim.engine.dataServiceEngine.dataMart.*;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataServiceBureau.CimDataSpace;
import com.glodon.pcop.cim.engine.dataServiceEngine.dataWarehouse.ExploreParameters;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineDataMartException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineInfoExploreException;
import com.glodon.pcop.cim.engine.dataServiceEngine.util.exception.CimDataEngineRuntimeException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IotEquipmentAndTreeRelation {
    private static final Logger log = LoggerFactory.getLogger(IotEquipmentAndTreeRelation.class);

    public static boolean addRelationType(CimDataSpace cds, String relationTypeId)
            throws CimDataEngineDataMartException {
        if (cds.hasRelationType(relationTypeId)) {
            log.info("relation type of [{}] is already exists!", relationTypeId);
            return true;
        } else {
            RelationType relationType = cds.addRelationType(relationTypeId);
            if (relationType != null) {
                return true;
            } else {
                log.error("create relation type of [{}] failed!", relationTypeId);
                return false;
            }
        }
    }

    public static void createRelation(CimDataSpace cds, String sourceTypeId, String targetTypeId, String relationType)
            throws CimDataEngineRuntimeException, CimDataEngineInfoExploreException {
        ExploreParameters ep = new ExploreParameters();
        ep.setResultNumber(Integer.MAX_VALUE);
        ep.setType(sourceTypeId);

        List<Fact> sourceFacts = cds.getInformationExplorer().discoverInheritFacts(ep);
        if (CollectionUtils.isNotEmpty(sourceFacts)) {
            int sourceFactSize = sourceFacts.size();
            ep.setResultNumber(sourceFactSize);
            ep.setType(targetTypeId);

            List<Fact> targetFacts = cds.getInformationExplorer().discoverInheritFacts(ep);

            if (CollectionUtils.isNotEmpty(targetFacts)) {
                int targetFactSize = targetFacts.size();
                for (int i = 0; i < sourceFactSize; i++) {
                    Fact sourceFact = sourceFacts.get(i);
                    List<Relation> relationList = sourceFact
                            .getAllSpecifiedRelations(relationType, RelationDirection.TWO_WAY);

                    if (CollectionUtils.isNotEmpty(relationList)) {
                        log.info("relation is already exists between relation id: [{}]", relationList.get(0).getId());
                        continue;
                    }

                    Fact targetFact = targetFacts.get(i % targetFactSize);
                    Relation relation = sourceFact.addToRelation(targetFact, relationType);
                    if (relation != null) {
                        log.info("one relation between facts is created: [{} -> {}]", sourceFact.getId(),
                                targetFact.getId());
                    } else {
                        log.info("one relation between facts add failed: [{} -> {}]", sourceFact.getId(),
                                targetFact.getId());
                    }
                }
            } else {
                log.error("no fact of target type [{}] is found", targetTypeId);
            }
        } else {
            log.error("no fact of source type [{}] is found", sourceTypeId);
        }
    }


}
