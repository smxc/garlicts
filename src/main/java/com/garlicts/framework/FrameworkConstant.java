package com.garlicts.framework;

public interface FrameworkConstant {
    
    //=================================== 框架本身需要的常量 ===================================
    
	// 项目默认编码
	String UTF_8 = "UTF-8";
    String BEAN_LOADER_TEMPLATE = "garlicts.framework.bean_loader_template";
    String DS_FACTORY = "garlicts.framework.ds_factory";
    String JDBC_TEMPLATE = "garlicts.framework.jdbc_template";
    String HANDLER_MAPPING = "garlicts.framework.handler_mapping";
    String HANDLER_INVOKER = "garlicts.framework.handler_invoker";
    String HANDLER_EXCEPTION_RESOLVER = "garlicts.framework.handler_exception_resolver";
    String VIEW_RESOLVER = "garlicts.framework.view_resolver";

    
    //=================================== 框架对外开放的常量 ===================================
    
    /**
     * 框架读取的配置文件 
     */ 
    String CONFIG_FILE = "config.properties";
//    String SQL_FILE = "sql.properties";
    String BASE_PACKAGE = "garlicts.framework.base_package";    
    
    /**
     * 数据库连接的配置 
     */
    String JDBC_DRIVER = "garlicts.jdbc.driver";
    String JDBC_URL = "garlicts.jdbc.url";
    String JDBC_USERNAME = "garlicts.jdbc.username";
    String JDBC_PASSWORD = "garlicts.jdbc.password";    
    
    /**
     * jsp存放路径 
     */ 
    String JSP_PATH = "garlicts.jsp.path";
    /**
     * 静态资源存放路径 
     */
    String WWW_PATH = "garlicts.www.path";
    
    /**
     * 项目首页 
     */
    String HOME_PAGE = "garlicts.home.page";
    /**
     * 上传限制10M  
     */
    int UPLOAD_LIMIT = 10;
    
    /**
     * 插件路径 
     */
//    String PLUGIN_PACKAGE = "garlicts.plugin.path";
    
    /**
     * redis服务器ip 
     */
    String REDIS_SERVER = "garlicts.redis.host";   
    
    /**
     * ftp 
     */
    String FTP_ENABLE = "garlicts.ftp.enable";
    String FTP_HOST = "garlicts.ftp.host";
    String FTP_PORT = "garlicts.ftp.port";
    String FTP_USERNAME = "garlicts.ftp.username";
    String FTP_PASSWORD = "garlicts.ftp.password";
    
    /**
     * zookeeper 
     */
    String ZK_ENABLE = "garlicts.zookeeper.enable";
    String ZK_CONNECTION_STRING = "garlicts.zookeeper.connection.url";
	int ZK_SESSION_TIMEOUT = 5000;
	String ZK_ROOT_PATH = "/garlicts";
	String ZK_HTTP_PROVIDER_PATH = ZK_ROOT_PATH + "/HttpProvider";
	
	String HTTP_SERVICE_HOST = "garlicts.http.service.host";
	String HTTP_SERVICE_PORT = "garlicts.http.service.port";
    
}
