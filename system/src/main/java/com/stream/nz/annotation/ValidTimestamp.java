package com.stream.nz.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimestampValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimestamp {
    String message() default "Invalid timestamp";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int length(); // 添加一个指定长度的参数
}