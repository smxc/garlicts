package com.garlicts.framework.mvc;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.InstanceFactory;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.util.WebUtil;

/**
 * 前端控制器
 *
 * @author 水木星辰
 * @since 1.0
 */
@WebServlet(urlPatterns={"*.do", "/services/"}, loadOnStartup=0)
public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 4955762961302934566L;
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping handlerMapping = InstanceFactory.getHandlerMapping();
    private HandlerInvoker handlerInvoker = InstanceFactory.getHandlerInvoker();
//    private HandlerExceptionResolver handlerExceptionResolver = InstanceFactory.getHandlerExceptionResolver();
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化相关配置
//        ServletContext servletContext = config.getServletContext();
//        UploadAbility.init(servletContext);
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求编码方式
//        request.setCharacterEncoding(FrameworkConstant.UTF_8);
        // 获取当前请求相关数据
        String currentRequestMethod = request.getMethod();
        String currentRequestPath = WebUtil.getRequestPath(request);
        
        if(currentRequestPath.endsWith(".do")){
        	currentRequestPath = currentRequestPath.substring(0, currentRequestPath.lastIndexOf(".do"));
        }
        
        if(currentRequestPath.contains("/services")){
        	currentRequestPath = currentRequestPath.replaceAll("/services", "");
        }
        
        // 将“/”请求重定向到首页
        if (currentRequestPath.equals("/")) {
            WebUtil.redirectRequest(PropertiesProvider.getString(FrameworkConstant.HOME_PAGE), request, response);
            return;
        }
        // 去掉当前请求路径末尾的“/”
        if (currentRequestPath.endsWith("/")) {
            currentRequestPath = currentRequestPath.substring(0, currentRequestPath.length() - 1);
        }
        // 获取 Handler
        Handler handler = handlerMapping.getHandler(currentRequestMethod, currentRequestPath);
        // 若未找到 Action，则跳转到 404 页面
        if (handler == null) {
            WebUtil.sendError(HttpServletResponse.SC_NOT_FOUND, "", response);
            return;
        }
        // 初始化 GarlictsContext
        GarlictsContext.init(request, response);
        
        try {
        	
            // 调用 Handler
            handlerInvoker.invokeHandler(request, response, handler);
        } catch (Exception e) {
            // 处理 Action 异常
//            handlerExceptionResolver.resolveHandlerException(request, response,e);
        	e.printStackTrace();
        } finally {
            // 销毁 GarlictsContext
            GarlictsContext.destroy();
        }
    }
    
    
    
}
