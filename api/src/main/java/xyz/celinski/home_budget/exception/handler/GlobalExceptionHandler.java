package xyz.celinski.home_budget.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import xyz.celinski.home_budget.dto.HttpErrorDTO;
import xyz.celinski.home_budget.exception.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private HttpErrorDTO createHttpErrorDTO(LocalDateTime timestamp, int statusCode, String error, String path) {
        HttpErrorDTO httpErrorDTO = new HttpErrorDTO();
        httpErrorDTO.setTimestamp(timestamp.toString());
        httpErrorDTO.setStatus(statusCode);
        httpErrorDTO.setError(error);
        httpErrorDTO.setPath(path);
        return httpErrorDTO;
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<HttpErrorDTO> handleInvalidTokenException(InvalidTokenException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(LocalDateTime.now(), status.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(httpErrorDTO, status);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<HttpErrorDTO> handleInvalidDateRangeException(InvalidDateRangeException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(LocalDateTime.now(), status.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(httpErrorDTO, status);
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<HttpErrorDTO> handleExpenseNotFoundException(ExpenseNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(LocalDateTime.now(), status.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(httpErrorDTO, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpErrorDTO> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(LocalDateTime.now(), status.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(httpErrorDTO, status);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<HttpErrorDTO> handleUserAlreadyExistsException(UserAlreadyExistsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(LocalDateTime.now(), status.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(httpErrorDTO, status);
    }

    @ExceptionHandler(InvalidUserDetailsException.class)
    public ResponseEntity<HttpErrorDTO> handleInvalidUserDetailsException(InvalidUserDetailsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(LocalDateTime.now(), status.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(httpErrorDTO, status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpErrorDTO> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(LocalDateTime.now(), status.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(httpErrorDTO, status);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<HttpErrorDTO> handleInvalidCredentialsException(InvalidCredentialsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        HttpErrorDTO httpErrorDTO = createHttpErrorDTO(LocalDateTime.now(), status.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(httpErrorDTO, status);
    }

}



