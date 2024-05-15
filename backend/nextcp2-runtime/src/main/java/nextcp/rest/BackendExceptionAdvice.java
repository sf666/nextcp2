package nextcp.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import nextcp.util.BackendException;

@RestControllerAdvice
public class BackendExceptionAdvice {

	public BackendExceptionAdvice() {
	}

	@ExceptionHandler(value = { BackendException.class })
	@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
	public String mapCode(BackendException ex) {
		return ex.getMessage();
	}
}
