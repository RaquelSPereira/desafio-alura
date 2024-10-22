package br.com.alura.ProjetoAlura.util.interceptor;

import br.com.alura.ProjetoAlura.dtos.Error.ErrorItemDTO;
import br.com.alura.ProjetoAlura.util.exceptions.CreatedException;
import br.com.alura.ProjetoAlura.util.exceptions.NotFoundException;
import br.com.alura.ProjetoAlura.util.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorItemDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorItemDTO> errors = ex.getBindingResult().getFieldErrors().stream().map(ErrorItemDTO::new).toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorItemDTO> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(new ErrorItemDTO("Error", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorItemDTO> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorItemDTO("Error", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreatedException.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ErrorItemDTO> handleCreatedException(CreatedException ex){
        return new ResponseEntity<>(new ErrorItemDTO("Criado", ex.getMessage()), HttpStatus.CREATED);
    }
}