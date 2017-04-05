package com.garlicts.framework.plugin.distributed.rmi;

import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.garlicts.framework.util.ThreadLocalRandom;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmiConsumerTemplate {
	 
    private static final Logger logger = LoggerFactory.getLogger(RmiConsumerTemplate.class);
 
    // 等待 SyncConnected 事件触发后，则执行当前线程
    private CountDownLatch latch = new CountDownLatch(1);
 
    // 定义一个 volatile 成员变量，用于保存最新的 RMI 地址（考虑到该变量或许会被其它线程所修改，一旦修改后，该变量的值会影响到所有线程）
    // List中存放RMI地址，如 rmi://127.0.0.1:20001/com.garlicts.test.rmi.HelloServiceImpl
    // 1个key代表一个接口，一个接口可能在多个服务器上都有发布，存放在List<String>
    private volatile Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
 
    private Lock _lock = new ReentrantLock();
    
    // 构造器
    public RmiConsumerTemplate() {
        ZooKeeper zk = connectServer(); // 连接 ZooKeeper 服务器并获取 ZooKeeper 对象
        if (zk != null) {
            watchNode(zk);
        }
    }
 
    // 查找 RMI 服务
    public <T extends Remote> T lookup(String key) {
        T service = null;
        int size = dataMap.size();
        if (size > 0) {
            String url = null;
            if(dataMap.containsKey(key)){
            	List<String> urlList = dataMap.get(key);
            	if(urlList.size() > 0){
            		if(urlList.size() == 1){
            			url = urlList.get(0);
            		}else{
            			url = urlList.get(ThreadLocalRandom.current().nextInt(size));
            		}
            	}
            	service = lookupService(url, key); // 从 JNDI 中查找 RMI 服务
            }
            
        }
        return service;
    }
 
    // 连接ZooKeeper服务器
    private ZooKeeper connectServer() {
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
            latch.await(); // 使当前线程在锁存器倒计数至零之前一直等待
        } catch (Exception e) {
        	logger.error("ZooKeeper连接失败", e);
        }
        
        return zk;
        
    }
 
    // 观察 /registry 节点下所有子节点是否有变化
    private void watchNode(final ZooKeeper zk) {
    	
    	_lock.lock();
    	
        try {
            List<String> nodeList = zk.getChildren(RmiConstant.ZK_PROVIDER_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zk); // 若子节点有变化，则重新调用该方法（为了获取最新子节点中的数据）
                    }
                }
            });
            
            // 用于存放 /registry 所有子节点中的数据
            List<String> dataList = new ArrayList<String>(); 
            for (String node : nodeList) {
                byte[] data = zk.getData(RmiConstant.ZK_PROVIDER_PATH + "/" + node, false, null); // 获取 /registry 的子节点中的数据
                dataList.add(new String(data));
                
                String d = new String(data).toString();
                String key = d.split("\\|")[0];
                String url = d.split("\\|")[1];
                if(dataMap.containsKey(key)){
                	dataMap.get(key).add(url);
                }else{
                	List<String> list = new ArrayList<String>();
                	list.add(url);
                	dataMap.put(key, list);
                }
            }
            logger.debug("node data: {}", dataList);
            this.dataMap = dataMap;
        } catch (Exception e) {
        	logger.error("", e);
        }
        
        _lock.unlock();
        
    }
 
    // 在 JNDI 中查找 RMI 远程服务对象
    @SuppressWarnings("unchecked")
    private <T> T lookupService(String url, String key) {
        T remote = null;
        try {
            remote = (T) Naming.lookup(url);
        } catch (Exception e) {
            if (e instanceof ConnectException) {
                // 若连接中断，则使用 urlList 中第一个 RMI 地址来查找（这是一种简单的重试方式，确保不会抛出异常）
            	logger.error("ConnectException -> url: {}", url);
            	
            	if(dataMap.containsKey(key)){
            		List<String> urlList = dataMap.get(key);
            		if(urlList.size() > 0){
            			return lookupService(urlList.get(0), key);
            		}
            	}
            	
            }
            logger.error("", e);
        }
        return remote;
    }
}