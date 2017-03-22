package com.garlicts.framework;

/**
 * 初始化数据
 * 场景描述：项目启动时，将分类信息等字典数据查询出来，然后放入到缓存。
 * @since V1.0
 * @author 水木星辰
 */
public abstract class InitializeData {

	public abstract void init();
	
	public abstract void destroy();
	
}
