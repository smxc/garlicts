package com.garlicts.framework.mvc.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.ioc.BeanContainerAbility;
import com.garlicts.framework.mvc.GarlictsContext;
import com.garlicts.framework.mvc.ViewResolver;
import com.garlicts.framework.mvc.bean.Params;
import com.garlicts.framework.security.annotation.Role;
import com.garlicts.framework.util.ClassUtil;
import com.garlicts.framework.util.ConvertUtil;
import com.garlicts.framework.util.MapUtil;
import com.garlicts.framework.util.StringUtil;
import com.garlicts.framework.util.WebUtil;
import com.garlicts.framework.mvc.Handler;
import com.garlicts.framework.mvc.HandlerInvoker;

/**
 * 默认 Handler 调用器
 *
 * @author 水木星辰
 * @since 1.0
 */
public class DefaultHandlerInvoker implements HandlerInvoker {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultHandlerInvoker.class);

    private ViewResolver viewResolver = InstanceFactory.getViewResolver();

    @Override
    public void invokeHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception {
        // 获取 Action 相关信息
        Class<?> controllerClass = handler.getControllerClass();
        Method controllerMethod = handler.getControllerMethod();
        
        Object controllerInstance = BeanContainerAbility.getBean(controllerClass);
        // 创建 Action 方法的参数列表
        List<Object> actionMethodParamList = createControllerMethodParamList(request, handler);
        // 检查参数列表是否合法
//        checkParamList(controllerMethod, actionMethodParamList);
        // 调用 Action 方法
        Object actionMethodResult = invokeActionMethod(controllerMethod, controllerInstance, actionMethodParamList);
        // 解析视图
        viewResolver.resolveView(request, response, actionMethodResult);
    }

    public List<Object> createControllerMethodParamList(HttpServletRequest request, Handler handler) throws Exception {
        // 定义参数列表
        List<Object> paramList = new ArrayList<Object>();
        // 获取 Action 方法参数类型
        Class<?>[] controllerParamTypes = handler.getControllerMethod().getParameterTypes();
        // 添加路径参数列表（请求路径中的带占位符参数）
        paramList.addAll(createPathParamList(handler.getRequestPathMatcher(), controllerParamTypes));
        // 分两种情况进行处理
//        if (UploadAbility.isMultipart(request)) {
//            // 添加 Multipart 请求参数列表
//            paramList.addAll(UploadAbility.createMultipartParamList(request));
//        } else {
//            // 添加普通请求参数列表（包括 Query String 与 Form Data）
//            Map<String, Object> requestParamMap = WebUtil.getRequestParamMap(request);
//            if (MapUtil.isNotEmpty(requestParamMap)) {
//                paramList.add(new Params(requestParamMap));
//            }
//        }
        
        // 添加普通请求参数列表（包括 Query String 与 Form Data）
//        Map<String, Object> requestParamMap = WebUtil.getRequestParamMap(request);
//        if (MapUtil.isNotEmpty(requestParamMap)) {
//            paramList.add(new Params(requestParamMap));
//        }
        
        Map<String, Object> requestParamMap = WebUtil.getRequestParamMap(request);
        GarlictsContext.Request.put("params", requestParamMap);
        
        // 返回参数列表
        return paramList;
    }

    private List<Object> createPathParamList(Matcher requestPathMatcher, Class<?>[] controllerParamTypes) {
        // 定义参数列表
        List<Object> paramList = new ArrayList<Object>();
        // 遍历正则表达式中所匹配的组
        for (int i = 1; i <= requestPathMatcher.groupCount(); i++) {
            // 获取请求参数
            String param = requestPathMatcher.group(i);
            // 获取参数类型（支持四种类型：int/Integer、long/Long、double/Double、String）
            Class<?> paramType = controllerParamTypes[i - 1];
            if (ClassUtil.isInt(paramType)) {
                paramList.add(ConvertUtil.convertToInt(param));
            } else if (ClassUtil.isLong(paramType)) {
                paramList.add(ConvertUtil.convertToLong(param));
            } else if (ClassUtil.isDouble(paramType)) {
                paramList.add(ConvertUtil.convertToDouble(param));
            } else if (ClassUtil.isString(paramType)) {
                paramList.add(param);
            }
        }
        // 返回参数列表
        return paramList;
    }

    private Object invokeActionMethod(Method actionMethod, Object actionInstance, List<Object> actionMethodParamList) throws IllegalAccessException, InvocationTargetException {
        // 通过反射调用 Action 方法
        actionMethod.setAccessible(true); // 取消类型安全检测（可提高反射性能）
        return actionMethod.invoke(actionInstance, actionMethodParamList.toArray());
    }

//    private void checkParamList(Method actionMethod, List<Object> actionMethodParamList) {
//        // 判断 Action 方法参数的个数是否匹配
//        Class<?>[] actionMethodParameterTypes = actionMethod.getParameterTypes();
//        if (actionMethodParameterTypes.length != actionMethodParamList.size()) {
//            String message = String.format("因为参数个数不匹配，所以无法调用 Action 方法！原始参数个数：%d，实际参数个数：%d", actionMethodParameterTypes.length, actionMethodParamList.size());
//            throw new RuntimeException(message);
//        }
//    }
    
}
