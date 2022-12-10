package yoonleeverse.onlinejudge.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yoonleeverse.onlinejudge.api.common.dto.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse onAccessDenied(AccessDeniedException e) {
        APIResponse response = new APIResponse();
        response.setSuccess(false);
        response.setErrMsg(e.getMessage());
        return response;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public APIResponse onFailedResult(RuntimeException e) {
        APIResponse response = new APIResponse();
        response.setSuccess(false);
        response.setErrMsg(e.getMessage());
        return response;
    }
}
