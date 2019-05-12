package com.fish.activiti6;

import com.fish.activiti6.task.bean.Sale;
import com.fish.activiti6.task.bean.SaleItem;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Activiti6ApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSaleProcess() {
        //
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        //得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        //得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        //得到任务服务组件
        TaskService taskService = engine.getTaskService();
        //部署流程文件
        repositoryService.createDeployment()
                .addClasspathResource("rule/sale.drl")
                .addClasspathResource("process/sale.bpmn")
                .deploy();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("process1");

        //创建事实实例， 符合周六日打儿折条件
        Sale s1 = new Sale("001", createDate("2017-07-01"));
        SaleItem s1item1 = new SaleItem("矿泉水", new BigDecimal(5), new BigDecimal(4));
        s1.addItem(s1item1);
        //满100 元打八折
        Sale s2 = new Sale("002", createDate("2017-07-03"));
        SaleItem s2Item1 = new SaleItem("矿泉水", new BigDecimal(20), new BigDecimal(5));
        s2.addItem(s2Item1);

        //满200元打七折
        Sale s3 = new Sale("003", createDate("2017-07-03"));
        SaleItem s3Item1 = new SaleItem("可乐一箱", new BigDecimal(70), new BigDecimal(3));
        s3.addItem(s3Item1);

        //星期天满200元
        Sale s4 = new Sale("004", createDate("2017-07-02"));
        SaleItem s4Item1 = new SaleItem("爆米花一箱", new BigDecimal(80), new BigDecimal(3));
        s4.addItem(s4Item1);
        Map<String, Object> vars = new HashMap<>();
        vars.put("sale1", s1);
        vars.put("sale2", s2);
        vars.put("sale3", s3);
        vars.put("sale4", s4);

        //查找任务
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        taskService.complete(task.getId(), vars);
    }

    @Test
    public void testActivti6Demo() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addClasspathResource("process/VacationRequest.bpmn20.xml")
                .deploy();

        System.out.println("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employeeName", "Kermit");
        variables.put("numberOfDays", new Integer(4));
        variables.put("vacationMotivation", "I'm really tired!");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);

        // Verify that we started a new process instance
        System.out.println("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());

        // Fetch all tasks for the management group
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            System.out.println("Task available: " + task.getName());
        }

        Task task = tasks.get(0);

        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("vacationApproved", "false");
        taskVariables.put("managerMotivation", "We have a tight deadline!");
        taskService.complete(task.getId(), taskVariables);
    }


    @Test
    public void testTenMinuteTutorial() {
//        // Create Activiti process engine
//        ProcessEngine processEngine = ProcessEngineConfiguration
//                .createStandaloneProcessEngineConfiguration()
//                .buildProcessEngine();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // Get Activiti services
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // Deploy the process definition
        repositoryService.createDeployment()
                .addClasspathResource("process/FinancialReportProcess.bpmn20.xml")
                .deploy();

        // Start a process instance
        String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();

        // Get the first task
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
        for (Task task : tasks) {
            System.out.println("Following task is available for accountancy group: " + task.getName());

            // claim it
            taskService.claim(task.getId(), "fozzie");
        }

        // Verify Fozzie can now retrieve the task
        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
        for (Task task : tasks) {
            System.out.println("Task for fozzie: " + task.getName());

            // Complete the task
            taskService.complete(task.getId());
        }

        System.out.println("Number of tasks for fozzie: "
                + taskService.createTaskQuery().taskAssignee("fozzie").count());

        // Retrieve and claim the second task
        tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            System.out.println("Following task is available for management group: " + task.getName());
            taskService.claim(task.getId(), "kermit");
        }

        // Completing the second task ends the process
        for (Task task : tasks) {
            taskService.complete(task.getId());
        }

        // verify that the process is actually finished
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();
        System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());

        /**
         * Future enhancements:
         * It’s easy to see that this business process is too simple to be usable in reality. However, as you are going through the BPMN 2.0 constructs available in Activiti, you will be able to enhance the business process by:
         *
         * 1. defining gateways that act as decisions. This way, a manager could reject the financial report which would recreate the task for the accountant.
         *
         * 2. declaring and using variables, such that we can store or reference the report so that it can be visualized in the form.
         *
         * 3. defining a service task at the end of the process that will send the report to every shareholder.
         *
         * etc.
         */
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    static Date createDate(String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Parse date error::" + e.getMessage());
        }
    }
}
