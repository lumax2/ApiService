package com.stream.nz.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.stream.nz.model.ApiAuthDto;
import com.stream.nz.model.ResultData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JwtTest {

    public static String jwt;

    @BeforeAll
    public static void getToken() {
        String url = "https://www.streamnz.com/australia/test/base/api/token/register";
        ApiAuthDto param = new ApiAuthDto("Ateco","12291981916047848820127885506756957297608117951493822010585831243415634497772067063999895178023712880459677788684659232506540991053364851419402479561975719");
        HttpResponse execute = HttpRequest.post(url).body(JSONUtil.toJsonStr(param)).execute();
        ResultData resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        jwt = null==resultData.getData()?null:String.valueOf(resultData.getData());
       System.out.println("result date :"+JSON.toJSONString(resultData));
    }

    @Test
    public void testCallWithToken(){
        String url = "https://www.streamnz.com/australia/test/epc/mapping/lnk?brand=abc";
        HttpResponse execute = HttpRequest.get(url).header("jwt", jwt).execute();
        ResultData resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        System.out.println("result date :"+ JSON.toJSONString(resultData));
    }


}
