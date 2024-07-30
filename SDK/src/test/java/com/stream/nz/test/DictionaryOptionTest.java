package com.stream.nz.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.stream.nz.model.ResultData;
import com.stream.nz.model.dic.DictionaryDto;
import com.stream.nz.model.dic.FeedbackDicDto;
import com.stream.nz.model.dic.OptionDictionaryDto;
import org.junit.jupiter.api.Test;

import java.util.List;


/**
 * @Author cheng hao
 * @Date 2024/1/3 14:20
 */
public class DictionaryOptionTest extends JwtTest {

    @Test
    public void queryDicInfoByTypeCodeTest()  {
        String url = "https://oversea.saicmaxus.com/australia/test/fir/dictionaryInfo/queryByTypeCode?typeCode=1033";
        HttpResponse execute = HttpRequest.get(url).header("jwt", jwt).execute();
        ResultData<List<FeedbackDicDto>> resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        List<FeedbackDicDto> list = resultData.getData();
        System.out.println(list.toString());
    }

    @Test
    public void queryAllDictionaryTest()  {
        String url = "https://oversea.saicmaxus.com/australia/test/fir/dictionary/all/query";
        HttpResponse execute = HttpRequest.get(url).header("jwt", jwt).execute();
        ResultData<List<DictionaryDto>> resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        List<DictionaryDto> all = resultData.getData();
        System.out.println(all.toString());
    }

    @Test
    public void queryAllOptionTest()  {
        String url = "https://oversea.saicmaxus.com/australia/test/fir/option/all/query";
        HttpResponse execute = HttpRequest.get(url).header("jwt", jwt).execute();
        ResultData<List<OptionDictionaryDto>> resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        List<OptionDictionaryDto> all = resultData.getData();
        System.out.println(all.toString());
    }

}
