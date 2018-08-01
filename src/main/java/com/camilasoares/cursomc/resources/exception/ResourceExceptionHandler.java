package com.camilasoares.cursomc.resources.exception;

import com.camilasoares.cursomc.services.exception.DataIntegrityException;
import com.camilasoares.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
		
		
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(System.currentTimeMillis (), HttpStatus.BAD_REQUEST.value(), e.getMessage(), "integridade de dados", e.getMessage (), request.getRequestURI ());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request){

		ValidationError err = new ValidationError(System.currentTimeMillis (), HttpStatus.UNPROCESSABLE_ENTITY.value (), "Erro de validação", e.getMessage (), request.getRequestURI ());
		for(FieldError x : e.getBindingResult ().getFieldErrors ()){
		    err.addError(x.getField (), x.getDefaultMessage ());
        }
        return ResponseEntity.status ( HttpStatus.UNPROCESSABLE_ENTITY).body ( err );
	}
}