package edu.unb.reddit.exception;

public class InvalidTokenException extends RuntimeException {
	private static final long serialVersionUID = 369192267823846987L;

	public InvalidTokenException(String message) {
		super(message);
	}
}
