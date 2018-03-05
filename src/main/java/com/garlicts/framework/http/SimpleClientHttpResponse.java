package com.garlicts.framework.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.garlicts.framework.util.StringUtil;

/**
 * {@link ClientHttpResponse} implementation that uses standard JDK facilities.
 * Obtained via {@link SimpleBufferingClientHttpRequest#execute()} and
 * {@link SimpleStreamingClientHttpRequest#execute()}.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
final class SimpleClientHttpResponse extends AbstractClientHttpResponse {

	private final HttpURLConnection connection;

	private HttpHeaders headers;


	SimpleClientHttpResponse(HttpURLConnection connection) {
		this.connection = connection;
	}


	public int getRawStatusCode() throws IOException {
		return this.connection.getResponseCode();
	}

	public String getStatusText() throws IOException {
		return this.connection.getResponseMessage();
	}

	public HttpHeaders getHeaders() {
		if (this.headers == null) {
			this.headers = new HttpHeaders();
			// Header field 0 is the status line for most HttpURLConnections, but not on GAE
			String name = this.connection.getHeaderFieldKey(0);
			if (StringUtil.hasLength(name)) {
				this.headers.add(name, this.connection.getHeaderField(0));
			}
			int i = 1;
			while (true) {
				name = this.connection.getHeaderFieldKey(i);
				if (!StringUtil.hasLength(name)) {
					break;
				}
				this.headers.add(name, this.connection.getHeaderField(i));
				i++;
			}
		}
		return this.headers;
	}

	public InputStream getBody() throws IOException {
		InputStream errorStream = this.connection.getErrorStream();
		return (errorStream != null ? errorStream : this.connection.getInputStream());
	}

	public void close() {
		this.connection.disconnect();
	}

}
