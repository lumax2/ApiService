package com.stream.nz.config.converter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class StringToDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        if (StrUtil.isEmpty(source)) {
            return null;
        }

        return DateUtil.parseDateTime(source);
    }
}
