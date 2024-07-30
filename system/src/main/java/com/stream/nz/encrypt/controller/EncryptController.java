package com.stream.nz.encrypt.controller;


import com.stream.nz.constant.ResultData;
import com.stream.nz.encrypt.model.RsaVO;
import com.stream.nz.encrypt.model.ToDecryptDto;
import com.stream.nz.encrypt.model.ToEncryptDto;
import com.stream.nz.encrypt.service.RsaService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EncryptController {

    private final RsaService rsaService;

    /**
     * 动态获取RSA公钥因子和模数
     * @param jwt
     * @return
     */
    @GetMapping(value = "/rsa/getPubRsa")
    public ResultData<RsaVO> getRsaKeyPair(@RequestParam("jwt") String jwt) {
        return new ResultData<RsaVO>(ResultData.SUCCESS,"get rsa pair success",rsaService.createRsaModule(jwt));
    }

    /**
     * 使RSA公钥失效
     * @param jwt
     * @return
     */
    @GetMapping(value = "/rsa/disable")
    public ResultData<Boolean> disableRsaKeyPair(@RequestParam("jwt") String jwt)  {
        return new ResultData(ResultData.SUCCESS,"disable rsa pair success",rsaService.disableRsa(jwt));
    }

    /**
     * 私钥加密
     * @param toEncryptDto
     * @return
     */
    @RequestMapping(value = "/rsa/private/encrypt" ,method = RequestMethod.POST)
    public ResultData<String> encryptByPrivate(@RequestBody @Validated ToEncryptDto toEncryptDto)  {
        return new ResultData(ResultData.SUCCESS,"私钥加密成功",rsaService.encryptByPrivate(toEncryptDto));
    }

    /**
     * 私钥解密
     * @param toDecryptDto
     * @return
     */
    @RequestMapping(value = "/rsa/private/decrypt" ,method = RequestMethod.POST)
    public ResultData<String> decryptByPrivate(@RequestBody @Validated ToDecryptDto toDecryptDto)  {
        return new ResultData(ResultData.SUCCESS,"私钥解密成功",rsaService.decryptByPrivate(toDecryptDto));
    }

    /**
     * 公钥加密-模拟sdk客户端公钥加密-业务服务器开发测试用
     * @param toEncryptDto
     * @return
     */
    @RequestMapping(value = "/rsa/public/encrypt" ,method = RequestMethod.POST)
    public ResultData<String> encryptByPublic( @RequestBody @Validated ToEncryptDto toEncryptDto)  {
        return new ResultData(ResultData.SUCCESS,"公钥加密成功",rsaService.encryptByPublic(toEncryptDto));
    }

    /**
     * 公钥解密-模拟sdk客户端公钥解密-业务服务器开发测试用
     * @param toDecryptDtor
     * @return
     */
    @RequestMapping(value = "/rsa/public/decrypt" ,method = RequestMethod.POST)
    public ResultData<String> decryptByPublic( @RequestBody @Validated ToDecryptDto toDecryptDtor)  {
        return new ResultData(ResultData.SUCCESS,"公钥解密成功",rsaService.decryptByPublic(toDecryptDtor));
    }
}
