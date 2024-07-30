package com.stream.nz.token.model.dto;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.stream.nz.encrypt.model.RsaVO;
import com.stream.nz.token.model.po.TmOverseaService;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

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

    public OverseaClaims(TmOverseaService tm, RsaVO rsaVO){
        BeanUtils.copyProperties(tm, this);
        // 获取当前时间
        Date currentDate = DateUtil.date();
        // 获取3分钟后的时间
        Date threeMinutesLater = DateUtil.offsetMinute(currentDate, 3);
        // 获取3分钟后的时间戳
        long timestamp = threeMinutesLater.getTime();
        this.expireTimeStamp = String.valueOf(timestamp);
        this.rsaVO = rsaVO;
    }

    public JSONObject toJSONObject(){
        return (JSONObject) JSON.toJSON(this);
    }

}
