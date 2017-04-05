package com.garlicts.framework.plugin.upload.ftp;

import java.lang.reflect.Constructor;

import com.garlicts.framework.FrameworkConstant;
import com.garlicts.framework.config.PropertiesProvider;
import com.garlicts.framework.ioc.BeanContainerAbility;
import com.garlicts.framework.plugin.Plugin;
import com.garlicts.framework.util.ClassUtil;

public class FtpPlugin implements Plugin {

	FtpConstant ftpConstant;
	
	@Override
	public void init() {
		
		String ftpHost = PropertiesProvider.getString(FrameworkConstant.FTP_HOST);
		String ftpPort = PropertiesProvider.getString(FrameworkConstant.FTP_PORT, "21");
		String ftpUsername = PropertiesProvider.getString(FrameworkConstant.FTP_USERNAME);
		String ftpPassword = PropertiesProvider.getString(FrameworkConstant.FTP_PASSWORD);
		ftpConstant = new FtpConstant();
		ftpConstant.setHost(ftpHost);
		ftpConstant.setPort(Integer.parseInt(ftpPort));
		ftpConstant.setUsername(ftpUsername);
		ftpConstant.setPassword(ftpPassword);
		
		//将FtpTemplate注册到Bean容器
		Class<?> ftpTemplateClass = ClassUtil.loadClass("com.garlicts.framework.plugin.upload.ftp.FtpTemplate");
		try {
			Constructor<?> constructor = ftpTemplateClass.getConstructor(FtpConstant.class);
			FtpTemplate ftpTemplateInstance = (FtpTemplate) constructor.newInstance(ftpConstant);
			BeanContainerAbility.setBean(ftpTemplateClass, ftpTemplateInstance);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}

	@Override
	public void destroy() {
		
	}

}
