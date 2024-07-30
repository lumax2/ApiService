package com.stream.nz.model.fir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: cheng hao
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MiniAttachmentDto implements Serializable {

    private String fileType;
    private String fileUrl;
    private String fileName;
    private String fileSize;
    private String createBy;
}
