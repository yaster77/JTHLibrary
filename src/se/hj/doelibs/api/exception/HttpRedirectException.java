package se.hj.doelibs.api.exception;

import org.apache.http.HttpException;

/**
 * Exception for 3xx Http status codes (300-308)
 *
 * @author Christoph
 */
public class HttpRedirectException extends HttpException {

	public HttpRedirectException() {
		super();
	}

	public HttpRedirectException(String message) {
		super(message);
	}

	public HttpRedirectException(String message, Throwable cause) {
		super(message, cause);
	}
}
