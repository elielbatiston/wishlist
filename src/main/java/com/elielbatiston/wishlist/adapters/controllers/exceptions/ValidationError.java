package com.elielbatiston.wishlist.adapters.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {	

	private static final long serialVersionUID = 1L;

	public ValidationError() {
		super(null, null, null, null, null);
	}

	public ValidationError(
		final Long timestamp,
		final Integer status,
		final String error,
		final String message,
		final String path
	) {
		super(timestamp, status, error, message, path); 
	}
	
	private List<FieldMessage> errors = new ArrayList<>();
	
	public List<FieldMessage> getErrors() {
		return errors;
	}
	
	public void addError(final String fieldName, final String message) {
		final FieldMessage fieldMessage = new FieldMessage(fieldName, message);		
		errors.add(fieldMessage);
	}
}
