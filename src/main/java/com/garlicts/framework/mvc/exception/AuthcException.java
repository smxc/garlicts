package com.garlicts.framework.mvc.exception;

/**
 * 认证异常（当非法访问时抛出）
 *
 * @author 水木星辰
 * @since 1.0
 */
public class AuthcException extends RuntimeException {

	private static final long serialVersionUID = -6816646565062025993L;

	public AuthcException() {
        super();
    }

    public AuthcException(String message) {
        super(message);
    }

    public AuthcException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthcException(Throwable cause) {
        super(cause);
    }
}
