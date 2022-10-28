/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.jmoordb.core.annotation.controller;

import com.jmoordb.core.annotation.controller.enumerations.ProducesType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author avbravo
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface APIDocumentation {

    String code() default "";

    String description() default "";

    String name() default "";

    ProducesType producesType() default ProducesType.JSON;
    
     boolean required() default false;
     boolean readOnly() default false;
 public Class<?> implementation() default Void.class;


}
