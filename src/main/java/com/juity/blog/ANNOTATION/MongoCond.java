package com.juity.blog.ANNOTATION;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MongoCond {
    int value() default 1;
}
