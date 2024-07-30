package com.stream.nz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDecryptDto implements Serializable {

    private String toDecrypt;

    private String maxusJwt;
}
