/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jmoordb.core.annotation.controller;

import com.jmoordb.core.annotation.controller.enumerations.ProducesType;
import com.jmoordb.core.annotation.controller.enumerations.ResponseType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 *
 * @author avbravo
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Rest {
  String  path() ;
  String description() default "";
  ResponseType responseType() default ResponseType.GET;
  ProducesType[] producesType();
 //  MediaType[] produces() default [MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON];
  APIDocumentation[]  apiDocumentation() default {@APIDocumentation(code="",description = "")} ;
  /**
   * {method.similar} Indica que la firma del metodo del repositorio es la misma que el metodo declarado
   * {fima_methodo_repositorio} --> Es la firma del metodo del repositorio que se usara.
   * @return 
   */

  String repositoryMethod() default "{method.similar}";
}
