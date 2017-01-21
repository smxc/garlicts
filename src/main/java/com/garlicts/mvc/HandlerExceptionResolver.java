package com.garlicts.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler 异常解析器
 *
 * @author 水木星辰
 * @since 1.0
 */
public interface HandlerExceptionResolver {

    /**
     * 解析 Handler 异常
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param e        异常
     */
    void resolveHandlerException(HttpServletRequest request, HttpServletResponse response,Exception e);
}
