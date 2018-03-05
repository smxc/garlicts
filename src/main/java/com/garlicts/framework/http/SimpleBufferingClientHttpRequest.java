package com.garlicts.framework.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.garlicts.framework.util.FileCopyUtils;
import com.garlicts.framework.util.StringUtil;

/**
 * {@link ClientHttpRequest} implementation that uses standard JDK facilities to
 * execute buffered requests. Created via the {@link SimpleClientHttpRequestFactory}.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 3.0
 * @see SimpleClientHttpRequestFactory#createRequest(java.net.URI, HttpMethod)
 */
final class SimpleBufferingClientHttpRequest extends AbstractBufferingClientHttpRequest {

	private final HttpURLConnection connection;

	private final boolean outputStreaming;


	SimpleBufferingClientHttpRequest(HttpURLConnection connection, boolean outputStreaming) {
		this.connection = connection;
		this.outputStreaming = outputStreaming;
	}


	public HttpMethod getMethod() {
		return HttpMethod.valueOf(this.connection.getRequestMethod());
	}

	public URI getURI() {
		try {
			return this.connection.getURL().toURI();
		}
		catch (URISyntaxException ex) {
			throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
		}
	}

	@Override
	protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
		addHeaders(this.connection, headers);

		if (this.connection.getDoOutput() && this.outputStreaming) {
			this.connection.setFixedLengthStreamingMode(bufferedOutput.length);
		}
		this.connection.connect();
		if (this.connection.getDoOutput()) {
			FileCopyUtils.copy(bufferedOutput, this.connection.getOutputStream());
		}

		return new SimpleClientHttpResponse(this.connection);
	}


	/**
	 * Add the given headers to the given HTTP connection.
	 * @param connection the connection to add the headers to
	 * @param headers the headers to add
	 */
	static void addHeaders(HttpURLConnection connection, HttpHeaders headers) {
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
