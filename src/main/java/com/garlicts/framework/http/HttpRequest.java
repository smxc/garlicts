package com.garlicts.framework.http;

import java.net.URI;

/**
 * Represents an HTTP request message, consisting of {@linkplain #getMethod() method}
 * and {@linkplain #getURI() uri}.
 *
 * @author Arjen Poutsma
 * @since 3.1
 */
public interface HttpRequest extends HttpMessage {

	/**
	 * Return the HTTP method of the request.
	 * @return the HTTP method as an HttpMethod enum value
	 */
	HttpMethod getMethod();

	/**
	 * Return the URI of the request.
	 * @return the URI of the request
	 */
	URI getURI();

}
