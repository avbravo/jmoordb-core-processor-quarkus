package com.jmoordb.core.processor.analizer;

import com.jmoordb.core.annotation.date.ExcludeTime;
import com.jmoordb.core.annotation.date.IncludeTime;
import com.jmoordb.core.annotation.repository.Count;
import com.jmoordb.core.annotation.repository.CountBy;
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

public class CountAnalizer {
// <editor-fold defaultstate="collapsed" desc="field()">

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
        CountAnalizer.message = message;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="analizer(ExecutableElement executableElement,TypeMirror typeEntity)">

    public static Boolean analizer(Count countAnnotation, Element element, ExecutableElement executableElement, TypeMirror typeEntity) {
        try {
            List<String> includeTimeFields = new ArrayList<>();
            List<String> excludeTimeFields = new ArrayList<>();
            String nameOfMethod = ProcessorUtil.nameOfMethod(executableElement);
            String nameOfEntity = ProcessorUtil.nameOfEntity(typeEntity);
            String typeOptional = "java.util.Optional<" + typeEntity.toString().trim() + ">";
            String typeList = "java.util.List<" + typeEntity.toString().trim() + ">";
            String typeSet = "java.util.Set<" + typeEntity.toString().trim() + ">";
            Boolean haveSearchParameters = Boolean.FALSE;
            /**
             *
             */

            /**
             * Imprimo el valor de la anotación Query
             */
            TypeMirror returnTypeOfMethod = executableElement.getReturnType();
            if (!returnTypeOfMethod.toString().equals("java.lang.Long")) {
                message = nameOfMethod + "() The return type must be java.lang.Long";

                return Boolean.FALSE;
            }

            List<? extends VariableElement> parameters = executableElement.getParameters();
            if (parameters.size() <= 0) {

                message = nameOfMethod + "() It must have at least one parameter of type Search... search";
                return Boolean.FALSE;
            } else {
                if (parameters.size() > MAXIMUM_NUMBER_OF_PARAMETERS) {
                    message = nameOfMethod + "() the maximum size of parameters allowed is " + MAXIMUM_NUMBER_OF_PARAMETERS + " . If you use more than" + MAXIMUM_NUMBER_OF_PARAMETERS + " use @QueryJSON instead.";
                    return Boolean.FALSE;
                }

                Boolean isIncludeTime = Boolean.FALSE;
                Boolean isExcludeTime = Boolean.FALSE;
                for (int i = 0; i < parameters.size(); i++) {

                    VariableElement param = parameters.get(i);
                    if (parameters.get(i).getAnnotation(IncludeTime.class) != null) {
                        if (param.asType().toString().equals("java.util.Date") || param.asType().toString().equals("java.time.LocalDateTime")) {
                        } else {
                            message = nameOfMethod + "() @IncludeTime should only be applied to Date or DateTime fields.";
                            return Boolean.FALSE;
                        }
                        includeTimeFields.add(param.getSimpleName().toString());
                    } else {
                        if (parameters.get(i).getAnnotation(ExcludeTime.class) != null) {
                            if (param.asType().toString().equals("java.util.Date") || param.asType().toString().equals("java.time.LocalDateTime")) {
                            } else {
                                message = nameOfMethod + "() @ExcludeTime should only be applied to Date or DateTime fields.";
                                return Boolean.FALSE;
                            }
                            excludeTimeFields.add(param.getSimpleName().toString());
                        }
                    }

                    // Verifica que el tipo del parámetro sea un tipo aceptado
                    if (!JmoordbCoreUtil.isJmoordbJavaTypeValid(param.asType().toString())) {
                        message = "Method " + nameOfMethod + "() parameter: " + param.getSimpleName().toString() + " Not a valid data type for a parameter.";
                        return Boolean.FALSE;
                    }
                    if (param.asType().toString().equals("com.jmoordb.core.model.Search[]") && param.asType().toString().indexOf("[]") != -1) {
                        haveSearchParameters = Boolean.TRUE;
                    }
                }
                if (parameters.size() == MAXIMUM_NUMBER_OF_PARAMETERS && (!haveSearchParameters)) {
                    message = nameOfMethod + "() only Search... search Parameters are allowed.";
                    return Boolean.FALSE;
                }
                if (!haveSearchParameters) {
                    message = nameOfMethod + "() It must have at least one parameter of type String.";
                    return Boolean.FALSE;
                }
            }

            if (!nameOfMethod.startsWith("count")) {
                message = nameOfMethod + "() should start with count";
                return Boolean.FALSE;
            }
            Count regexCount = executableElement.getAnnotation(Count.class);

            return Boolean.TRUE;
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return Boolean.FALSE;
    }
    // </editor-fold>

}
