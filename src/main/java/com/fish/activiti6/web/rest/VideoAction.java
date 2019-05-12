package com.fish.activiti6.web.rest;

import com.fish.activiti6.constant.ErrorCodeEnum;
import com.fish.activiti6.dto.VideoInfo;
import com.fish.activiti6.request.VideoRequest;
import com.fish.activiti6.resbean.ResultBean;
import com.fish.activiti6.service.VideoService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
@RestController
@RequestMapping("/video")
public class VideoAction {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public ResultBean<String> uploadVideo(@RequestBody VideoRequest videoRequest) {
        ProcessInstance processInstance = videoService.startWorkflow(new VideoInfo(videoRequest));
        return new ResultBean<String>(processInstance.getId(), ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMsg());
    }
}
