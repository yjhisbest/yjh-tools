package com.yjh.tools.demo.exception;

import com.yjh.tools.core.exception.CommonException;
import com.yjh.tools.demo.result.Result;
import com.yjh.tools.demo.result.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    /**
     * 全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e, HttpServletRequest request) {
        logger(e, request);
        return Result.failure();
    }

    /**
     * yjh-tools-core包异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public Object handleException(CommonException e, HttpServletRequest request) {
        logger(e, request);
        return Result.result(Status.ERROR, e.getMessage());
    }

    private void logger(Exception e, HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        log.error("[{}] [{}] {}", e.getClass().getName(), url, e);
    }
}
