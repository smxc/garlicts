package com.garlicts.framework.distributed.upload.ftp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.garlicts.framework.ioc.BeanContainerComponent;
import com.garlicts.framework.util.ClassUtil;
import com.garlicts.framework.util.JsonUtil;

@WebServlet(name="ftpUploadServlet",urlPatterns="/ftpUploadServlet", loadOnStartup=2)
//使用注解@MultipartConfig将一个Servlet标识为支持文件上传
@MultipartConfig
public class FTPUploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = -3402583385900605439L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
			Map<String, String> ret = null;
			FTPUpload ftpUpload = null;
			
            //获取所有的文件上传域
            Collection<Part> parts = request.getParts();
            
            if(parts.size() > 0){
            	ftpUpload = FTPUpload.getInstance();
            }
            
            //上传单个文件
            if (parts.size()==1) {
                
            	//根据名称来获取文件上传域
                Part part = request.getPart("file");
                
                //Servlet3没有提供直接获取文件名的方法,需要从请求头中解析出来
                //获取请求头，请求头的格式：form-data; name="file"; filename="1.jpg"
                String header = part.getHeader("content-disposition");
                //获取文件名
                String filename = generateFilename(header);
                
                try {
                	
					ret = ftpUpload.uploadFile(filename, part.getInputStream());
				} catch (Exception e) {
					e.printStackTrace();
				}
                
            }else {
                //一次性上传多个文件
                for (Part part : parts) {//循环处理上传的文件
                	
                    //获取请求头，请求头的格式：form-data; name="file"; filename="1.jpg"
                    String header = part.getHeader("content-disposition");
                    //获取文件名
                    String filename = generateFilename(header);
                    
                    try {
						ret = ftpUpload.uploadFile(filename, part.getInputStream());
					} catch (Exception e) {
						e.printStackTrace();
					}
                    
                }
            }
            
            JsonUtil.toJson(ret);
    }
	
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }
	
		
	/**
     * 根据请求头解析出文件名
     * 请求头的格式：火狐和google浏览器下：form-data; name="file"; filename="1.zip"
     *                 IE浏览器下：form-data; name="file"; filename="E:\1.zip"
     * @param header 请求头
     * @return 文件名
     */
    public String generateFilename(String header) {
    	String suffix = header.substring(header.lastIndexOf("."), header.lastIndexOf("\""));
    	String filename = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
    	return filename;
    }
    
    public String generateFolderName() {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    	String date = simpleDateFormat.format(new Date());
    	return date;
    }    
    
}
