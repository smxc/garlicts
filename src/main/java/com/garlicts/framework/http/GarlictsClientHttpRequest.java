package com.garlicts.framework.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import com.garlicts.framework.util.StreamUtil;

/**
 * {@link ClientHttpRequest} implementation that uses standard JDK facilities to
 * execute streaming requests. Created via the {@link GarlictsClientHttpRequestFactory}.
 *
 * @author Arjen Poutsma
 * @since 3.0
 * @see GarlictsClientHttpRequestFactory#createRequest(java.net.URI, HttpMethod)
 */
final class GarlictsClientHttpRequest extends AbstractClientHttpRequest {

	private final HttpURLConnection connection;

	private final int chunkSize;

	private OutputStream body;

	private final boolean outputStreaming;


	GarlictsClientHttpRequest(HttpURLConnection connection, int chunkSize, boolean outputStreaming) {
		this.connection = connection;
		this.chunkSize = chunkSize;
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
	protected OutputStream getBodyInternal(HttpHeaders headers) {
		if (this.body == null) {
			if (this.outputStreaming) {
				int contentLength = (int) headers.getContentLength();
				if (contentLength >= 0) {
					this.connection.setFixedLengthStreamingMode(contentLength);
				}
				else {
					this.connection.setChunkedStreamingMode(this.chunkSize);
				}
			}
			addHeaders(this.connection, headers);
			try {
				this.connection.connect();
				this.body = this.connection.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return StreamUtil.nonClosing(this.body);
	}

	@Override
	protected ClientHttpResponse executeInternal(HttpHeaders headers) {
		try {
			if (this.body != null) {
				this.body.close();
			}
			else {
				addHeaders(this.connection, headers);
				this.connection.connect();
			}
		}
		catch (IOException ex) {
			// ignore
		}
		return new GarlictsClientHttpResponse(this.connection);
	}

}
