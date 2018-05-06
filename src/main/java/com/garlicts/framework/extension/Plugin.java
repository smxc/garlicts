package com.garlicts.framework.extension;

/**
 * 插件接口
 *
 * @author 水木星辰
 * @since 1.0
 */
public interface Plugin {

    /**
     * 初始化插件
     */
    void init();
    
    /**
     * 销毁插件
     */
    void destroy();
    
}
