package se.hj.doelibs.api.exception;

/**
 * Exception for Http status code 400 Bad Request
 *
 * @author Christoph
 */
public class HttpBadRequestException extends HttpClientErrorException {

	public HttpBadRequestException() {
		super();
	}

	public HttpBadRequestException(String message) {
		super(message);
	}

	public HttpBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
