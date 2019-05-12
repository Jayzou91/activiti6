package com.fish.activiti6.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
@Getter
@Setter
public class UploadVideoDO {
    private Long id;
    private String videoName;
    private String uploader;
    private Boolean isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String processInstanceId;
}
