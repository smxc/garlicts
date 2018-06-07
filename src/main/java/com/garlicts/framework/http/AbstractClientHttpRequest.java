package com.garlicts.framework.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import com.garlicts.framework.util.Assert;
import com.garlicts.framework.util.StringUtil;


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

	public final ClientHttpResponse execute() {
		checkExecuted();
		ClientHttpResponse result = executeInternal(this.headers);
		this.executed = true;
		return result;
	}
	
	public final ClientHttpResponse executeJson() {
		
		checkExecuted();
		
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "application/json");
		
		ClientHttpResponse result = null;
		try {
			result = executeInternal(this.headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	protected abstract OutputStream getBodyInternal(HttpHeaders headers);

	/**
	 * Abstract template method that writes the given headers and content to the HTTP request.
	 * @param headers the HTTP headers
	 * @return the response object for the executed request
	 */
	protected abstract ClientHttpResponse executeInternal(HttpHeaders headers);

	void addHeaders(HttpURLConnection connection, HttpHeaders headers) {
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			String headerName = entry.getKey();
			if ("Cookie".equalsIgnoreCase(headerName)) {  // RFC 6265
				String headerValue = StringUtil.collectionToDelimitedString(entry.getValue(), "; ");
				connection.setRequestProperty(headerName, headerValue);
			}
			else {
				for (String headerValue : entry.getValue()) {
					connection.addRequestProperty(headerName, headerValue);
				}
			}
		}
	}	
	
}
