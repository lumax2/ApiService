package com.stream.nz.model.fir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: cheng hao
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QualityReportAttachmentDto implements Serializable {

    private List<MiniAttachmentDto> attachmentVoList;

    private String busiTag;

}
