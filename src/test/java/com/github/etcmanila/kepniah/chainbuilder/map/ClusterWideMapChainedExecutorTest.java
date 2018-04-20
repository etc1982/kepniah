package com.github.etcmanila.kepniah.chainbuilder.map;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;
import com.github.etcmanila.kepniah.chainbuilder.ChainedExecutor;
import com.hazelcast.config.Config;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ClusterWideMapChainedExecutorTest {

    private boolean executedNext = false;
    private Vertx vertx;
    private Throwable throwable;
    private boolean executedErrHandler = false;
    
    @Before
    public void setup() throws Throwable {
        Config hazelcastConfig = new Config();

        HazelcastClusterManager clusterManager = new HazelcastClusterManager(hazelcastConfig);

        Vertx.clusteredVertx(new VertxOptions().setClusterManager(clusterManager), r -> {
            if (r.succeeded()) {
                this.vertx = r.result();
            } else {
                throwable = r.cause();
            }
        });
        while (vertx==null && throwable==null) TimeUnit.SECONDS.sleep(1L);;
        if (throwable != null) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

    @Test
    public void retrievsMapAndPutItToChainContextPayload() throws Throwable {
       
        String mapName = "theMap";
        
        ClusterWideMapChainedExecutor uut = new ClusterWideMapChainedExecutor(vertx, mapName);

        uut.setNext(new ChainedExecutor() {

            @Override
            public void execute(ChainContext context) {
                executedNext = true;
            }
        });
        ChainContext ctx = new ChainContext();
        uut.execute(ctx);
        TimeUnit.SECONDS.sleep(1L);
        Assert.assertTrue(ctx.getPayload(Object.class) instanceof AsyncMap);
        Assert.assertTrue(executedNext);
        vertx.close();

    }
    
//    @Test
//    public void executeErrorHandlerForException() throws Throwable {
//       
//        String mapName = "theMap";
//        
//        ClusterWideMapChainedExecutor uut = new ClusterWideMapChainedExecutor(vertx, mapName);
//        vertx.close();
//        uut.setError(new ChainedExecutor() {
//            
//            @Override
//            public void execute(ChainContext context) {
//                executedErrHandler = true;
//            }
//        });
//        ChainContext ctx = new ChainContext();
//        uut.execute(ctx);
//        TimeUnit.SECONDS.sleep(1L);
//        Assert.assertTrue(executedErrHandler);
//        vertx.close();
//
//    }

}
