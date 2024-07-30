package com.stream.nz.config;

import com.github.pagehelper.Page;
import com.stream.nz.constant.ResponseDataConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;

@Getter
@Setter
@Accessors(chain = true)
public class ResponseData<T> {

    private Integer status = ResponseDataConstant.STATUS_SUCCESS;

    private String code = ResponseDataConstant.CODE_SUCCESS;

    private String msg;

    private T data;

    private Long total;

    public ResponseData<T> setTotal(Object obj) {
        if (obj instanceof Page<?>) {
            this.total = ((Page<?>) obj).getTotal();
        } else if (obj instanceof Collection<?>) {
            this.total = (long) ((Collection<?>) obj).size();
        }else if (obj instanceof Long) {
            this.total = (long) obj;
        } else if (obj instanceof Integer) {
            this.total = Long.valueOf((Integer) obj);
        }
        return this;
    }

    public ResponseData() {
    }

    public ResponseData(String code,String msg,Long total, T data){
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.data = data;
    }

    /**
     * 返回成功
     *
     * @return 成功状态的响应体
     * @param <T> 数据类型
     */
    public static <T> ResponseData<T> success(){
        return succ(null);
    }

    public static <T> ResponseData<T> succ(T data){
        return new ResponseData<>(ResponseDataConstant.CODE_SUCCESS,"处理成功",0L,data);
    }
}
