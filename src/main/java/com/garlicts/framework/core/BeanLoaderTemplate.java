package com.garlicts.framework.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garlicts.framework.ioc.BeanContainerComponent;
import com.garlicts.framework.util.ClassUtil;
import com.garlicts.framework.util.StringUtil;

/**
 * 加载Bean的模板类
 * <br/>
 *
 * @author 水木星辰
 * @since 1.0
 */
public class BeanLoaderTemplate{

    private static final Logger logger = LoggerFactory.getLogger(BeanLoaderTemplate.class);

    /**
     * 获取指定package包下的所有class 
     */
    //bug
    public final List<Class<?>> getBeanClassList(String packageName) {
        	
    		// 获取框架下所有的类
//            List<Class<?>> garlictsClassList = getBeanClassListIterate("com.garlicts.framework");
            // 获取插件包路径下所有的类
//            List<Class<?>> pluginClassList = getBeanClassListIterate("com.garlicts.framework.plugin");
//            if(pluginClassList.size() > 0){
//            	garlictsClassList.addAll(pluginClassList);
//            }
            
            List<Class<?>> customerClassList = null;
            
            if(StringUtil.isNotBlank(packageName)){
            	
//            	if(!packageName.contains("com.galicts")){
//            		
//            		customerClassList = getBeanClassListIterate(packageName);
//            		
//            	}else{
//            		throw new RuntimeException("自定义工程的包名与框架包名发生重名");
//            	}
            	
            	customerClassList = getBeanClassListIterate(packageName);
            	
            }

//            return garlictsClassList;
            return customerClassList;
    }

    public List<Class<?>> getBeanClassListIterate(String packageName) {
    	
    	List<Class<?>> classList = new ArrayList<Class<?>>();
    	Enumeration<URL> urls = null;
    	
    	try {
			urls = ClassUtil.getClassLoader().getResources(packageName.replace(".", "/"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
        // 遍历 URL 资源
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url != null) {
                // 获取协议名（分为 file 与 jar）
                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    // 若在 class 目录中，则执行添加类操作
                    String packagePath = url.getPath();
                    addClass(classList, packagePath, packageName);
                } else if (protocol.equals("jar")) {

					try {
	                    // 若在 jar 包中，则解析 jar 包中的 entry
	                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
	                    JarFile jarFile = jarURLConnection.getJarFile();
						
	                    Enumeration<JarEntry> jarEntries = jarFile.entries();
	                    while (jarEntries.hasMoreElements()) {
	                        JarEntry jarEntry = jarEntries.nextElement();
	                        String jarEntryName = jarEntry.getName();
	                        // 判断该 entry 是否为 class
	                        if (jarEntryName.endsWith(".class")) {
	                            // 获取类名
	                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
	                            // 执行添加类操作
	                            doAddClass(classList, className);
	                        }
	                    }						
						
					} catch (IOException e) {
						e.printStackTrace();
					}

                }
            }
        }    	
    	
        return classList;
        
    }
    
    
    private void addClass(List<Class<?>> classList, String packagePath, String packageName) {
        try {
            // 获取包名路径下的 class 文件或目录
            File[] files = new File(packagePath).listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
                }
            });
            
            String subPackagePath = null; // 子包路径 /com/garlicts/
            String subPackageName = null; // 子包名 com.garlicts.framework
            String fileName;
            // 遍历文件或目录
            for (File file : files) {
            	fileName = file.getName();
                // 判断是否为文件或目录
                if (file.isFile()) {
                    // 获取类名
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    if (StringUtil.isNotEmpty(packageName)) {
                        className = packageName + "." + className;
                    }
                    // 执行添加类操作
                    doAddClass(classList, className);
                } else {
                    // 获取子包
                    if (StringUtil.isNotEmpty(packagePath)) {
                    	if(packagePath.endsWith("/")){
                    		subPackagePath = packagePath + fileName;
                    	}else{
                    		subPackagePath = packagePath + "/" + fileName;
                    	}
                    }
                    // 子包名
                    if (StringUtil.isNotEmpty(packageName)) {
                        subPackageName = packageName + "." + fileName;
                    }
                    // 递归调用
                    addClass(classList, subPackagePath, subPackageName);
                }
            }
        } catch (Exception e) {
            logger.error("添加类出错！");
        }
    }

    private void doAddClass(List<Class<?>> classList, String className) {
        // 加载类
        Class<?> cls = ClassUtil.loadClass(className, false);
        
        if(!classList.contains(cls)){
        	classList.add(cls);
        }
        
        
    }


	/**
	 * 获取包名下带有某注解的所有类 
	 */
	public List<Class<?>> getBeanClassListByAnnotation(Class<? extends Annotation> annotationClass) {
		
		List<Class<?>> classList = new ArrayList<Class<?>>();
		Map<Class<?>,Object> beanMap = BeanContainerComponent.getBeanMap();
		
        for (Class<?> cls : beanMap.keySet()) {
            if (cls.isAnnotationPresent(annotationClass)) {
            	classList.add(cls);
            }
        }
        return classList;

	}

    /**
     * 获取包名下某父类（或接口）的所有子类（或实现类）
     */
	public List<Class<?>> getBeanClassListBySuper(Class<?> superClass) {

        List<Class<?>> classList = new ArrayList<Class<?>>();
        Map<Class<?>, Object> beanMap = BeanContainerComponent.getBeanMap();
        
        for(Class<?> cls : beanMap.keySet()){
        	
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
            	classList.add(cls);
            }
            
        }
        
        return classList;		
		
	}     
    
    
}