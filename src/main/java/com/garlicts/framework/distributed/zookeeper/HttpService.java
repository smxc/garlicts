package com.garlicts.framework.distributed.zookeeper;

import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.config.PropertiesProvider;

public class HttpService {

	private static final Logger logger = LoggerFactory.getLogger(HttpService.class);
	
	 // 等待 SyncConnected 事件触发后，执行当前线程
    private CountDownLatch latch = new CountDownLatch(1);	
	
    // 连接ZooKeeper服务器
    public  ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(PropertiesProvider.getString(FrameworkConstant.ZK_CONNECTION_STRING), FrameworkConstant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown(); // 递减锁存器的计数，如果计数到达零，则释放所有等待的线程
                    }
                }
            });
            
            latch.await(); // 使当前线程在锁存器倒计数至零之前一直等待
            
        } catch (Exception e) {
        	logger.error("连接zookeeper服务发生异常", e);
        }
        
        if(zk != null){
        	
        	try {
				Stat stat = zk.exists(FrameworkConstant.ZK_ROOT_PATH, false);
				if(stat == null){
					String path = zk.create(FrameworkConstant.ZK_ROOT_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					logger.info("创建了zookeeper节点 ({})", path);
				}
				stat = zk.exists(FrameworkConstant.ZK_HTTP_PROVIDER_PATH, false);
				if(stat == null){
					String path = zk.create(FrameworkConstant.ZK_HTTP_PROVIDER_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					logger.info("创建了zookeeper节点 ({})", path);
				}
        	} catch (Exception e) {
				e.printStackTrace();
			}
        	
        }        
        
        return zk;
    }	
	
}
