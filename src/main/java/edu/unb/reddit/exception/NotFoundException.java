package edu.unb.reddit.exception;

public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = -4985730203216957851L;

	public NotFoundException(String message) {
		super(message);
	}
}