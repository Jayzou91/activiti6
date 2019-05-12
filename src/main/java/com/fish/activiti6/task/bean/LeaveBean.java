package com.fish.activiti6.task.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/8
 */
@Getter
@Setter
public class LeaveBean {
    private String proposer;
    private Integer day;
    private Boolean leaderApprove;
    private Boolean managerApprove;
    private Boolean isNeedManagerAudit;
}
