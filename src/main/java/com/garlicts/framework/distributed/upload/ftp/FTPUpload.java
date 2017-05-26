package com.garlicts.framework.distributed.upload.ftp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.config.PropertiesProvider;

public class FTPUpload {
	
	FTPConstant ftpConstant;

	public FTPUpload() {
		
		String ftpHost = PropertiesProvider.getString(FrameworkConstant.FTP_HOST);
		String ftpPort = PropertiesProvider.getString(FrameworkConstant.FTP_PORT, "21");
		String ftpUsername = PropertiesProvider.getString(FrameworkConstant.FTP_USERNAME);
		String ftpPassword = PropertiesProvider.getString(FrameworkConstant.FTP_PASSWORD);
		ftpConstant = new FTPConstant();
		ftpConstant.setHost(ftpHost);
		ftpConstant.setPort(Integer.parseInt(ftpPort));
		ftpConstant.setUsername(ftpUsername);
		ftpConstant.setPassword(ftpPassword);
		
	}
	
	public static FTPUpload getInstance(){
		return new FTPUpload();
	}

	private FTPClient ftpClient = null;

	/**
	 * 连接FTP服务器
	 * 
	 * @author wangwei
	 */
	public FTPClient connectFTPServer() throws Exception {

		ftpClient = new FTPClient();
		try {
			ftpClient.configure(getFTPClientConfig());
			ftpClient.connect(ftpConstant.getHost(), ftpConstant.getPort());
			ftpClient.login(ftpConstant.getUsername(), ftpConstant.getPassword());

			// 设置以二进制方式传输
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 设置被动模式
			ftpClient.enterLocalPassiveMode();
			ftpClient.setControlEncoding("GBK");

			// 响应信息
			int replyCode = ftpClient.getReplyCode();
			if ((!FTPReply.isPositiveCompletion(replyCode))) {
				// 关闭Ftp连接
				closeFTPClient();
				// 释放空间
				ftpClient = null;
				throw new Exception("登录FTP服务器失败,请检查!");
			} else {
				return ftpClient;
			}
		} catch (Exception e) {
			ftpClient.disconnect();
			ftpClient = null;
			throw e;
		}
	}

	
	/**
	 * 配置FTP连接参数
	 * 
	 * @author wangwei
	 */
	public FTPClientConfig getFTPClientConfig() throws Exception {

		String systemKey = FTPClientConfig.SYST_UNIX;
		String serverLanguageCode = "zh";
		FTPClientConfig conf = new FTPClientConfig(systemKey);
		conf.setServerLanguageCode(serverLanguageCode);
		conf.setDefaultDateFormatStr("yyyy-MM-dd");

		return conf;
	}

	
	/**
	 * 上传文件到指定的FTP路径下
	 * 
	 * @author wangwei
	 */
	public Map<String,String> uploadFile(String newFilename, InputStream inputStream) throws Exception {
		
		Map<String,String> rs = new HashMap<String,String>();
		// 连接FTP服务器
		ftpClient = this.connectFTPServer();

		String remoteFolder = generateFolderName();
		
		try {

			// 改变当前路径到指定路径
			this.changeDirectory(remoteFolder);
			boolean result = ftpClient.storeFile(newFilename, inputStream);
			if (!result) {
				rs.put("ret", "fail");
				throw new Exception("FTP文件上传失败!");
			}else{
				rs.put("ret", "success");
				// 文件上传的远程目录
				rs.put("url", new StringBuffer(remoteFolder).append("/").append(newFilename).toString());
			}

		} catch (Exception e) {
			throw e;
		}
		
		return rs;
		
	}	

	
	/**
	 * FTP下载
	 * 
	 * @author wangwei
	 */
	public void downloadFile(String downloadFilename, String remoteFolder,
			OutputStream outputStream, HttpServletResponse response)
			throws Exception {

		// 连接FTP服务器
		FTPClient ftp = this.connectFTPServer();

		try {

			this.changeDirectory(remoteFolder);
			FTPFile[] fs = ftp.listFiles();
			for (int i = 0; i < fs.length; i++) {
				FTPFile ff = fs[i];
				if (ff.getName().equals(downloadFilename)) {
					response.setHeader(
							"Content-disposition",
							"attachment;filename="
									+ URLEncoder.encode(downloadFilename,
											"utf-8"));
					boolean result = ftp.retrieveFile(new String(ff.getName()
							.getBytes("GBK"), "ISO-8859-1"), outputStream);
					if (!result) {
						throw new Exception("FTP文件下载失败!");
					}
					outputStream.flush();
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}

	}


	/**
	 * 改变工作目录，如失败则创建文件夹
	 * 
	 * @author wangwei
	 */
	public void changeDirectory(String remoteFolder) throws Exception {

		if (remoteFolder != null) {
			boolean flag = ftpClient.changeWorkingDirectory(remoteFolder);
			if (!flag) {
				ftpClient.makeDirectory(remoteFolder);
				ftpClient.changeWorkingDirectory(remoteFolder);
			}
		}

	}

	/**
	 * 删除文件
	 * 
	 * @author wangwei
	 */
	public Map<String,String> deleteFile(String filename, String remoteFolder)
			throws Exception {

		Map<String,String> rs = new HashMap<String, String>();
		// 连接FTP服务器
		FTPClient ftp = this.connectFTPServer();

		try {

			// 改变当前路径到指定路径
			this.changeDirectory(remoteFolder);
			boolean result = ftp.deleteFile(filename);
			if (!result) {
				throw new Exception("FTP删除文件失败!");
			}else{
				rs.put("ret", "success");
			}

		} catch (Exception e) {
			throw e;
		}
		
		return rs;

	}

	/**
	 * 判断ftp文件是否存在
	 * 
	 * @author wangwei
	 */
	public boolean isFtpFile(String filename, String remoteFolder)
			throws Exception {
		boolean result = false;
		// 连接FTP服务器
		ftpClient = this.connectFTPServer();

		try {

			// 改变当前路径到指定路径
			this.changeDirectory(remoteFolder);
			InputStream is = ftpClient.retrieveFileStream(filename);
			if (null != is) {
				// 存在
				result = true;
			}

		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 关闭FTP连接
	 * 
	 * @author wangwei
	 */
	public void closeFTPClient(FTPClient ftp) throws Exception {

		try {
			if (ftp.isConnected())
				ftp.disconnect();
		} catch (Exception e) {
			throw new Exception("关闭FTP服务出错!");
		}
	}

	/**
	 * 关闭FTP连接
	 * 
	 * @author wangwei
	 */
	public void closeFTPClient() throws Exception {

		this.closeFTPClient(this.ftpClient);

	}

	/**
	 * 读取FTP服务器上的文件 
	 */
    public InputStream retrieveFileStream(String filename, String remoteFolder) throws Exception{
    	
    	ftpClient = this.connectFTPServer();
    	
		this.changeDirectory(remoteFolder);
		InputStream is = ftpClient.retrieveFileStream(filename);    	
    	
		return is;
		
    }
    
    public String generateFolderName() {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    	String date = simpleDateFormat.format(new Date());
    	return date;
    } 
    
}
