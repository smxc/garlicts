package com.garlicts.framework.util;

//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
//import java.util.Random;

//import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
//import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.FrameworkConstant;

/**
 * Web 操作工具类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class WebUtil {

    private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

    /**
     * 将数据以 JSON 格式写入响应中
     */
    public static void writeJSON(HttpServletResponse response, Object data) {
        try {
            // 设置响应头
            response.setContentType("application/json"); // 指定内容类型为 JSON 格式
            response.setCharacterEncoding(FrameworkConstant.UTF_8); // 防止中文乱码
            // 向响应中写入数据
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtil.toJson(data)); // 转为 JSON 字符串
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("在响应中写数据出错！");
            throw new RuntimeException(e);
        }
    }

    /**
     * 将数据以 HTML 格式写入响应中（在 JS 中获取的是 JSON 字符串，而不是 JSON 对象）
     */
    public static void writeHTML(HttpServletResponse response, Object data) {
        try {
            // 设置响应头
            response.setContentType("text/html"); // 指定内容类型为 HTML 格式
            response.setCharacterEncoding(FrameworkConstant.UTF_8); // 防止中文乱码
            // 向响应中写入数据
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtil.toJson(data)); // 转为 JSON 字符串
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("在响应中写数据出错！");
            throw new RuntimeException(e);
        }
    }

    /**
     * 从请求中获取所有参数（当参数名重复时，用后者覆盖前者）
     */
    public static Map<String, Object> getRequestParamMap(HttpServletRequest request) {
        Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
        try {
            
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                if (checkParamName(paramName)) {
                    String[] paramValues = request.getParameterValues(paramName);
                    if (ArrayUtil.isNotEmpty(paramValues)) {
                        if (paramValues.length == 1) {
                            paramMap.put(paramName, paramValues[0]);
                        } else {
                            StringBuilder paramValue = new StringBuilder("");
                            for (int i = 0; i < paramValues.length; i++) {
                                paramValue.append(paramValues[i]);
                                if (i != paramValues.length - 1) {
                                    paramValue.append(",");
                                }
                            }
                            paramMap.put(paramName, paramValue.toString());
                        }
                    }
                }
            }            
            
        } catch (Exception e) {
            logger.error("获取请求参数出错！");
            throw new RuntimeException(e);
        }
        return paramMap;
    }
    
    public static String getJsonParam(HttpServletRequest request){
    	
    	BufferedReader br = null;
    	StringBuffer stringBuffer = new StringBuffer("");  
    	
		try {
			br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "UTF-8"));
	        
	        String temp;  
	        while ((temp = br.readLine()) != null) {  
	        	stringBuffer.append(temp);  
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(br != null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return stringBuffer.toString();
		
    }
    

    private static boolean checkParamName(String paramName) {
        return !paramName.equals("_"); // 忽略 jQuery 缓存参数
    }

    /**
     * 转发请求
     */
    public static void forwardRequest(String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (Exception e) {
            logger.error("转发请求出错！");
            throw new RuntimeException(e);
        }
    }

    /**
     * 重定向请求
     */
    public static void redirectRequest(String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + path);
        } catch (Exception e) {
            logger.error("重定向请求出错！");
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送错误代码
     */
    public static void sendError(int code, String message, HttpServletResponse response) {
        try {
            response.sendError(code, message);
        } catch (Exception e) {
            logger.error("发送错误代码出错！");
            throw new RuntimeException(e);
        }
    }

//    /**
//     * 判断是否为 AJAX 请求
//     */
//    public static boolean isAJAX(HttpServletRequest request) {
//        return request.getHeader("X-Requested-With") != null;
//    }

    /**
     * 获取请求路径
     */
    public static String getRequestPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pathInfo = StringUtil.defaultIfEmpty(request.getPathInfo(), "");
        return servletPath + pathInfo;
    }

    /**
     * 从 Cookie 中获取数据
     */
    public static String getCookie(HttpServletRequest request, String name) {
        String value = "";
        try {
            Cookie[] cookieArray = request.getCookies();
            if (cookieArray != null) {
                for (Cookie cookie : cookieArray) {
                    if (StringUtil.isNotEmpty(name) && name.equals(cookie.getName())) {
                        value = CodecUtil.decodeURL(cookie.getValue());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取 Cookie 出错！");
            throw new RuntimeException(e);
        }
        return value;
    }

//    /**
//     * 下载文件
//     */
//    public static void downloadFile(HttpServletResponse response, String filePath) {
//        try {
//            String originalFileName = FilenameUtils.getName(filePath);
//            String downloadedFileName = new String(originalFileName.getBytes("GBK"), "ISO8859_1"); // 防止中文乱码
//
//            response.setContentType("application/octet-stream");
//            response.addHeader("Content-Disposition", "attachment;filename=\"" + downloadedFileName + "\"");
//
//            InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
//            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
//            StreamUtil.copyStream(inputStream, outputStream);
//        } catch (Exception e) {
//            logger.error("下载文件出错！");
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * 获取客户端IP 
     */
	public static String getClientIP(HttpServletRequest request) {
		
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		String[] arr = ip.split(",");
		if (arr.length > 1) {
			return arr[0];
		} else {
			return ip;
		}

	}
    
}
