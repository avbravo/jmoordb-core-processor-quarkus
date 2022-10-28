/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jmoordb.core.annotation.date;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author avbravo
 */

@Retention(RUNTIME)
@Target({ FIELD, PARAMETER })
public @interface DateFormat {
  public static final String DEFAULT_DATE = "dd-MM-yyyy";
  String value() default DEFAULT_DATE;
    
}
