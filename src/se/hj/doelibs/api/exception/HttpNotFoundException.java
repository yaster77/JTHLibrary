package se.hj.doelibs.api.exception;

/**
 * Exception for Http status code 404 Not found
 *
 * @author Christoph
 */
public class HttpNotFoundException extends HttpClientErrorException {

	public HttpNotFoundException() {
		super();
	}

	public HttpNotFoundException(String message) {
		super(message);
	}

	public HttpNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
