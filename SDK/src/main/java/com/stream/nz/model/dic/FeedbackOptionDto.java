package com.stream.nz.model.dic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackOptionDto implements Serializable {

    private Long id;

    private String optionName;

    private String optionNameEn;

    private String optionCode;

    private String optionValue;

    private String optionValueEn;

    private String optionType;

    private String optionTypeValue;

    private String optionLevel;

    private String optionLevelValue;

    private String optionValueCode;

    private String parentOptionValueCode;

    private String parentOptionCode;

    private String parentOptionName;

    private String parentOptionNameEn;

    private String parentOptionValue;

    private String parentOptionValueEn;

    private String warnRule;

    private String warnRuleValue;

    private String optionDescribe;

}
