package com.fish.activiti6.service;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/8
 */
@Slf4j
@Service
@Transactional
public class ActivitiService {

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final HistoryService historyService;

    private final RepositoryService repositoryService;

    @Autowired
    public ActivitiService(RuntimeService runtimeService, TaskService taskService, HistoryService historyService, RepositoryService repositoryService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.historyService = historyService;
        this.repositoryService = repositoryService;
    }

    public ProcessDefinition deployProcess(MultipartFile bpmn, String path) throws IOException {
        if (ObjectUtils.isEmpty(bpmn) || StringUtils.isEmpty(path)) {
            return null;
        }
        //上传文件到processes
        File file = new File(path + bpmn.getOriginalFilename());
        bpmn.transferTo(file);

        String resource = ResourceLoader.CLASSPATH_URL_PREFIX + bpmn.getOriginalFilename();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource(resource).deploy();
        log.info("Process [" + deployment.getName() + "] deployed successful");
        return repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
    }

    public ProcessInstance startProcess(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return runtimeService.startProcessInstanceByKey(key);
    }

    public List<TaskInfo> getTasksByAssignee(String assignee) {
        if (StringUtils.isEmpty(assignee)) {
            return null;
        }
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        if (ObjectUtils.isEmpty(tasks)) {
            return null;
        }
        List<TaskInfo> infos = new ArrayList<>();
        for (Task task : tasks) {
            infos.add(new TaskInfo(task.getId(), task.getName()));
        }
        return infos;
    }

    public List<TaskInfo> getTasksByGroup(String group) {
        if (StringUtils.isEmpty(group)) {
            return null;
        }
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(group).list();
        if (ObjectUtils.isEmpty(tasks)) {
            return null;
        }
        List<TaskInfo> infos = new ArrayList<>();
        for (Task task : tasks) {
            infos.add(new TaskInfo(task.getId(), task.getName()));
        }
        return infos;
    }

    public List<TaskInfo> getTasks(String assigneeOrGroup) {
        if (StringUtils.isEmpty(assigneeOrGroup)) {
            return null;
        }
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(assigneeOrGroup).list();
        if (ObjectUtils.isEmpty(tasks)) {
            return null;
        }
        List<TaskInfo> infos = new ArrayList<>();
        for (Task task : tasks) {
            infos.add(new TaskInfo(task.getId(), task.getName()));
        }
        return infos;
    }

    public void completeTask(String taskId, Object item) {
        Map<String, Object> map = objectToMap(item);
        if (StringUtils.isEmpty(taskId) || ObjectUtils.isEmpty(map)) {
            log.error("Params cannot be empty");
            throw new RuntimeException("Params cannot be empty");
        }
        taskService.complete(taskId, map);
    }

    private Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : null;
                map.put(key, value);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }

    public void completeTask(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            log.error("Param taskId cannot be empty");
            return;
        }
        taskService.complete(taskId);
    }

    public static class TaskInfo {

        private String id;
        private String name;

        public TaskInfo(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}