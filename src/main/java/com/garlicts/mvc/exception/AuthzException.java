package com.garlicts.mvc.exception;

/**
 * 授权异常（当权限无效时抛出）
 *
 * @author 水木星辰
 * @since 1.0
 */
public class AuthzException extends RuntimeException {

	private static final long serialVersionUID = 8757367803878391302L;

	public AuthzException() {
        super();
    }

    public AuthzException(String message) {
        super(message);
    }

    public AuthzException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthzException(Throwable cause) {
        super(cause);
    }
}
