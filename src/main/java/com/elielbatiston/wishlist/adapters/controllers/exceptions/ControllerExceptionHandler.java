package com.elielbatiston.wishlist.adapters.controllers.exceptions;

import com.elielbatiston.wishlist.domains.exceptions.DataIntegrityException;
import com.elielbatiston.wishlist.helpers.MessagesHelper;
import com.elielbatiston.wishlist.domains.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

	@Autowired
	private MessagesHelper messagesHelper;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
		ValidationError err = new ValidationError(
			System.currentTimeMillis(),
			HttpStatus.UNPROCESSABLE_ENTITY.value(),
			messagesHelper.getExceptionValidationMessageHeader(),
			e.getMessage(),
			request.getRequestURI()
		);

		for (FieldError x : e.getBindingResult().getFieldErrors()) {
			err.addError(x.getField(), x.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		StandardError err = new StandardError(
			System.currentTimeMillis(),
			HttpStatus.NOT_FOUND.value(),
			messagesHelper.getExceptionNotFoundMessageHeader(),
			e.getMessage(),
			request.getRequestURI()
		);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {
		StandardError err = new StandardError(
			System.currentTimeMillis(),
			HttpStatus.BAD_REQUEST.value(),
			"BAD REQUEST",
			e.getMessage(),
			request.getRequestURI()
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
}
