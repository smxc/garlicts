package com.garlicts.framework.exception.http;

import com.garlicts.framework.http.converter.HttpMessageConversionException;

/**
 * Thrown by {@link HttpMessageConverter} implementations when the
 * {@link HttpMessageConverter#write} method fails.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
@SuppressWarnings("serial")
public class HttpMessageNotWritableException extends HttpMessageConversionException {

	/**
	 * Create a new HttpMessageNotWritableException.
	 * @param msg the detail message
	 */
	public HttpMessageNotWritableException(String msg) {
		super(msg);
	}

	/**
	 * Create a new HttpMessageNotWritableException.
	 * @param msg the detail message
	 * @param cause the root cause (if any)
	 */
	public HttpMessageNotWritableException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
