package com.fish.activiti6.service.impl;

import com.fish.activiti6.dao.VideoMapper;
import com.fish.activiti6.domain.UploadVideoDO;
import com.fish.activiti6.dto.VideoInfo;
import com.fish.activiti6.service.VideoService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private IdentityService identityservice;
    @Autowired
    private RuntimeService runtimeservice;
    @Autowired
    private TaskService taskservice;

    @Autowired
    private VideoMapper videoMapper;

    @Override
    @Transactional
    public ProcessInstance startWorkflow(VideoInfo info) {
        UploadVideoDO uploadVideoDO = new UploadVideoDO();
        uploadVideoDO.setVideoName(info.getVideoName());
        uploadVideoDO.setUploader(info.getUploader());
        uploadVideoDO.setCreateTime(LocalDateTime.now());
        videoMapper.insertUploadVideo(uploadVideoDO);

        //使用upload_vide表的主键作为businesskey，连接业务数据和流程数据
        String businesskey = String.valueOf(uploadVideoDO.getId());
        identityservice.setAuthenticatedUserId(info.getUploader());
        ProcessInstance instance = runtimeservice.startProcessInstanceByKey("videomaker", businesskey);
        uploadVideoDO.setProcessInstanceId(instance.getId());
        videoMapper.updateUploadVideo(uploadVideoDO);
        return instance;
    }
}
