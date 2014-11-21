package se.hj.doelibs.api.exception;

/**
 * Exception for Http status code 501 Not Implemented
 *
 * @author Christoph
 */
public class HttpNotImplementedException extends HttpServerErrorException {

	public HttpNotImplementedException() {
		super();
	}

	public HttpNotImplementedException(String message) {
		super(message);
	}

	public HttpNotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}
}
