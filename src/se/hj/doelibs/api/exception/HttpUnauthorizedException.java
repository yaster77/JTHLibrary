package se.hj.doelibs.api.exception;

/**
 * Exception for Http status code 401 Unauthorized
 *
 * @author Christoph
 */
public class HttpUnauthorizedException extends HttpClientErrorException {

	public HttpUnauthorizedException() {
		super();
	}

	public HttpUnauthorizedException(String message) {
		super(message);
	}

	public HttpUnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}
}
