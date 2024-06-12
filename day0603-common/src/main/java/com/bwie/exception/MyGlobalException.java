package com.bwie.exception;

import com.bwie.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-05 10:44
 */
@RestControllerAdvice
@Slf4j
public class MyGlobalException {
    @ExceptionHandler(value = Exception.class)
    Object handlerException(Exception e, HttpServletRequest request){
        log.error("url={},msg={}",request.getRequestURL(),e);
        if(e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            return R.error(500, exception.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("; ")));
        }
        return R.error(500,e.getMessage());
    }
}
