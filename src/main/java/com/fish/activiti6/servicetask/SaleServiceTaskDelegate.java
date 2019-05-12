package com.fish.activiti6.servicetask;

import com.fish.activiti6.task.bean.Sale;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.Collection;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/8
 */
public class SaleServiceTaskDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        Collection sales = (Collection) execution.getVariable("saleResults");
        System.out.println("输出处理结果：");
        for (Object obj : sales) {
            Sale sale = (Sale) obj;
            System.out.println("销售单：" + sale.getSaleCode() + "原价："
                    + sale.getTotal() + "优惠后：" + sale.getDiscountTotal()
                    + "折扣：" + sale.getDiscount());
        }
    }
}
