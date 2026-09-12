package nextcp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import nextcp.upnp.GenActionException;
import nextcp.util.BackendException;

@RestControllerAdvice
public class BackendExceptionAdvice {

	private static final Logger log = LoggerFactory.getLogger(BackendExceptionAdvice.class.getName());

	public BackendExceptionAdvice() {
	}

	@ExceptionHandler(value = { BackendException.class })
	@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
	public String mapCode(BackendException ex) {
		return ex.getMessage();
	}

	/**
	 * A UPnP action against a device failed. Without this handler the exception escaped as an empty
	 * HTTP 500 and the UI showed a nameless error. BAD_GATEWAY says what it is: the device behind us
	 * failed, not the request.
	 */
	@ExceptionHandler(value = { GenActionException.class })
	@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
	public String mapUpnpActionError(GenActionException ex) {
		String message = ex.description != null && !ex.description.isBlank() ? ex.description
				: "UPnP action failed with error code " + ex.errorCode;
		log.warn("UPnP action failed [errorCode={}] : {}", ex.errorCode, message);
		return message;
	}
}
