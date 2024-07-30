package com.stream.nz.model;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ResultData<T> implements Serializable{

        private static final long serialVersionUID = -1L;

        private int status;

        private String errMsg;

        private T data;

        public static final int SUCCESS = 1;

        public static final int FAILE = 0;
}
