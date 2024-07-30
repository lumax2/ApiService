package com.stream.nz.websocketClient;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.stream.nz.constant.ResultData;
import com.stream.nz.token.model.dto.ApiAuthDto;
import org.junit.jupiter.api.BeforeAll;

/**
 * @Author cheng hao
 * @Date 2024/1/2 15:41
 */
public class MaxusJwtTest {


    public static String jwt;

    @BeforeAll
    public static void getMaxusToken() {
        String url = "http://localhost:9500/base/api/token/register";
        ApiAuthDto param = new ApiAuthDto("Ateco","12291981916047848820127885506756957297608117951493822010585831243415634497772067063999895178023712880459677788684659232506540991053364851419402479561975719");
        HttpResponse execute = HttpRequest.post(url).body(JSONUtil.toJsonStr(param)).execute();
        ResultData resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        jwt = null==resultData.getData()?null:String.valueOf(resultData.getData());
        System.out.println("result date :"+jwt);
    }
}
