package simpleerp.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoNameException.class)
    public ResponseEntity<String> handleNoNameException(NoNameException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }


    @ExceptionHandler(NoEmailException.class)
    public ResponseEntity<String> handleNoEmailException(NoEmailException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    public ProblemDetail handleDuplicateException(DuplicateException ex){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        problem.setTitle("Duplicate Entity");
        return problem;
    }

    @ExceptionHandler(WrongValueException.class)
    public ProblemDetail handleWrongValueException(WrongValueException ex){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        problem.setTitle("Invalid Input Value");
        return problem;
    }


    @ExceptionHandler(CannotDeleteException.class)
    public ResponseEntity<String> handleCannotDeleteComponentActiveInBOM(CannotDeleteException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MissingEntityException.class)
    public ProblemDetail handleMissingEntityException(MissingEntityException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Entity Not Found");
        problemDetail.setType(URI.create("https://api.simpleerp.com/errors/not-found"));
        return problemDetail;
        }

    @ExceptionHandler(IllegalOperationException.class)
    public ProblemDetail handleIllegalOperation(IllegalOperationException ex){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        problem.setTitle("Illegal Operation");
        return problem;
    }


}
