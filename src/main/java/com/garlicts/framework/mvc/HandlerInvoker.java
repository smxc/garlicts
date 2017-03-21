package com.garlicts.framework.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler 调用器
 *
 * @author 水木星辰
 * @since 1.0
 */
public interface HandlerInvoker {

    /**
     * 调用 Handler
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  Handler
     * @throws Exception 异常
     */
    void invokeHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception;
}
