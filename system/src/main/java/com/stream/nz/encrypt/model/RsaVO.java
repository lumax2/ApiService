package com.stream.nz.encrypt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsaVO implements Serializable {

    //公钥指数 hex
    private String rsaPublicExponent;

    //模数 hex
    private String rsaModules;

    //私钥指数hex
    private String rsaPrivateExponent;

    // 公钥下发次数
    private Integer gainCount;

}
