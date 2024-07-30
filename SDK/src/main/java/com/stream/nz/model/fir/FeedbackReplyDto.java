package com.stream.nz.model.fir;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: hao
 * @Date 2020/4/10 17:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackReplyDto implements Serializable {

    private Long id;

    private String feedbackId;

    private String reply;

    private String replyBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String replyTime;

    private String who;

    private String remark;

    private List<MiniAttachmentDto> attachmentList;

}
