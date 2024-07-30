package com.stream.nz.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class OverseaClaims implements Serializable {

    private String serviceCode;

    private String ascCode;

    private Long ascId;

    private String sapAccount;

    private String ascName;

    private String callBackUrl;

    private Date onlineDate;

    private String remark;

    private String expireTimeStamp;

    private RsaVO rsaVO;


}
