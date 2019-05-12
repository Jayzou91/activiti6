package com.fish.activiti6.task.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/8
 */
@Setter
@Getter
@AllArgsConstructor
public class SaleItem implements Serializable {

    // 商品名称
    private String goodsName;
    //商品单价
    private BigDecimal price;

    //数量
    private BigDecimal amount;
}
