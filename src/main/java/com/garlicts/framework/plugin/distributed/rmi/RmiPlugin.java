package com.garlicts.framework.plugin.distributed.rmi;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.ioc.BeanContainerAbility;
import com.garlicts.framework.plugin.Plugin;
import com.garlicts.framework.util.ClassUtil;

public class RmiPlugin implements Plugin {
	
	Logger logger = LoggerFactory.getLogger(RmiPlugin.class);
	
	@Override
	public void init() {
		
		//RmiProviderTemplate和RmiConsumerTemplate注册到Bean容器
		Class<?> rmiProviderTemplateClass = ClassUtil.loadClass("com.garlicts.framework.plugin.distributed.rmi.RmiProviderTemplate");
		Class<?> rmiConsumerTemplateClass = ClassUtil.loadClass("com.garlicts.framework.plugin.distributed.rmi.RmiConsumerTemplate");
		try {
			RmiProviderTemplate rmiProviderTemplate = (RmiProviderTemplate) rmiProviderTemplateClass.newInstance();
			BeanContainerAbility.setBean(rmiProviderTemplateClass, rmiProviderTemplate);
			
			RmiConsumerTemplate rmiConsumerTemplate = (RmiConsumerTemplate) rmiConsumerTemplateClass.newInstance();
			BeanContainerAbility.setBean(rmiConsumerTemplateClass, rmiConsumerTemplate);
			
			//创建ZooKeeper根节点
			ZooKeeper zk = rmiProviderTemplate.connectServer();
//	        if(zk != null){
//	        	
//	        	try {
//					Stat stat = zk.exists(RmiConstant.ZK_ROOT_PATH, false);
//					if(stat == null){
//						String path = zk.create(RmiConstant.ZK_ROOT_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//						logger.info("创建了ZooKeeper根节点 ({})", path);
//					}
//					stat = zk.exists(RmiConstant.ZK_PROVIDER_PATH, false);
//					if(stat == null){
//						String path = zk.create(RmiConstant.ZK_PROVIDER_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//						logger.info("创建了ZooKeeper子节点 ({})", path);
//					}
//	        	} catch (Exception e) {
//	        		logger.error("创建ZooKeeper节点 /garlicts/provider 失败");
//					e.printStackTrace();
//				}finally{
//					zk.close();
//				}
//	        	
//	        }  
			
			if(zk == null){
				logger.error("创建ZooKeeper节点 /garlicts/provider 失败");
			}
			
		} catch (Exception e) {
			logger.error("RmiPlugin初始化失败，请检查ZooKeeper是否已启动！");
			e.printStackTrace();
		}		
		
	}

	@Override
	public void destroy() {
		
	}

}
