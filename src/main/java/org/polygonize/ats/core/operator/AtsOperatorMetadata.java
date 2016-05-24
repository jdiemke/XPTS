package org.polygonize.ats.core.operator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AtsOperatorMetadata {
    String operatorDesignation() default "";

    String category() default "plugins";

    String sourceOfSupply() default "";

    String author() default "";

    String date() default "";

    String description() default "";

    String version() default "";
}
