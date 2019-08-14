package com.clickbus.placesapi.exceptionhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.clickbus.placesapi.exception.PlaceNotFoundExeption;
import com.clickbus.placesapi.exception.SlugAlreadyExistException;

@ControllerAdvice
public class PlaceAPIExceptionHandler extends ResponseEntityExceptionHandler {

	
	@ExceptionHandler(value = {SlugAlreadyExistException.class})
	public ResponseEntity<Object> handleAlreadyExistException(RuntimeException ex, WebRequest request){
		return this.handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@ExceptionHandler(value = {PlaceNotFoundExeption.class})
	public ResponseEntity<Object> handlePlaceNotFoundException(RuntimeException ex, WebRequest request){
		return this.handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
}
