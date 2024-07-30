package com.stream.nz.model.dic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryDto {

    private String typeCode;

    private List<FeedbackDicDto> dictionaryList;
}
