package com.stream.nz.token.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ApiAuthDto implements Serializable {

    @NotNull(message = "serviceCode is empty")
    private String serviceCode;

    @NotNull(message = "service secret is empty")
    private String serviceSecret;

}
