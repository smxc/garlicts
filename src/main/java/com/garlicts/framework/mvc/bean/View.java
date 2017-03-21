package com.garlicts.framework.mvc.bean;

import com.garlicts.framework.core.bean.BaseBean;

/**
 * 封装返回数据
 *
 * @author 水木星辰
 * @since 1.0
 */
public class View extends BaseBean {

	private static final long serialVersionUID = -3531897340564908153L;
	
	private boolean success; // 成功标志
    private String message; // 信息
    
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
}
