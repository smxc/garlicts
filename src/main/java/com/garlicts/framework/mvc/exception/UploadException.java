package com.garlicts.framework.mvc.exception;

/**
 * 上传异常（当文件上传失败时抛出）
 *
 * @author 水木星辰
 * @since 1.0
 */
public class UploadException extends RuntimeException {

	private static final long serialVersionUID = -6371678940928022913L;

	public UploadException() {
        super();
    }

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadException(Throwable cause) {
        super(cause);
    }
}
