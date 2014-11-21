package se.hj.doelibs.api.exception;

import org.apache.http.HttpException;

/**
 * Exception for 4xx Http status codes (400-499)
 *
 * @author Christoph
 */
public class HttpClientErrorException extends HttpException {

	public HttpClientErrorException() {
		super();
	}

	public HttpClientErrorException(String message) {
		super(message);
	}

	public HttpClientErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
