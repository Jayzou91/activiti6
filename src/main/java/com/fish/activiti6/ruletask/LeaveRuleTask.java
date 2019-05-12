package com.fish.activiti6.ruletask;

import org.activiti.engine.delegate.DelegateExecution;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/8
 */
public class LeaveRuleTask {
    /**
     * @param execution
     * @param name      用于区分触发哪些规则  sessionname
     */
    public void fireRule(String name, DelegateExecution execution) {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieSession kieSession = kContainer.newKieSession(name);
        //插入入参
        kieSession.insert(execution.getVariable("leaveBean"));
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("rule has fired");
    }
}
