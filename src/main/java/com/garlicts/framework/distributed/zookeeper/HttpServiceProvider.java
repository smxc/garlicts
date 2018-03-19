package com.garlicts.framework.distributed.zookeeper;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.FrameworkConstant;

public class HttpServiceProvider extends HttpService {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpServiceProvider.class);
	
	private static final Map<String,String> CACHED_URL = new ConcurrentHashMap<String,String>();
	
	private Lock _lock = new ReentrantLock();
    
    // 发布http服务并注册 RMI 地址到 ZooKeeper 中
    public void publish(String relativePath, String serviceName, String host, int port){
    	
    	logger.debug("发布http服务 开始...");
    	_lock.lock();
    	
    	String serviceUrl = publishService(relativePath, host, port);  // 发布 RMI 服务并返回 RMI 地址
    	if(serviceUrl !=null){
    		ZooKeeper zk = connectServer();   // 连接 ZooKeeper 服务器并获取 ZooKeeper 对象
    		if(zk !=null){
    			// 创建节点，并将RMI地址放入该节点上，RMI地址格式为"key|url"
    			createNode(zk, serviceName, serviceUrl);   
    		}
    		
    	}
    	
    	_lock.unlock();
    	
    }
    
    //发布http服务服务
    private String publishService(String relativePath, String host, int port){
    	String  serviceUrl = null;
    	try {
    		serviceUrl = String.format("http://%s:%d/%s", host, port, relativePath);
			logger.debug("发布http服务 (url:{})", serviceUrl);
		} catch (Exception e) {
			logger.error("",e);
		}
    	
    	return serviceUrl;
    }
    
    // 创建zookeeper节点
    private void createNode(ZooKeeper zk, String serviceName, String serviceUrl) {
        try {
        	
        	CACHED_URL.put(serviceName, serviceUrl);
            
        	byte[] data = (serviceName + "|" + serviceUrl).getBytes();
        	// 创建一个临时性且有序的zookeeper节点
            String path = zk.create(FrameworkConstant.ZK_HTTP_PROVIDER_PATH + "/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("发布http服务成功，成功创建了节点 path={}", path);
        
        } catch (Exception e) {
        	logger.error("", e);
        }
    }

}