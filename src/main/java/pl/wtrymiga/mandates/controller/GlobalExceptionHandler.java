package pl.wtrymiga.mandates.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import pl.wtrymiga.mandates.controller.Message.MessageType;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)

	public ResponseEntity<Message> handleBadData(MethodArgumentNotValidException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(
						new Message(
								"Uzupełnij dane: " + ex.getBindingResult().getFieldErrors().stream()
										.map(FieldError::getField).collect(Collectors.joining(", ")),
								MessageType.warning));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Message> handleBadDataValidation(ConstraintViolationException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(
						new Message(
								"Uzupełnij dane: " + ex.getConstraintViolations().stream()
										.map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")),
								MessageType.warning));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Message> handleException(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(new Message("Wystąpił błąd: " + ex.getMessage(), MessageType.danger));
	}
}
