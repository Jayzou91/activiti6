package com.fish.activiti6.dto;

import com.fish.activiti6.request.VideoRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description:
 * @Author: Jayzou
 * @Date: 2019/5/9
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoInfo {
    private String videoName;
    private String uploader;

    public VideoInfo(VideoRequest request) {
        this.videoName = request.getVideoName();
        this.uploader = request.getUploader();
    }
}
