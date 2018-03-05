package com.garlicts.framework.http.converter;

import com.garlicts.framework.exception.NestedRuntimeException;


/**
 * Thrown by {@link HttpMessageConverter} implementations when a conversion attempt fails.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
@SuppressWarnings("serial")
public class HttpMessageConversionException extends NestedRuntimeException {

	/**
	 * Create a new HttpMessageConversionException.
	 * @param msg the detail message
	 */
	public HttpMessageConversionException(String msg) {
		super(msg);
	}

	/**
	 * Create a new HttpMessageConversionException.
	 * @param msg the detail message
	 * @param cause the root cause (if any)
	 */
	public HttpMessageConversionException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
