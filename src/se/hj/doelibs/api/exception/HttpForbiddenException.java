package se.hj.doelibs.api.exception;

/**
 * Exception for Http status code 403 Forbidden
 *
 * @author Christoph
 */
public class HttpForbiddenException extends HttpClientErrorException {

	public HttpForbiddenException() {
		super();
	}

	public HttpForbiddenException(String message) {
		super(message);
	}

	public HttpForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}
}
