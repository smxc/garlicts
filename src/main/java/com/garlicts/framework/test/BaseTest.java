package com.garlicts.framework.test;

import org.junit.runner.RunWith;

import com.garlicts.framework.ComponentLoader;

@RunWith(OrderedRunner.class)
public abstract class BaseTest {
	
	protected BaseTest(){
		ComponentLoader.init();
	}
	
}
