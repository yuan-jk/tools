package com.jeck.tools.ehcache;

import com.google.gson.Gson;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.clustered.client.config.builders.ClusteredStoreConfigurationBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.clustered.common.Consistency;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.util.HashSet;

import static org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder.clusteredShared;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;

public class OperationsTest {
    public static String CacheDir = "CacheDir";
    private static Logger log = LoggerFactory.getLogger(OperationsTest.class);

    private static PersistentCacheManager cacheManager;
    public static Gson gson = new Gson();

    public static final String CLUSTER_RESOURCE_POOL = "CIM_BUILDIN_CLUSTER_RESOURCE_POOL";

    public static String CLUSTER_RESOURCE_CACHE_SERVICE_LOCATION = "terracotta://localhost:9410/clustered";
    public static String CLUSTER_RESOURCE_CACHE_SERVICE_DEFAULT_RESOURCE_ID = "main";
    public static String CLUSTER_RESOURCE_CACHE_SERVICE_SHARE_RESOURCE_ID = "primary-server-resource";
    public static boolean CLUSTER_RESOURCE_CACHE_ENABLE = true;
    public static String OBJECT_AND_INDUSTRY_MAPPING_CACHE = "OBJECT_AND_INDUSTRY_MAPPING_CACHE";
    public static String ROOT_INDUSTRY_CACHE = "ROOT_INDUSTRY_CACHE";
    public static String RELATIONSHIP_MAPPING_CACHE = "RELATIONSHIP_MAPPING_CACHE";

    public static void cacheManagerInit() {
        if (cacheManager == null) {
            if (isClusterMode()) {
                CacheManagerBuilder<PersistentCacheManager> clusteredCacheManagerBuilder =
                        CacheManagerBuilder.newCacheManagerBuilder()
                                .with(ClusteringServiceConfigurationBuilder.cluster(URI.create(CLUSTER_RESOURCE_CACHE_SERVICE_LOCATION)).autoCreate()
                                        .defaultServerResource(CLUSTER_RESOURCE_CACHE_SERVICE_DEFAULT_RESOURCE_ID)
                                        .resourcePool(CLUSTER_RESOURCE_POOL, 256, MemoryUnit.MB, CLUSTER_RESOURCE_CACHE_SERVICE_SHARE_RESOURCE_ID));
                cacheManager = clusteredCacheManagerBuilder.build(true);
            } else {
                cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                        .with(CacheManagerBuilder.persistence(new File(CacheDir, "myData")))
                        .build(true);
            }
        }
    }

    private static boolean isClusterMode() {
        if (Boolean.valueOf(CLUSTER_RESOURCE_CACHE_ENABLE)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回指定的cache，若该cache不存在，则创建对应的cache
     *
     * @param cacheName
     * @param _KClass
     * @param _VClass
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Cache<K, V> getOrCreateCache(String cacheName, Class<K> _KClass, Class<V> _VClass) {
        if (cacheManager == null) {
            cacheManagerInit();
        }
        Cache<K, V> targetCache = cacheManager.getCache(cacheName, _KClass, _VClass);
        if (targetCache == null) {
            if (isClusterMode()) {
                targetCache = cacheManager.createCache(cacheName, newCacheConfigurationBuilder(_KClass, _VClass, ResourcePoolsBuilder.newResourcePoolsBuilder().with(clusteredShared(CLUSTER_RESOURCE_POOL)))
                        .add(ClusteredStoreConfigurationBuilder.withConsistency(Consistency.STRONG))
                        .withSizeOfMaxObjectSize(5, MemoryUnit.KB)
                        .withSizeOfMaxObjectGraph(100000)
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(3L))).build());
                // .withExpiry(ExpiryPolicyBuilder.noExpiration()).build());
            } else {
                targetCache = cacheManager.createCache(cacheName, newCacheConfigurationBuilder(_KClass, _VClass, ResourcePoolsBuilder.newResourcePoolsBuilder().disk(32, MemoryUnit.MB, true))
                        .withSizeOfMaxObjectSize(5, MemoryUnit.KB)
                        .withSizeOfMaxObjectGraph(100000)
                        .withExpiry(ExpiryPolicyBuilder.noExpiration()).build());
            }
            log.info("New cache cache_name={} is created!", cacheName);
        } else {
            log.debug("Cache cache_name={} is already exists!", cacheName);
        }
        return targetCache;
    }

    public static void addOrUpdateSetCacheItem(String cacheName, String key, HashSet<String> values) {
        Cache<String, HashSet> cache = getOrCreateCache(cacheName, String.class, HashSet.class);
        if (cache != null) {
            cache.put(key, values);
            log.info("add or update cache set item: cache_name={}, key={}, values={}", cacheName, key, gson.toJson(values));
            log.info("get after add or update cache set item: values={}", gson.toJson(cache.get(key)));
        } else {
            log.error("Cache not found: {}", cacheName);
        }
    }

    public static HashSet<String> getSetCacheItem(String cacheName, String key) {
        Cache<String, HashSet> cache = getOrCreateCache(cacheName, String.class, HashSet.class);
        if (cache != null) {
            HashSet<String> values = cache.get(key);
            log.info("query cache set item: cache_name={}, key={}, values={}", cacheName, key, gson.toJson(values));
            return values;
        } else {
            log.error("Cache not found: cache_name={}", cacheName);
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String cacheName = "testCache";
        String key = "k1";
        HashSet<String> values = new HashSet<>();
        values.add("one");
        values.add("two");
        values.add("three");
        HashSet<String> resultValues = getSetCacheItem(cacheName, key);
        System.out.println("==before add: " + gson.toJson(resultValues));

        addOrUpdateSetCacheItem(cacheName, key, values);
        resultValues = getSetCacheItem(cacheName, key);
        System.out.println("==after add: " + gson.toJson(resultValues));

        System.out.println("start to wait 5 seconds");
        Thread.sleep(5 * 1000);
        resultValues = getSetCacheItem(cacheName, key);
        System.out.println("==after add: " + gson.toJson(resultValues));

        values.add("123");
        addOrUpdateSetCacheItem(cacheName, key, values);
        resultValues = getSetCacheItem(cacheName, key);
        System.out.println("==after update: " + gson.toJson(resultValues));
    }


}
