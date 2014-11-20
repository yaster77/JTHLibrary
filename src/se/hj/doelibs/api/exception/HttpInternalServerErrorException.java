package se.hj.doelibs.api.exception;

/**
 * Exception for Http status code 500 Internal Server Error
 *
 * @author Christoph
 */
public class HttpInternalServerErrorException extends HttpServerErrorException {

	public HttpInternalServerErrorException() {
		super();
	}

	public HttpInternalServerErrorException(String message) {
		super(message);
	}

	public HttpInternalServerErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
