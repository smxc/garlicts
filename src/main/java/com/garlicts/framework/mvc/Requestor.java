package com.garlicts.framework.mvc;

/**
 * 封装 Request 对象相关信息
 *
 * @author 水木星辰
 * @since 1.0
 */
public class Requestor {
	
	//请求方法 get post 
    private String requestMethod;
    //请求路径
    private String requestPath;

    public Requestor(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }
}