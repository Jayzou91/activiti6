package com.fish.activiti6.constant;

import lombok.Getter;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
@Getter
public enum ErrorCodeEnum {

    SUCCESS(0, "Success");

    ErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 错误码
    private Integer code;
    // 错误提示信息
    private String msg;
}
