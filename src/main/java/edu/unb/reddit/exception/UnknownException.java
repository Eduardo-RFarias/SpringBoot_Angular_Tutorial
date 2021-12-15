package edu.unb.reddit.exception;

public class UnknownException extends RuntimeException {
	private static final long serialVersionUID = -7236094135202173025L;

	public UnknownException(String message) {
		super(message);
	}
}
