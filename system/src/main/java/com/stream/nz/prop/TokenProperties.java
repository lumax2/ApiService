package com.stream.nz.prop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenProperties {

    /**
     * 服务编码
     */
    private String serviceCode;

    /**
     * 服务秘钥
     */
    private String serviceSecret;

    /**
     * 服务token申请地址
     */
    private String serviceTokenApplyUrl;
}
