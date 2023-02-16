package com.example.demo.common.elog;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Elog {
    String value() default "日志";
    String target() default "file";
}
