package com.garlicts.mvc.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.garlicts.FrameworkConstant;
import com.garlicts.config.PropertiesProvider;
import com.garlicts.mvc.ViewResolver;
import com.garlicts.mvc.bean.JsonView;
import com.garlicts.mvc.bean.JspView;
import com.garlicts.util.MapUtil;
import com.garlicts.util.StringUtil;
import com.garlicts.util.WebUtil;

/**
 * 默认视图解析器
 *
 * @author 水木星辰
 * @since 1.0
 */
public class DefaultViewResolver implements ViewResolver {

    @Override
    public void resolveView(HttpServletRequest request, HttpServletResponse response, Object controllerMethodResult) {
        if (controllerMethodResult != null) {
            // Controller 返回值可为 View 或 Result
            if (controllerMethodResult instanceof JspView) {
                // 若为 View，则需考虑两种视图类型（重定向 或 转发）
                JspView jspView = (JspView) controllerMethodResult;
                if(StringUtil.isNotEmpty(jspView.getView())){
                	
                    if (jspView.isRedirect()) {
                        // 获取路径
                        String view = jspView.getView();
                        // 重定向请求
                        WebUtil.redirectRequest(view, request, response);
                    } else {
                        // 获取路径
                        String view = PropertiesProvider.getString(FrameworkConstant.JSP_PATH) + jspView.getView();
                        // 初始化请求属性
                        Map<String, Object> model = jspView.getModel();
                        if (MapUtil.isNotEmpty(model)) {
                            for (Map.Entry<String, Object> entry : model.entrySet()) {
                                request.setAttribute(entry.getKey(),entry.getValue());
                            }
                        }
                        System.out.println("view " + view);
                        // 转发请求
                        WebUtil.forwardRequest(view, request, response);
                    }                	
                	
                }

            } 
            else if(controllerMethodResult instanceof JsonView) {
                // 若为 Result，则需考虑两种请求类型（文件上传 或 普通请求）
            	JsonView jsonView = (JsonView) controllerMethodResult;
//                if (UploadAbility.isMultipart(request)) { //ddd
//                    // 对于 multipart 类型，说明是文件上传，需要转换为 HTML 格式并写入响应中
//                    WebUtil.writeHTML(response, jsonView.getModel());
//                } else {
//                    // 对于其它类型，统一转换为 JSON 格式并写入响应中
//                    WebUtil.writeJSON(response, jsonView.getModel());
//                }
                
                // 对于其它类型，统一转换为 JSON 格式并写入响应中
                WebUtil.writeJSON(response, jsonView.getModel());
                
            }
            else{
            	JsonView jsonView = (JsonView) controllerMethodResult;
            	WebUtil.writeJSON(response, jsonView);
            }
            
        }
    }
}
