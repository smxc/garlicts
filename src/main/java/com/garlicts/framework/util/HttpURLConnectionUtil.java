package com.garlicts.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * HttpURLConnection http连接
 * @author 水木星辰
 */
public class HttpURLConnectionUtil {
	
    public static String doPost(String url, String jsonStr){
        
    	StringBuffer responseStr = new StringBuffer();
    	OutputStreamWriter out = null;
    	HttpURLConnection conn = null;
    	
    	try {
			// 创建url资源
    		URL requestUrl = new URL(url);
			// 建立http连接
    		conn = (HttpURLConnection) requestUrl.openConnection();
    		
    		conn.setConnectTimeout(30000);
    		conn.setReadTimeout(30000);
    		
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// Post请求不能使用缓存 
			conn.setUseCaches(false);
			
			// 设置请求方法
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			
//			byte[] data = jsonStr.getBytes("UTF-8");
			conn.setRequestProperty("Content-Length", String.valueOf(jsonStr.length()));
			
            // 设定传送的内容类型是可序列化的java对象  
            // httpUrlConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");  
			conn.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			conn.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			
			//请求
			conn.connect();
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.append(jsonStr);
			out.flush();
			
			if(conn.getResponseCode() == 200) {
				
				//相应数据
				InputStream inputStream = conn.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				String str = "";
				while((str = bufferedReader.readLine()) != null){
					responseStr.append(str);
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if(null != out){
					out.close();
				}
				if(null != conn){
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
    	
    	return responseStr.toString();
    	
    }

    
}
