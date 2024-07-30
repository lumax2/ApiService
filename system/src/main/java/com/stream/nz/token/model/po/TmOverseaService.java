package com.stream.nz.token.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TmOverseaService {

    private Long id;

    private String serviceCode;

    private String serviceSecret;

    private String ascCode;

    private Long ascId;

    private String sapAccount;

    private String ascName;

    private Date onlineDate;

    private String remark;

    private int isDel;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;
}
