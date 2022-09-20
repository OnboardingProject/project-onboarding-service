package com.account.onboarding.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccountCustomerException.class)
	public ResponseEntity<String> handleException(AccountCustomerException accountCustomerException) {
		return new ResponseEntity<String>(accountCustomerException.getErrorMessage(),
				accountCustomerException.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		Map<String, String> response = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			response.put(fieldName, message);
		});
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserAlreadyFoundException.class)
	public ResponseEntity<ErrorInfo> userAlreadyFoundException(UserAlreadyFoundException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMsg());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorInfo> userNotFoundException(UserNotFoundException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMsg());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RoleAlreadySameException.class)
	public ResponseEntity<ErrorInfo> roleAlreadySameException(RoleAlreadySameException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMsg());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RoleIdNullException.class)
	public ResponseEntity<ErrorInfo> roleIdNullException(RoleIdNullException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMsg());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmailNotValidException.class)
	public ResponseEntity<ErrorInfo> emailNotValidException(EmailNotValidException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMsg());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PhoneNumberNotValidException.class)
	public ResponseEntity<ErrorInfo> phoneNumberNotValidException(PhoneNumberNotValidException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMsg());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserUpdateException.class)
	public ResponseEntity<ErrorInfo> applicationException(UserUpdateException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMsg());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> exception(Exception ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMessage());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles the javax validation exceptions like @NotBlank errors
	 * 
	 * @param ex
	 * @return ErrorInfo response entity representation
	 */
	/*
	 * @ExceptionHandler(MethodArgumentNotValidException.class) public
	 * ResponseEntity<ErrorInfo> invalidException(MethodArgumentNotValidException
	 * ex) { List<String> errors = ex.getFieldErrors().stream().map(e ->
	 * e.getField() + ":" + e.getDefaultMessage()) .collect(Collectors.toList());
	 * ErrorInfo errorInfo = new ErrorInfo(); errorInfo.setErrorMessage(errors);
	 * errorInfo.setTime(LocalDateTime.now()); return new
	 * ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.BAD_REQUEST); }
	 */

	/**
	 * Handles the custom exception thrown for creating a project with same name
	 * 
	 * @param ex
	 * @return ErrorInfo response entity representation
	 */
	@ExceptionHandler(NameAlreadyExistingException.class)
	public ResponseEntity<ErrorInfo> nameException(NameAlreadyExistingException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMsg());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles the exception for resource not found and returns 404
	 * 
	 * @param ex
	 * @return ErrorInfo response entity representation
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorInfo> NoResorceException(NoResourceFoundException ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorMessage(ex.getMsg());
		errorInfo.setTime(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<String> productIdNotFoundException(DataNotFoundException exp) {
		return new ResponseEntity<String>(exp.getErrorMessage(), HttpStatus.NOT_FOUND);
	}

}
