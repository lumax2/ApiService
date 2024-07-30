package com.stream.nz.annotation;

/**
 * @Author cheng hao
 * @Date 2024/1/30 14:03
 */
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimestampValidator implements ConstraintValidator<ValidTimestamp, Long> {

    private int length;

    @Override
    public void initialize(ValidTimestamp constraintAnnotation) {
        this.length = constraintAnnotation.length(); // 初始化时获取长度
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return String.valueOf(value).length() == this.length; // 使用指定的长度进行校验
    }
}
