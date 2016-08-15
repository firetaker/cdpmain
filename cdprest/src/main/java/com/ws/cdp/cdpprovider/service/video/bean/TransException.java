package com.ws.cdp.cdpprovider.service.video.bean;

public class TransException extends Exception{

	private static final long serialVersionUID = -5709297412670096861L;

	TransException() {
		super();
	}

	public TransException(String message) {
		super(message);
	}

	public TransException(Throwable cause) {
		super(cause);
	}

	public TransException(String message, Throwable cause) {
		super(message, cause);
	}
}
