package com.fish.activiti6.resbean;

import com.fish.activiti6.constant.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description: REST 接口返回对象
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultBean<T> {

    private T data;
    private Integer code = ErrorCodeEnum.SUCCESS.getCode();
    private String msg = ErrorCodeEnum.SUCCESS.getMsg();
}
