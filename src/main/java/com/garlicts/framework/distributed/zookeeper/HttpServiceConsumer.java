package com.garlicts.framework.distributed.zookeeper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.util.CollectionUtil;
import com.garlicts.framework.util.ThreadLocalRandom;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServiceConsumer extends HttpService{
	 
    private static final Logger logger = LoggerFactory.getLogger(HttpServiceConsumer.class);
    
    /**
     * 定义一个 volatile 成员变量，用于保存最新的http地址（考虑到该变量或许会被其它线程所修改，一旦修改后，该变量的值会影响到所有线程）
     * List中存放htp地址，如 http://127.0.0.1:20001/com.garlicts.test.rmi.HelloServiceImpl
     * 1个key代表一个接口，一个接口可能在多个服务器上都有发布，存放在List<String>
     */
    private volatile Map<String, List<String>> dataMap = new ConcurrentHashMap<String, List<String>>();
 
    private Lock _lock = new ReentrantLock();
    
    // 构造器
    public HttpServiceConsumer() {
        ZooKeeper zk = connectServer(); // 连接 ZooKeeper 服务器并获取 ZooKeeper 对象
        if (zk != null) {
            watchNode(zk);
        }
    }
 
    // 查找http服务
    public String lookup(String httpServiceName) {
    	
    	String serviceUrl = null;
    	List<String> serviceUrlList = dataMap.get(httpServiceName);
    	
    	if(CollectionUtil.isNotEmpty(serviceUrlList)){
    		
    		if(serviceUrlList.size() == 1){
    			serviceUrl = serviceUrlList.get(0);
    		}else{
    			serviceUrl = serviceUrlList.get(ThreadLocalRandom.current().nextInt(serviceUrlList.size()));
    		}
    		
    	}
    	
    	return serviceUrl;
    	
    }
 
    // 观察子节点是否有变化
    private void watchNode(final ZooKeeper zk) {
    	
    	_lock.lock();
    	
        try {
            List<String> nodeList = zk.getChildren(FrameworkConstant.ZK_HTTP_PROVIDER_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zk); // 若子节点有变化，则重新调用该方法（为了获取最新子节点中的数据）
                    }
                }
            });
            
            // 获取子节点中的数据
            List<String> dataList = new ArrayList<String>(); 
            for (String node : nodeList) {
            	
                byte[] data = zk.getData(FrameworkConstant.ZK_HTTP_PROVIDER_PATH + "/" + node, false, null);
                String dataString = new String(data);
                dataList.add(dataString);
                
                String serviceName = dataString.split("\\|")[0];
                String serviceUrl = dataString.split("\\|")[1];
                if(dataMap.containsKey(serviceName)){
                	dataMap.get(serviceName).add(serviceUrl);
                }else{
                	List<String> list = new ArrayList<String>();
                	list.add(serviceUrl);
                	dataMap.put(serviceName, list);
                }
                
            }
            
            logger.debug("node data: {}", dataList);
            this.dataMap = dataMap;
            
        } catch (Exception e) {
        	logger.error("watchNode ", e);
        }finally{
        	_lock.unlock();
        }
        
    }
 
}