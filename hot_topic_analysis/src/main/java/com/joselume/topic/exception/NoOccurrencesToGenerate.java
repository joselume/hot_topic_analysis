package com.joselume.topic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoOccurrencesToGenerate extends RuntimeException{

	private static final long serialVersionUID = -8529524852207804037L;

	public NoOccurrencesToGenerate(String message) {
		super(message);
	}
}
