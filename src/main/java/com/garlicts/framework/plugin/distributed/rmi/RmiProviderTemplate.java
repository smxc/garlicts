package com.garlicts.framework.plugin.distributed.rmi;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmiProviderTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(RmiProviderTemplate.class);
	
	private static final Map<String,String> CACHED_URL = new HashMap<String,String>();
	
	private Lock _lock = new ReentrantLock();
	 // 等待 SyncConnected 事件触发后，执行当前线程
    private CountDownLatch latch = new CountDownLatch(1);
    
    // 发布 RMI 服务并注册 RMI 地址到 ZooKeeper 中
    public void publish(Remote remote, String key, String host, int port){
    	
    	logger.debug("发布RMI服务 开始...");
    	_lock.lock();
    	
    	String url = publishService(remote, host, port);  // 发布 RMI 服务并返回 RMI 地址
    	if(url !=null){
    		ZooKeeper zk = connectServer();   // 连接 ZooKeeper 服务器并获取 ZooKeeper 对象
    		if(zk !=null){
    			// 创建节点，并将RMI地址放入该节点上，RMI地址格式为"key|url"
    			createNode(zk, url, key);   
    		}
    		
    	}
    	
    	_lock.unlock();
    	
    }
    
    //发布RMI服务
    private String publishService(Remote remote, String host, int port){
    	String  url = null;
    	try {
			url = String.format("rmi://%s:%d/%s", host, port, remote.getClass().getName());
			LocateRegistry.createRegistry(port);
			Naming.rebind(url, remote);
			logger.debug("发布RMI服务 (url:{})", url);
		} catch (Exception e) {
			logger.error("",e);
		}
    	
    	return url;
    }
    
    
    // 连接ZooKeeper服务器
    public  ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(RmiConstant.ZK_CONNECTION_STRING, RmiConstant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown(); // 递减锁存器的计数，如果计数到达零，则释放所有等待的线程
                    }
                }
            });
            
            logger.info("链接zookeeper服务器成功！");
            latch.await(); // 使当前线程在锁存器倒计数至零之前一直等待
            
        } catch (Exception e) {
        	logger.error("", e);
        }
        
        if(zk != null){
        	
        	try {
				Stat stat = zk.exists(RmiConstant.ZK_ROOT_PATH, false);
				if(stat == null){
					String path = zk.create(RmiConstant.ZK_ROOT_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					logger.info("创建了zookeeper节点 ({})", path);
				}
				stat = zk.exists(RmiConstant.ZK_PROVIDER_PATH, false);
				if(stat == null){
					String path = zk.create(RmiConstant.ZK_PROVIDER_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					logger.info("创建了zookeeper节点 ({})", path);
				}
        	} catch (Exception e) {
				e.printStackTrace();
			}
        	
        }        
        
        return zk;
    }
    
    // 创建zookeeper节点
    private void createNode(ZooKeeper zk, String url, String key) {
        try {
        	
        	CACHED_URL.put(key, url);
            
        	byte[] data = (key + "|" + url).getBytes();
        	// 创建一个临时性且有序的zookeeper节点
            String path = zk.create(RmiConstant.ZK_PROVIDER_PATH + "/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("发布RMI服务成功，成功创建了节点 path => url ({} => {})", path, url);
        
        } catch (Exception e) {
        	logger.error("", e);
        }
    }

}
