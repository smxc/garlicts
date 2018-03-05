package com.garlicts.framework.util;

/**
 * Helper class for implementing exception classes which are capable of
 * holding nested exceptions. Necessary because we can't share a base
 * class among different exception types.
 *
 * <p>Mainly for use within the framework.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see NestedRuntimeException
 * @see NestedCheckedException
 * @see NestedIOException
 * @see org.springframework.web.util.NestedServletException
 */
public abstract class NestedExceptionUtils {

	/**
	 * Build a message for the given base message and root cause.
	 * @param message the base message
	 * @param cause the root cause
	 * @return the full exception message
	 */
	public static String buildMessage(String message, Throwable cause) {
		if (cause != null) {
			StringBuilder sb = new StringBuilder();
			if (message != null) {
				sb.append(message).append("; ");
			}
			sb.append("nested exception is ").append(cause);
			return sb.toString();
		}
		else {
			return message;
		}
	}

}
