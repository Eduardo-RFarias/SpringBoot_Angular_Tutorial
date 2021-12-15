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
	public ResponseEntity<String> handleNotFound(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<String> handleInvalidToken(InvalidTokenException ex) {
		// TODO Figure out why this handler is not working
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}

	@ExceptionHandler(UnknownException.class)
	public ResponseEntity<String> handleUnknown(UnknownException ex) {
		// TODO Figure out why this handler is not working
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
}
