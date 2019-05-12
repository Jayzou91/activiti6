package com.fish.activiti6.exception;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
public class CommonRequestException extends RuntimeException {

    public CommonRequestException(String message) {
        super(message);
    }

    public CommonRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
