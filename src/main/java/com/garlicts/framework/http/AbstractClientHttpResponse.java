package com.garlicts.framework.http;

import java.io.IOException;

/**
 * Abstract base for {@link ClientHttpResponse}.
 *
 * @author Arjen Poutsma
 * @since 3.1.1
 */
public abstract class AbstractClientHttpResponse implements ClientHttpResponse {

	public HttpStatus getStatusCode() throws IOException {
		return HttpStatus.valueOf(getRawStatusCode());
	}

}
