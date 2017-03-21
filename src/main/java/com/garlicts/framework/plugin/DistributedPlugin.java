package com.garlicts.framework.plugin;

public abstract class DistributedPlugin implements Plugin {

	@Override
	public abstract void init();

	@Override
	public abstract void destroy();

//	@Override
//	public abstract void register();

}
