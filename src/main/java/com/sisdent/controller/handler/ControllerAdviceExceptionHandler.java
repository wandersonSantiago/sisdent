package com.sisdent.controller.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sisdent.service.exception.NomeOuCodigoJaCadastradoException;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {

	@ExceptionHandler(NomeOuCodigoJaCadastradoException.class)
	public ResponseEntity<String> handleNomeEstiloJaCadastradoException(NomeOuCodigoJaCadastradoException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
}
