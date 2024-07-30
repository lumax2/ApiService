package com.stream.nz.token.controller;

import com.stream.nz.token.model.dto.ApiAuthDto;
import com.stream.nz.token.service.ApiAuthService;
import com.stream.nz.constant.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final ApiAuthService apiAuthService;

    @PostMapping("/base/api/token/register")
    public ResultData<String> queryOverseaService(@RequestBody ApiAuthDto apiAuthDto) throws Exception {
        String token = apiAuthService.registerApiToken(apiAuthDto);
        return new ResultData<String>(ResultData.SUCCESS,"register success",token);
    }

}
