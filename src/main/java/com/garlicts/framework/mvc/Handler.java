package com.garlicts.framework.mvc;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

/**
 * 封装 Action 方法相关信息
 *
 * @author 水木星辰
 * @since 1.0
 */
public class Handler {

    private Class<?> controllerClass;
    private Method controllerMethod;
    private Matcher requestPathMatcher;

    public Handler(Class<?> controllerClass, Method controllerMethod) {
        this.controllerClass = controllerClass;
        this.controllerMethod = controllerMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }

    public Matcher getRequestPathMatcher() {
        return requestPathMatcher;
    }

    public void setRequestPathMatcher(Matcher requestPathMatcher) {
        this.requestPathMatcher = requestPathMatcher;
    }
}