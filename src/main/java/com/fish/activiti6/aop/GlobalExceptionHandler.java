package com.fish.activiti6.aop;

import com.fish.activiti6.exception.CommonRequestException;
import com.fish.activiti6.resbean.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultBean handleException(HttpServletResponse response, Exception ex) {
        ResultBean resultBean = new ResultBean();
        if (ex instanceof CommonRequestException) {
            resultBean.setCode(422);
            resultBean.setMsg(ex.getMessage());
            response.setStatus(422);
        } else {
            resultBean.setCode(500);
            resultBean.setMsg(ex.getMessage());
            response.setStatus(500);
        }
        return resultBean;
    }
}
