package com.stream.nz.model.dic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDictionaryDto implements Serializable {

    private String optionCode;

    private String optionLevel;

    private List<FeedbackOptionDto> feedbackOptionDTOList;
}
