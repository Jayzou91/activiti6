package com.fish.activiti6.dao;

import com.fish.activiti6.domain.UploadVideoDO;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
public interface VideoMapper {

    int insertUploadVideo(UploadVideoDO uploadVideoDO);

    int updateUploadVideo(UploadVideoDO uploadVideoDO);

}
