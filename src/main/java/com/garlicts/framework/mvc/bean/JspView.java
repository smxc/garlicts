package com.garlicts.framework.mvc.bean;

import java.util.Map;

/**
 * JSP模型视图
 *
 * @author 水木星辰
 * @since 1.0
 */
public class JspView extends View {

	private static final long serialVersionUID = 6127226807565589708L;
	
	private String view;          // 视图路径
    private Map<String, Object> model; // 相关数据
    
    public JspView() {
		super();
	}

	public JspView(String view) {
        this.view = view;
    }

    public boolean isRedirect() {
        return view.startsWith("/");
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
    
}
