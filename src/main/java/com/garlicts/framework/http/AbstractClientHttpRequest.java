package com.garlicts.framework.http;

import java.io.IOException;
import java.io.OutputStream;

import com.garlicts.framework.util.Assert;


/**
 * Abstract base for {@link ClientHttpRequest} that makes sure that headers
 * and body are not written multiple times.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public abstract class AbstractClientHttpRequest implements ClientHttpRequest {

	private final HttpHeaders headers = new HttpHeaders();

	private boolean executed = false;


	public final HttpHeaders getHeaders() {
		return (this.executed ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers);
	}

	public final OutputStream getBody() throws IOException {
		checkExecuted();
		return getBodyInternal(this.headers);
	}

	public final ClientHttpResponse execute() throws IOException {
		checkExecuted();
		ClientHttpResponse result = executeInternal(this.headers);
		this.executed = true;
		return result;
	}
	
	public final ClientHttpResponse executeJson() throws IOException {
		
		checkExecuted();
		
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "application/json");
		
		ClientHttpResponse result = executeInternal(this.headers);
		
		this.executed = true;
		
		return result;
		
	}	

	private void checkExecuted() {
		Assert.state(!this.executed, "ClientHttpRequest already executed");
	}


	/**
	 * Abstract template method that returns the body.
	 * @param headers the HTTP headers
	 * @return the body output stream
	 */
	protected abstract OutputStream getBodyInternal(HttpHeaders headers) throws IOException;

	/**
	 * Abstract template method that writes the given headers and content to the HTTP request.
	 * @param headers the HTTP headers
	 * @return the response object for the executed request
	 */
	protected abstract ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException;

}
