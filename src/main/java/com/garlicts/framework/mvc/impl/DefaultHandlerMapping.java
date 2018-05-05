package com.garlicts.framework.mvc.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.garlicts.framework.mvc.ControllerComponent;
import com.garlicts.framework.mvc.Handler;
import com.garlicts.framework.mvc.HandlerMapping;
import com.garlicts.framework.mvc.Requestor;

/**
 * 默认处理器映射
 *
 * @author 水木星辰
 * @since 1.0
 */
public class DefaultHandlerMapping implements HandlerMapping {

    @Override
    public Handler getHandler(String currentRequestMethod, String currentRequestPath) {
        // 定义一个 Handler
        Handler handler = null;
        // 获取并遍历 Action 映射
        Map<Requestor, Handler> controllerMap = ControllerComponent.getControllerMap();
        for (Map.Entry<Requestor, Handler> controllerEntry : controllerMap.entrySet()) {
            // 从 Requestor 中获取 Request 相关属性
            Requestor requestor = controllerEntry.getKey();
            String requestMethod = requestor.getRequestMethod();
            String requestPath = requestor.getRequestPath(); // 正则表达式
            // 获取请求路径匹配器（使用正则表达式匹配请求路径并从中获取相应的请求参数）
            Matcher requestPathMatcher = Pattern.compile(requestPath).matcher(currentRequestPath);
            // 判断请求方法与请求路径是否同时匹配
            if (requestMethod.equalsIgnoreCase(currentRequestMethod) && requestPathMatcher.matches()) {
                // 获取 Handler 及其相关属性
                handler = controllerEntry.getValue();
                // 设置请求路径匹配器
                if (handler != null) {
                    handler.setRequestPathMatcher(requestPathMatcher);
                }
                // 若成功匹配，则终止循环
                break;
            }
        }
        // 返回该 Handler
        return handler;
    }
}
