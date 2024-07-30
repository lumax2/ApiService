package com.stream.nz.token.dao;

import com.stream.nz.token.model.po.TmOverseaService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TmOverseaServiceMapper {

    TmOverseaService queryByServiceCode(@Param("serviceCode")String serviceCode);
}