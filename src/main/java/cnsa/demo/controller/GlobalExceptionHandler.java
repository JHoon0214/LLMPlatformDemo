package cnsa.demo.controller;

import cnsa.demo.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO> handleUserSaveException(RuntimeException ex) {
        ResponseDTO errorResponse = ResponseDTO.builder()
                .code(403)
                .message("Sorry...Something went wrong")
                .data("")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
