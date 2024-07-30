package com.stream.nz.model.fir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryInfoDto implements Serializable {

    private Long id;

    private String code;

    private String value;

    private String valueEn;

    private Long typeId;

    private String typeCode;

    private String typeName;

    private Integer level;

    private Long parentId;

    private String path;


}
