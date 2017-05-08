//package com.garlicts.framework.plugin.distributed.rmi;
//
//import org.apache.zookeeper.ZooKeeper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.garlicts.framework.FrameworkConstant;
//import com.garlicts.framework.config.PropertiesProvider;
//import com.garlicts.framework.ioc.BeanContainerAbility;
//import com.garlicts.framework.plugin.Plugin;
//import com.garlicts.framework.util.ClassUtil;
//
//public class RmiPlugin implements Plugin {
//	
//	Logger logger = LoggerFactory.getLogger(RmiPlugin.class);
//	
//	@Override
//	public void init() {
//		
//		String zkEnable = PropertiesProvider.getString(FrameworkConstant.ZK_ENABLE);
//		if("true".equals(zkEnable)){
//		
//			//RmiProviderTemplate和RmiConsumerTemplate注册到Bean容器
//			Class<?> rmiProviderTemplateClass = ClassUtil.loadClass("com.garlicts.framework.plugin.distributed.rmi.RmiProviderTemplate");
//			Class<?> rmiConsumerTemplateClass = ClassUtil.loadClass("com.garlicts.framework.plugin.distributed.rmi.RmiConsumerTemplate");
//			try {
//				RmiProviderTemplate rmiProviderTemplate = (RmiProviderTemplate) rmiProviderTemplateClass.newInstance();
//				BeanContainerAbility.setBean(rmiProviderTemplateClass, rmiProviderTemplate);
//				
//				RmiConsumerTemplate rmiConsumerTemplate = (RmiConsumerTemplate) rmiConsumerTemplateClass.newInstance();
//				BeanContainerAbility.setBean(rmiConsumerTemplateClass, rmiConsumerTemplate);
//				
//				//创建ZooKeeper根节点
//				ZooKeeper zk = rmiProviderTemplate.connectServer();
//				
//				if(zk == null){
//					logger.error("创建ZooKeeper节点 /garlicts/provider 失败");
//				}
//				
//			} catch (Exception e) {
//				logger.error("RmiPlugin初始化失败，请检查ZooKeeper是否已启动！");
//				e.printStackTrace();
//			}	
//			
//		}
//		
//	}
//
//	@Override
//	public void destroy() {
//		
//	}
//
//}
