package com.garlicts.framework.mvc.bean;

/**
 * JSON模型视图
 *
 * @author 水木星辰
 * @since 1.0
 */
public class JsonView implements View {

    private Object model; // 相关数据

    public JsonView() {
		super();
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}
    
}
