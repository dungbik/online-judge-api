package yoonleeverse.onlinejudge.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse onSystemError(RuntimeException e) {
        APIResponse response = new APIResponse();
        response.setSuccess(false);
        response.setErrMsg(e.getMessage());
        return response;
    }
}
