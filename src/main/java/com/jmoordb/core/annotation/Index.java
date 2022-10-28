package com.jmoordb.core.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
    String name() default "";
    String[] fieldNames() default {};
    
    /**
     * Ejemplo de uso
    * Genera el indice
       @Index(
        name ="oceanoFK"
        fieldNames = {"idoceano", "oceano"},         
    )
    */
}
