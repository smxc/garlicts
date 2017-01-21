package com.garlicts.mvc.bean;

import java.util.Map;

/**
 * JSON模型视图
 *
 * @author 水木星辰
 * @since 1.0
 */
public class JsonView extends View {

	private static final long serialVersionUID = -4016062564133736642L;
	
    private Map<String, Object> model; // 相关数据

    public JsonView() {
		super();
	}

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
    
}
