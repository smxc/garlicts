package com.garlicts.framework.http;

import java.io.IOException;
import java.net.URI;

public interface HttpRequestFactory {

	HttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException;
	
}
