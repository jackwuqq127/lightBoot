package pers.wuchao.action.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
	String name() default "";
	String[] urlPatterns() default "";
}
