package com.garlicts.framework.util;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流操作工具类
 *
 * @author 水木星辰
 * @since 1.0
 */
public class StreamUtil {

    private static final Logger logger = LoggerFactory.getLogger(StreamUtil.class);

    public static final int BUFFER_SIZE = 4096;
    
    /**
     * 将输入流复制到输出流
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream) {
        try {
            int length;
            byte[] buffer = new byte[4 * 1024];
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        } catch (Exception e) {
            logger.error("复制流出错！");
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                logger.error("释放资源出错！");
            }
        }
    }

    /**
     * 从输入流中获取字符串
     */
    public static String getString(InputStream is) {
    	
    	if(Objects.equals(is, null)){
    		return "";
    	}
    	
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            logger.error("Stream 转 String 出错！");
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
    
	/**
	 * Copy the contents of the given InputStream to the given OutputStream.
	 * Leaves both streams open when done.
	 * @param in the InputStream to copy from
	 * @param out the OutputStream to copy to
	 * @return the number of bytes copied
	 * @throws IOException in case of I/O errors
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
		Assert.notNull(in, "No InputStream specified");
		Assert.notNull(out, "No OutputStream specified");
		int byteCount = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}
		out.flush();
		return byteCount;
	}    
	
	/**
	 * Returns a variant of the given {@link InputStream} where calling
	 * {@link InputStream#close() close()} has no effect.
	 * @param in the InputStream to decorate
	 * @return a version of the InputStream that ignores calls to close
	 */
	public static InputStream nonClosing(InputStream in) {
		Assert.notNull(in, "No InputStream specified");
		return new NonClosingInputStream(in);
	}

	/**
	 * Returns a variant of the given {@link OutputStream} where calling
	 * {@link OutputStream#close() close()} has no effect.
	 * @param out the OutputStream to decorate
	 * @return a version of the OutputStream that ignores calls to close
	 */
	public static OutputStream nonClosing(OutputStream out) {
		Assert.notNull(out, "No OutputStream specified");
		return new NonClosingOutputStream(out);
	}


	private static class NonClosingInputStream extends FilterInputStream {

		public NonClosingInputStream(InputStream in) {
			super(in);
		}

		@Override
		public void close() throws IOException {
		}
	}


	private static class NonClosingOutputStream extends FilterOutputStream {

		public NonClosingOutputStream(OutputStream out) {
			super(out);
		}

		@Override
		public void write(byte[] b, int off, int let) throws IOException {
			// It is critical that we override this method for performance
			out.write(b, off, let);
		}

		@Override
		public void close() throws IOException {
		}
		
	}	
	
	/**
	 * Copy the contents of the given byte array to the given OutputStream.
	 * Leaves the stream open when done.
	 * @param in the byte array to copy from
	 * @param out the OutputStream to copy to
	 * @throws IOException in case of I/O errors
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No OutputStream specified");
		out.write(in);
	}	
	
	/**
	 * Copy the contents of the given InputStream into a String.
	 * Leaves the stream open when done.
	 * @param in the InputStream to copy from
	 * @return the String that has been copied to
	 * @throws IOException in case of I/O errors
	 */
	public static String copyToString(InputStream in, Charset charset) throws IOException {
		Assert.notNull(in, "No InputStream specified");
		StringBuilder out = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(in, charset);
		char[] buffer = new char[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = reader.read(buffer)) != -1) {
			out.append(buffer, 0, bytesRead);
		}
		return out.toString();
	}	
	
	/**
	 * Copy the contents of the given String to the given output OutputStream.
	 * Leaves the stream open when done.
	 * @param in the String to copy from
	 * @param charset the Charset
	 * @param out the OutputStream to copy to
	 * @throws IOException in case of I/O errors
	 */
	public static void copy(String in, Charset charset, OutputStream out) throws IOException {
		Assert.notNull(in, "No input String specified");
		Assert.notNull(charset, "No charset specified");
		Assert.notNull(out, "No OutputStream specified");
		Writer writer = new OutputStreamWriter(out, charset);
		writer.write(in);
		writer.flush();
	}
    
}