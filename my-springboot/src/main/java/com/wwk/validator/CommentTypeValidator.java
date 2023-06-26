package com.wwk.validator;

import com.wwk.annotation.CommentType;
import io.swagger.models.auth.In;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 评论类型校验器
 *
 * @author WWK
 * @program: my-springboot
 */
public class CommentTypeValidator implements ConstraintValidator<CommentType, Integer> {

    private final Set<Integer> set = new HashSet<>();

    /**
     * 初始化
     *
     * @param constraintAnnotation 评论类型注解
     */
    @Override
    public void initialize(CommentType constraintAnnotation) {
        for(int value : constraintAnnotation.values()) {
            set.add(value);
        }
    }

    /**
     * 校验方法
     *
     * @param value                      校验的值
     * @param constraintValidatorContext 上下文
     * @return 是否校验成功
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(value);
    }
}
