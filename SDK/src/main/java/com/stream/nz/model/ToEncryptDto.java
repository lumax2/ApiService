package com.stream.nz.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToEncryptDto {

    private String toEncrypt;

    private String maxusJwt;
}
