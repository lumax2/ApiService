package com.stream.nz.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.stream.nz.model.ResultData;
import com.stream.nz.model.fir.QualityReportDetailDto;
import org.junit.jupiter.api.Test;

/**
 * @Author cheng hao
 * @Date 2024/1/2 17:06
 */
public class FirDetailQueryTest extends JwtTest {


    @Test
    public void queryFirDetail() {
        String firId = "1564707596100981";
        String url = "https://oversea.saicmaxus.com/australia/test/fir/detail/query?id=" + firId;
        HttpResponse execute = HttpRequest.get(url).header("jwt", jwt).execute();
        ResultData resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        JSONObject data = (JSONObject) resultData.getData();
        QualityReportDetailDto qualityReportDetailVo = JSON.parseObject(data.toJSONString(), QualityReportDetailDto.class);
        System.out.println(qualityReportDetailVo.toString());
    }

}