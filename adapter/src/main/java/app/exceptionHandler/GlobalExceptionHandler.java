package app.exceptionHandler;

import exceptions.ProductNotFoundException;
import exceptions.ProductQuantityException;
import exceptions.ProductsOptionQuantity;
import exceptions.UserIsNotTheOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProblemDetail> handle(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ProductQuantityException.class)
    public ResponseEntity<ProblemDetail> handle(ProductQuantityException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(ProductsOptionQuantity.class)
    public ResponseEntity<ProblemDetail> handle(ProductsOptionQuantity ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(UserIsNotTheOwnerException.class)
    public ResponseEntity<ProblemDetail> handle(UserIsNotTheOwnerException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage()));
    }
}
