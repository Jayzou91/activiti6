package com.fish.activiti6.service;

import com.fish.activiti6.dto.VideoInfo;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
public interface VideoService {

    ProcessInstance startWorkflow(VideoInfo info);
}
