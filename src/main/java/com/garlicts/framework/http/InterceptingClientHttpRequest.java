package com.garlicts.framework.http;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import com.garlicts.framework.util.StreamUtil;

/**
 * Wrapper for a {@link ClientHttpRequest} that has support for {@link ClientHttpRequestInterceptor}s.
 *
 * @author Arjen Poutsma
 * @since 3.1
 */
class InterceptingClientHttpRequest extends AbstractBufferingClientHttpRequest {

	private final ClientHttpRequestFactory requestFactory;

	private final List<ClientHttpRequestInterceptor> interceptors;

	private HttpMethod method;

	private URI uri;


	protected InterceptingClientHttpRequest(ClientHttpRequestFactory requestFactory,
			List<ClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod method) {

		this.requestFactory = requestFactory;
		this.interceptors = interceptors;
		this.method = method;
		this.uri = uri;
	}


	public HttpMethod getMethod() {
		return this.method;
	}

	public URI getURI() {
		return this.uri;
	}

	@Override
	protected final ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
		InterceptingRequestExecution requestExecution = new InterceptingRequestExecution();
		return requestExecution.execute(this, bufferedOutput);
	}


	private class InterceptingRequestExecution implements ClientHttpRequestExecution {

		private final Iterator<ClientHttpRequestInterceptor> iterator;

		public InterceptingRequestExecution() {
			this.iterator = interceptors.iterator();
		}

		public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
			if (this.iterator.hasNext()) {
				ClientHttpRequestInterceptor nextInterceptor = this.iterator.next();
				return nextInterceptor.intercept(request, body, this);
			}
			else {
				ClientHttpRequest delegate = requestFactory.createRequest(request.getURI(), request.getMethod());
				delegate.getHeaders().putAll(request.getHeaders());
				if (body.length > 0) {
					StreamUtil.copy(body, delegate.getBody());
				}
				return delegate.execute();
			}
		}
		
	}

}
