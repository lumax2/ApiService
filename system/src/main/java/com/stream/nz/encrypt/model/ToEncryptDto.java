package com.stream.nz.encrypt.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToEncryptDto {

    @NotBlank(message = "要解密字符串不能为空")
    private String toEncrypt;

    @NotBlank(message = "用户信息不能为空")
    private String maxusJwt;
}
