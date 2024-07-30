package com.stream.nz.model.fir;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author cheng hao
 * @Date 2023/12/28 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirCreateDto implements Serializable {

    private String title;
    private String creatorName;
    @JSONField(name = "vin")
    private String VIN;
    private String brand;
    private String modelYear;
    private String carLine;
    private String mileage;
    private String troubleLevel1;
    private String troubleLevel1Code;
    private String troubleLevel2;
    private String troubleLevel2Code;
    private String troubleLevel3;
    private String troubleLevel3Code;
    private String failedMode;
    private String failedModeCode;
    private String failPartCode;
    private String loseFunctionPartName;
    private String isAuthorized;
    private String troubleCode;
    private String symptomComplaint;
    private String corReactiveAction;
    private String probableCause;
    private String remark;
    private String caseGrade;

    private List<PbFeedbackAttachmentDto> pbList;

    private List<QualityReportAttachmentDto> attachmentVoList;

    private List<MiniAttachmentDto> liveSupportAttachment;

    private String managerClaimCode;

    private String managerClaimStatusName;

    private List<String> engineTypeCodes;

    private List<String> gearboxTypeCodes;

    private List<String> driveModeCodes;

    private List<String> entertainmentSystemCodes;

    private String caseClassification;
}
