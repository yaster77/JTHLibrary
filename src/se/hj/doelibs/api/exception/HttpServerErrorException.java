package se.hj.doelibs.api.exception;

import org.apache.http.HttpException;

/**
 * Exception for 5xx Http status codes (500-599)
 *
 * @author Christoph
 */
public class HttpServerErrorException extends HttpException {

	public HttpServerErrorException() {
		super();
	}

	public HttpServerErrorException(String message) {
		super(message);
	}

	public HttpServerErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
