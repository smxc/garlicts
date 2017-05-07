package com.garlicts.framework.mvc.bean;

/**
 * JSP模型视图
 *
 * @author 水木星辰
 * @since 1.0
 */
public class JspView implements View {
	
	private String view;          // 视图路径
    private Object model; // 相关数据
    
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

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}
    
}
