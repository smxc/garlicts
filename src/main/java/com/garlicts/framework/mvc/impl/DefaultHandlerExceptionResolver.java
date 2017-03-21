//package com.garlicts.framework.mvc.impl;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.garlicts.framework.FrameworkConstant;
//import com.garlicts.framework.mvc.exception.AuthcException;
//import com.garlicts.framework.mvc.exception.AuthzException;
//import com.garlicts.framework.util.WebUtil;
//import com.garlicts.framework.mvc.HandlerExceptionResolver;
//
///**
// * 默认 Handler 异常解析器
// *
// * @author 水木星辰
// * @since 1.0
// */
//public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver {
//
//    private static final Logger logger = LoggerFactory.getLogger(DefaultHandlerExceptionResolver.class);
//
//    @Override
//    public void resolveHandlerException(HttpServletRequest request, HttpServletResponse response,Exception e) {
//        // 判断异常原因
//        Throwable cause = e.getCause();
//        if (cause == null) {
//            logger.error(e.getMessage());
//            return;
//        }
//        if (cause instanceof AuthcException) {
//            // 分两种情况进行处理
//            if (WebUtil.isAJAX(request)) {
//                // 跳转到 403 页面
//                WebUtil.sendError(HttpServletResponse.SC_FORBIDDEN, "", response);
//            } else {
//                // 重定向到首页
//                WebUtil.redirectRequest(FrameworkConstant.HOME_PAGE, request, response);
//            }
//        } else if (cause instanceof AuthzException) {
//            // 跳转到 403 页面
//            WebUtil.sendError(HttpServletResponse.SC_FORBIDDEN, "", response);
//        } else {
//            // 跳转到 500 页面
//            WebUtil.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, cause.getMessage(), response);
//        }
//    }
//}
