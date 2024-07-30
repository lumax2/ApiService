package com.stream.nz.model.fir;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: hao
 * @Date 2020/4/10 16:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityReportDetailDto implements Serializable {


    // fir detail ********************************************************************
    //  case_id
    private String caseId;
    // tm_inf_feedback crate_time
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    // tm_inf_feedback title
    private String title;
    // tm_inf_feedback ascName
    private String ascName;
    // tm_inf_feedback ascCode
    private String ascCode;
    // tm_inf_feedback second_Asc_Name
    private String secondAscName;
    // tm_inf_feedback second_Asc_code
    private String secondAscCode;

    // tm_inf_feedback creator
    private String creator;
    // tm_info_user name 填表人
    private String creatorName;

    private String fprCategory;
    // tm_inf_feedbackfpr CREATE_TIME
    private String fprCreateTime;

    // 车辆基本信息 ********************************************************************

    // tm_inf_feedback VIN
    @JSONField(name = "vin")
    private String VIN;
    // tm_inf_feedback carLine
    private String carLine;
    // tm_inf_feedback mileage
    private String mileage;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insuranceStartTime;
    // TM_INF_VEHICLE ENGINE_NO
    private String engineNo;
    // TM_INF_VEHICLE CSN_NO
    private String csnNo;
    // TM_INF_VEHICLE  PURCHASE_DATE
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date onlineDate;
    // tm_inf_feedback create_time
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date worksheetStartDate;
    // tm_inf_feedback close_time
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date worksheetEndDate;
    // 问题描述 ********************************************************************
    // TM_INF_FEEDBACK trouble_level1
    private String troubleLevel1;
    private String troubleLevel1Code;
    //  TM_INF_FEEDBACK trouble_level2
    private String troubleLevel2;
    private String troubleLevel2Code;
    //  TM_INF_FEEDBACK trouble_level3
    private String troubleLevel3;
    private String troubleLevel3Code;
    //  TM_INF_FEEDBACK FAILEDMODE
    private String failedMode;
    private String failedModeCode;
    //  TM_INF_FEEDBACK FAULT_PARTS_CODE 失效零件号（主损件）
    private String failPartCode;
    // TM_INF_FEEDBACK part_name 失效零件名称
    private String loseFunctionPartName;

    //  TM_INF_FEEDBACK VEHICLE_SPEED 车速
    private String vehicleSpeed;
    private String vehicleSpeedName;
    private String vehicleSpeedNameEn;
    // TM_INF_FEEDBACK vehicle_load 车载
    private String vehicleLoad;
    private String vehicleLoadName;
    private String vehicleLoadNameEn;

    // TM_INF_FEEDBACK  ROAD_CONDITION 路况
    private String roadCondition;
    private String roadConditionName;
    private String roadConditionNameEn;
    // TM_INF_FEEDBACK  WEATHER 天气
    private String weather;
    private String weatherName;
    private String weatherNameEn;

    
    // TM_INF_FEEDBACK IS_AUTHORIZED is_pre_claim 是否申请预授权 0=no;1=yes
    private String isAuthorized;
    
    // TM_INF_FEEDBACK claim_status 是否申请预授权 0=未审批;1=通过;2=拒绝
    private String claimStatus;
    //回复自动结案，0是，1否
    private String autoEndCase;

    // TM_INF_FEEDBACK TROUBLE_CODE 诊断仪故障码
    private String troubleCode;

    // TM_INF_FEEDBACK  SYMPTOM_COMPLAINT 故障症状
    private String symptomComplaint;

    // TM_INF_FEEDBACK CORRECTIVE_ACTION 解决方案
    private String corReactiveAction;

    // TM_INF_FEEDBACK PROBABLE_CAUSE 维修经过
    private String probableCause;

    // TM_INF_FEEDBACK remark  备注
    private String remark;

    private String  caseGrade;

    private Date closeTime;

    // 问题列表
    private List<PbFeedbackAttachmentDto> pbList;

    // 附件
    private List<QualityReportAttachmentDto> attachmentVoList;

    // TAC与ASC信息交互 ********************************************************************
    // TAC与ASC信息交互
    private List<FeedbackReplyDto> replies;

    //TAC是否回复,yes,no
    private String isTacReplied;

    // 结案说明 ********************************************************************
    // TM_INF_TAC_FEEDBACK TAC_CONCLUSION
    private String conclusion;


    // TAC现场技术支持 ********************************************************************

    // TM_INF_TAC_FEEDBACK LIVE_STATUS 现场支持
    private String liveStatus;
    // TM_INF_TAC_FEEDBACK LIVE_SUPPORTOR 支持人员
    private String liveSupportor;
    // TM_INF_TAC_FEEDBACK LIVE_SUPPORT_REMARK 结果评价
    private String liveSupportRemark;
    // 现场支持附件
    private List<MiniAttachmentDto> liveSupportAttachment;
    
    // 案例评估 ********************************************************************
    //TM_INF_TAC_FEEDBACK TAC_CREATOR 登记人
    private String tacCreator;

    //  TM_INF_TAC_FEEDBACK SUGGEST_STATUS 建议有效
    private String suggestStatus;

    // TM_INF_TAC_FEEDBACK ONE_SUGGEST_STATUS 一次建议有效
    private String oneSuggestStatus;

    // 开启天数 (导出TAC时间到结案时间的日期间隔；如两个操作是同一天则显示一天)
    // tacClosedTime-tacCreateTime
    private Integer tacDealDays;

    // 解决天数 (导出TAC时间到选择的解决时间的日期间隔，如两个操作是同一天则显示一天 )
    // tacFinishedTime-tacCreateTime
    private Integer solveTacDays;

    // TM_INF_TAC_FEEDBACK CLOSED_KIND  关闭类型
    private String closedKind;

    // TM_INF_TAC_FEEDBACK TAC_FINISHED_TIME TAC完成日期 解决时间
    private String tacFinishedTime;
    // TM_INF_TAC_FEEDBACK TAC_CREATE_TIME TAC创建日期  导出TAC案例时间
    private String tacCreateTime;
    // TM_INF_TAC_FEEDBACK TAC_CLOSED_TIME TAC关闭日期 结案时间
    private String tacClosedTime;

    // 查询附件用 主键id tm_inf_feedback id tm_inf_tac_feedback ########################################################################################
    private String  feedbackId;
    private String TACFeedbackId;

    private String purchaseDate;
    private String clientName;
    private String clientMobile;
    private String clientEmail;
    private String clientAddr;

    private String processStatus;

    private String processStatusEn;

    private String processStatusCode;

    private String caseStatus;

    private String exportTacStatus;


    // 状态释义见 tm_inf_feedback_dictionary 三包预警级别
    private String threeGuaranteePolicyLevel;

    private String isOversea;
    private String isRv;

    private String productNameCn;

    private String productNameEn;

    private String managerClaimCode;

    private String managerClaimStatus;

    private String vehicleUse;

    private String num;
    private String country;
    private String countryCode;
    private String province;
    private String provinceCode;
    private String city;
    private String cityCode;

    // 渠道 国内还是海外 isOversea
    private String channel;

    // 1 是TAC案例 0 不是TAC案例 TAC类型
    private String tacType;

    private String fprLevel;
    //failPartCode
    private String loseFunctionPartCode;

    // 对应list threeGuaranteePolicyLevel
    private String threeGuaranteeCode;
    // mobile
    private String cellphone;

    private String submitTime;

    private String cloudOrderNumber;

    private List<DictionaryInfoDto> dictionaryInfos;

    private String createBy;
    private String caseClassification;


}
