package com.stream.nz.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ApiAuthDto implements Serializable {

    private String serviceCode;

    private String serviceSecret;

}
