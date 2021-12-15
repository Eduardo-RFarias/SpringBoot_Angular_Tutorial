package edu.unb.reddit.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.unb.reddit.exception.InvalidTokenException;
import edu.unb.reddit.exception.NotFoundException;
import edu.unb.reddit.exception.UnknownException;

@ControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handleNotFound(NotFoundException ex) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<?> handleInvalidToken(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(ex.getMessage());
	}

	@ExceptionHandler(UnknownException.class)
	public ResponseEntity<?> handleUnknown(NotFoundException ex) {
		// TODO Figure out why this handler is not working
		var cause = ex.getCause();
		return ResponseEntity.internalServerError().body(cause != null ? cause : ex.toString());
	}
}
