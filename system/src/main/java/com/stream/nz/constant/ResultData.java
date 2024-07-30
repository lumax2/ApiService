package com.stream.nz.constant;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResultData<T> implements Serializable{

        private int status;

        private String errMsg;

        private String errCode;

        private T data;

        public static final int SUCCESS = 1;

        public static final int FAILE = 0;

        public ResultData(int status, String errMsg, T data) {
                this.status = status;
                this.errMsg = errMsg;
                this.data = data;
        }
}
