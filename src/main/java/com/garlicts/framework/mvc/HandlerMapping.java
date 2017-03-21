package com.garlicts.framework.mvc;

/**
 * 处理器映射
 *
 * @author 水木星辰
 * @since 1.0
 */
public interface HandlerMapping {

    /**
     * 获取 Handler
     *
     * @param currentRequestMethod 当前请求方法
     * @param currentRequestPath   当前请求路径
     * @return Handler
     */
    Handler getHandler(String currentRequestMethod, String currentRequestPath);
}
