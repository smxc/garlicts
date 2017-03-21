package com.garlicts.framework.core.fault;

/**
 * 初始化错误
 *
 * @author 水木星辰
 * @since 1.0
 */
public class InitializationError extends Error {

	private static final long serialVersionUID = 2042636879102851189L;

	public InitializationError() {
        super();
    }

    public InitializationError(String message) {
        super(message);
    }

    public InitializationError(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationError(Throwable cause) {
        super(cause);
    }
}
