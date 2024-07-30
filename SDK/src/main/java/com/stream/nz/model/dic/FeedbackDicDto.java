package com.stream.nz.model.dic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDicDto implements Serializable {

    private Long id;

    private String dictionaryCode;

    private String dictionaryName;

    private String dictionaryNameEn;

    private String typeCode;

    private String typeName;

}