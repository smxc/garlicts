package com.garlicts.framework.plugin.upload.ftp;

/**
 * FTP常量
 */
public class FtpConstant {
	
	private String host; //FTP主机ip
	private Integer port; //FTP端口：默认21
	private String username; //FTP用户名
	private String password; //FTP密码
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
