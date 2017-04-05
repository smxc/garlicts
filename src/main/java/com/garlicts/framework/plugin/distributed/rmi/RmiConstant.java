package com.garlicts.framework.plugin.distributed.rmi;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.config.PropertiesProvider;

public interface RmiConstant {
	String ZK_CONNECTION_STRING = PropertiesProvider.getString(FrameworkConstant.ZK_HOST, "127.0.0.1")
			+ ":"
			+ PropertiesProvider.getString(FrameworkConstant.ZK_PORT, "2181");
	int ZK_SESSION_TIMEOUT = 5000;
	String ZK_ROOT_PATH = "/garlicts";
	String ZK_PROVIDER_PATH = ZK_ROOT_PATH + "/provider";
}
