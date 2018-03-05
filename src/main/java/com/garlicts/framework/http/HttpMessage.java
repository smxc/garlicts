package com.garlicts.framework.http;

/**
 * Represents the base interface for HTTP request and response messages.
 * Consists of {@link HttpHeaders}, retrievable via {@link #getHeaders()}.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public interface HttpMessage {

	/**
	 * Return the headers of this message.
	 * @return a corresponding HttpHeaders object (never {@code null})
	 */
	HttpHeaders getHeaders();

}
