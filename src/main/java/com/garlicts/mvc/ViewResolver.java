package com.garlicts.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 视图解析器
 *
 * @author 水木星辰
 * @since 1.0
 */
public interface ViewResolver {

    /**
     * 解析视图
     *
     * @param request            请求对象
     * @param response           响应对象
     * @param controllerMethodResult Action 方法返回值
     */
    void resolveView(HttpServletRequest request, HttpServletResponse response, Object controllerMethodResult);
}
