package com.stream.nz.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.stream.nz.model.ResultData;
import org.junit.jupiter.api.Test;

/**
 * @Author chenghao
 * @Date 2023/12/22 18:54
 */
public class FuzzySearchVinTest extends JwtTest {

    @Test
    public void fuzzySearchVinTest(){
        String url = "https://oversea.saicmaxus.com/australia/test/vehicle/fuzzy/queryVin?vin=LSKG5GL19CA211378&ascCode=4102019";
        HttpResponse execute = HttpRequest.get(url).header("jwt", jwt).execute();
        ResultData resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        System.out.println("result date :"+ JSON.toJSONString(resultData));
    }

}
