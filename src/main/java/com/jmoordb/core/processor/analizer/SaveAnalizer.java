package com.jmoordb.core.processor.analizer;

import com.jmoordb.core.annotation.repository.Lookup;
import com.jmoordb.core.annotation.repository.Save;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class SaveAnalizer {
// <editor-fold defaultstate="collapsed" desc="fields()">

    private static Integer MAXIMUM_NUMBER_OF_PARAMETERS = 1;
    private static Boolean NAMEOFIDENTIFIER_EQUALS_NAMEOFPARAMETERSOFMETHOD = Boolean.FALSE;
    private Messager messager;
    private static String message = "";

    public Messager getMessager() {
        return messager;
    }

    public void setMessager(Messager messager) {
        this.messager = messager;
    }

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String message) {
        SaveAnalizer.message = message;
    }
// </editor-fold>
 // <editor-fold defaultstate="collapsed" desc="analizer(Save saveAnnotation, Element element, ExecutableElement executableElement,TypeMirror typeEntit)">
    public static Boolean analizer(Save saveAnnotation, Element element, ExecutableElement executableElement, TypeMirror typeEntity) {
        try {
            Boolean haveBooleanParameters = Boolean.FALSE;
            Boolean haveOptionalParameters = Boolean.FALSE;
         //
           List<String> includeTimeFields = new ArrayList<>();
   List<String> excludeTimeFields = new ArrayList<>();
            String nameOfMethod = ProcessorUtil.nameOfMethod(executableElement);
            String nameOfEntity = ProcessorUtil.nameOfEntity(typeEntity);
            String typeOptional = "java.util.Optional<" + typeEntity.toString().trim() + ">";
            String typeList = "java.util.List<" + typeEntity.toString().trim() + ">";
            String typeSet = "java.util.Set<" + typeEntity.toString().trim() + ">";

          

            TypeMirror returnTypeOfMethod = executableElement.getReturnType();
         
            if (!returnTypeOfMethod.toString().equals("java.lang.Boolean")) {
               
                if (!returnTypeOfMethod.toString().equals(typeOptional)) {
                 
                    message =  nameOfMethod + "() The return type must be a Boolean or Optional<" + nameOfEntity + ">";
                    return Boolean.FALSE;
                }

            }

            List<? extends VariableElement> parameters = executableElement.getParameters();
            if (parameters.size() <= 0) {
           
                message =  nameOfMethod + "() must have a parameter of type " + nameOfEntity;
                return Boolean.FALSE;
            } else {
                if (parameters.size() > MAXIMUM_NUMBER_OF_PARAMETERS) {
                    message =  nameOfMethod + "() the maximum size of parameters allowed is " + MAXIMUM_NUMBER_OF_PARAMETERS;
                    return Boolean.FALSE;
                }
 Boolean isIncludeTime = Boolean.FALSE;
                Boolean isExcludeTime = Boolean.FALSE;
                for (int i = 0; i < parameters.size(); i++) {
                    VariableElement param = parameters.get(i);

                
                    
                    // No se valida los demás de tipos de dato solo que sea un entity
                       // Verifica que el tipo del parámetro sea un tipo aceptado
//                    if (!JmoordbCoreUtil.isJmoordbJavaTypeValidQueryRegexCountDelete(param.asType().toString())) {
//                        message = "Method " + nameOfMethod + "() parameter: "  +  param.getSimpleName().toString()+ " Not a valid data type for a parameter.";
//                        return Boolean.FALSE;
//                    }
                    if (!param.asType().equals(typeEntity)) {
                        message =  nameOfMethod + "() must have a parameter of type " + ProcessorUtil.nameOfEntity(typeEntity);
                        return Boolean.FALSE;
                    }
                }

            }
 if (!nameOfMethod.startsWith("save") ) {
                message =  nameOfMethod + "() should start with save";
                return Boolean.FALSE;
            }
          Save save = executableElement.getAnnotation(Save.class);

            return Boolean.TRUE;
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return Boolean.FALSE;
    }
    // </editor-fold>

}
