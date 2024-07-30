package com.stream.nz.token.support;

import com.stream.nz.token.dao.TmOverseaServiceMapper;
import com.stream.nz.token.model.po.TmOverseaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceSupport {

    private final TmOverseaServiceMapper overseaServiceDao;

    public TmOverseaService queryByServiceCode(String serviceCode){
        TmOverseaService tmOverseaService = overseaServiceDao.queryByServiceCode(serviceCode);
        return tmOverseaService;
    }
}
