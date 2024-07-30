package com.stream.nz.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.stream.nz.model.ResultData;
import com.stream.nz.model.fir.FirCreateDto;
import org.junit.jupiter.api.Test;

/**
 * @Author cheng hao
 * @Date 2023/12/22 18:54
 */
public class FirSubmitTest extends JwtTest {

    @Test
    public void firSubmit(){
        String url = "https://oversea.saicmaxus.com/australia/test/fir/new/submit";
        String body = "{\"ascCode\":\"4102028\",\"ascName\":\"上海怡通浦东分公司\",\"attachmentVoList\":[{\"attachmentVoList\":[{\"fileUrl\":\"https://cdn3.maxuscloud.com/xyxZz/wx_mbd_uat/order/uat/202401/251886ff-11cd-4388-a9dc-8e9ca4d98afd.jpg\",\"thumbFileUrl\":\"https://cdn3.maxuscloud.com/xyxZz/wx_mbd_uat/thumb_file/order/uat/202401/251886ff-11cd-4388-a9dc-8e9ca4d98afd_120x120.jpg\",\"businessCode\":\"344d1213-c012-4e22-9f37-0a6bce2f0fc5\",\"fileName\":\"4801703142285_.pic.jpg\",\"fileType\":\"jpg\",\"fileSize\":138435}],\"busiTag\":\"10191004\"}],\"carLine\":\"V80\",\"channel\":\"海外\",\"city\":\"上海市\",\"cityCode\":\"310100\",\"claimStatus\":\"0\",\"clientAddr\":null,\"clientEmail\":null,\"clientMobile\":null,\"clientName\":null,\"corReactiveAction\":\"*Solution\",\"creator\":10000000005022,\"creatorName\":\"程浩\",\"failPartCode\":\"C00027671\",\"failedMode\":\"AC failure\",\"failedModeCode\":\"10121026\",\"insuranceStartTime\":null,\"isAuthorized\":\"1\",\"isOversea\":\"1\",\"loseFunctionPartName\":\"TRANSITION ASM-SLDG DR LAT\",\"mileage\":\"100\",\"mobile\":\"15901945253\",\"pbList\":[{\"attachmentVoList\":[],\"pbDesc\":\"\",\"pbAuthorizedMoney\":\"\"}],\"probableCause\":null,\"processStatus\":null,\"purchaseDate\":null,\"remark\":null,\"roadCondition\":\"13011002\",\"symptomComplaint\":\"*Fault Symptom\\n\",\"title\":\"TestSubmitFir1\",\"troubleCode\":null,\"troubleLevel1\":\"Engine\",\"troubleLevel2\":\"Engine machinery - crank linkage\",\"troubleLevel3\":\"Cylinder block/ crankcase assembly\",\"troubleLevel1Code\":\"20011001\",\"troubleLevel2Code\":\"20011014\",\"troubleLevel3Code\":\"20011090\",\"vehicleLoad\":\"12011003\",\"vehicleSpeed\":\"11011002\",\"vin\":\"LSKG5GC17JA071702\",\"weather\":\"14011001\",\"worksheetEndDate\":null,\"worksheetStartDate\":\"2024-01-02 00:00:00\",\"engineNo\":null,\"country\":\"中国\",\"countryCode\":\"10000\",\"province\":\"上海市\",\"provinceCode\":\"310000\",\"tacType\":\"1\",\"feedbackId\":null,\"brand\":null,\"modelYea\":null,\"positionCode\":10046789,\"positionName\":\"华东一区-上海\",\"secondAscCode\":null,\"secondAscName\":null,\"managerClaimStatusName\":null,\"managerClaimCode\":null,\"caseClassification\":\"Warranty\"}";
        FirCreateDto vo = JSONObject.parseObject(body, FirCreateDto.class);
        HttpResponse execute = HttpRequest.post(url).body(JSON.toJSONString(vo)).header("jwt", jwt).execute();
        ResultData resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        Object data = resultData.getData();
        System.out.println("result date :firId:"+ data);
    }

    @Test
    public void queryFirDetail(){
        String firId = "1564707596100981";
        String url = "https://oversea.saicmaxus.com/australia/test/fir/detail/query?id="+firId;
        HttpResponse execute = HttpRequest.get(url).header("jwt", jwt).execute();
        ResultData resultData = JSONObject.parseObject(execute.body(), ResultData.class);
        Object data = resultData.getData();
        System.out.println("result date :"+ JSON.toJSONString(data));
    }

}
