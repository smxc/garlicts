package com.garlicts.framework.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.garlicts.framework.util.StreamUtil;
import com.garlicts.framework.util.StringUtil;

/**
 * {@link ClientHttpResponse} implementation that uses standard JDK facilities.
 * Obtained via {@link SimpleBufferingClientHttpRequest#execute()} and
 * {@link GarlictsClientHttpRequest#execute()}.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
final class GarlictsClientHttpResponse extends AbstractClientHttpResponse {

	private final HttpURLConnection connection;

	private HttpHeaders headers;


	GarlictsClientHttpResponse(HttpURLConnection connection) {
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

	public InputStream getBody() {
		
		InputStream inputStream = null;
		
		try {
			inputStream = this.connection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return inputStream;
		
	}
	
	public String getBodyAsString() {
		
		InputStream inputStream = null;
		String string = null;
		try {
			inputStream = this.connection.getInputStream();
			string = StreamUtil.getString(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return string;
		
	}

	public void close() {
		this.connection.disconnect();
	}

}
