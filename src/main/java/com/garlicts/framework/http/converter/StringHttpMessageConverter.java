package com.garlicts.framework.http.converter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.garlicts.framework.http.HttpInputMessage;
import com.garlicts.framework.http.HttpOutputMessage;
import com.garlicts.framework.http.MediaType;
import com.garlicts.framework.util.StreamUtil;

/**
 * Implementation of {@link HttpMessageConverter} that can read and write strings.
 *
 * <p>By default, this converter supports all media types ({@code &#42;&#47;&#42;}),
 * and writes with a {@code Content-Type} of {@code text/plain}. This can be overridden
 * by setting the {@link #setSupportedMediaTypes supportedMediaTypes} property.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public class StringHttpMessageConverter extends AbstractHttpMessageConverter<String> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");


	private final Charset defaultCharset;

	private final List<Charset> availableCharsets;

	private boolean writeAcceptCharset = true;


	/**
	 * A default constructor that uses {@code "ISO-8859-1"} as the default charset.
	 * @see #StringHttpMessageConverter(Charset)
	 */
	public StringHttpMessageConverter() {
		this(DEFAULT_CHARSET);
	}

	/**
	 * A constructor accepting a default charset to use if the requested content
	 * type does not specify one.
	 */
	public StringHttpMessageConverter(Charset defaultCharset) {
		super(new MediaType("text", "plain", defaultCharset), MediaType.ALL);
		this.defaultCharset = defaultCharset;
		this.availableCharsets = new ArrayList<Charset>(Charset.availableCharsets().values());
	}


	/**
	 * Indicates whether the {@code Accept-Charset} should be written to any outgoing request.
	 * <p>Default is {@code true}.
	 */
	public void setWriteAcceptCharset(boolean writeAcceptCharset) {
		this.writeAcceptCharset = writeAcceptCharset;
	}


	@Override
	public boolean supports(Class<?> clazz) {
		return String.class.equals(clazz);
	}

	@Override
	protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage) throws IOException {
		Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
		return StreamUtil.copyToString(inputMessage.getBody(), charset);
	}

	@Override
	protected Long getContentLength(String str, MediaType contentType) {
		Charset charset = getContentTypeCharset(contentType);
		try {
			return (long) str.getBytes(charset.name()).length;
		}
		catch (UnsupportedEncodingException ex) {
			// should not occur
			throw new IllegalStateException(ex);
		}
	}

	@Override
	protected void writeInternal(String str, HttpOutputMessage outputMessage) throws IOException {
		if (this.writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}
		Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
		StreamUtil.copy(str, charset, outputMessage.getBody());
	}


	/**
	 * Return the list of supported {@link Charset}s.
	 * <p>By default, returns {@link Charset#availableCharsets()}.
	 * Can be overridden in subclasses.
	 * @return the list of accepted charsets
	 */
	protected List<Charset> getAcceptedCharsets() {
		return this.availableCharsets;
	}

	private Charset getContentTypeCharset(MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			return contentType.getCharSet();
		}
		else {
			return this.defaultCharset;
		}
	}

}
