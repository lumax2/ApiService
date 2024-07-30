package com.stream.nz.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.stream.nz.model.ResultData;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * @Author cheng hao
 * @Date 2024/1/2 18:05
 */
public class FileUploadTest extends JwtTest {


    @Test
    public void uploadFile() throws IOException {
        String url = "https://oversea.saicmaxus.com/australia/test/fir/file/upload";
        String directoryPath = "file/OverseaQualityArctech.png";
        File file = FileUtil.file(directoryPath);
        HttpResponse execute = HttpRequest.post(url).header("jwt", jwt).form("file", file).contentType("multipart/form-data").execute();
        ResultData resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        Object data = resultData.getData();
        System.out.println("result date :"+ JSON.toJSONString(data));
    }


}
