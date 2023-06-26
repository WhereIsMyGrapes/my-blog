package com.wwk.annotation;

import com.wwk.validator.CommentTypeValidator;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;
/**
 * 评论类型注解
 */
@Documented
@Constraint(validatedBy = {CommentTypeValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommentType {
    String message() default "{javax.validation.constraints.NotBlank.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return 评论类型
     */
    int[] values() default {};
}
