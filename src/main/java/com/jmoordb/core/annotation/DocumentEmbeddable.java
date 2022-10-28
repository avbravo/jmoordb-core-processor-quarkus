/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmoordb.core.annotation;

import com.jmoordb.core.annotation.enumerations.JakartaSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author andreax64@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DocumentEmbeddable {
    String collection() default "";
    String database() default "{mongodb,database}";
    JakartaSource jakartaSource() default JakartaSource.JAKARTA;

}
