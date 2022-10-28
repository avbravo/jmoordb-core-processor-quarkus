package com.jmoordb.core.processor.analizer;

import com.jmoordb.core.annotation.date.ExcludeTime;
import com.jmoordb.core.annotation.date.IncludeTime;
import com.jmoordb.core.annotation.repository.Lookup;
import com.jmoordb.core.processor.methods.RepositoryMethod;
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

public class LookupAnalizer {
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
        LookupAnalizer.message = message;
    }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="analizer(ExecutableElement executableElement,TypeMirror typeEntity, RepositoryMethod repositoryMethod)">

    public static Boolean analizer(Lookup lookupAnnotation, Element element, ExecutableElement executableElement, TypeMirror typeEntity, RepositoryMethod repositoryMethod) {
        try {
            List<String> includeTimeFields = new ArrayList<>();
            List<String> excludeTimeFields = new ArrayList<>();
            String nameOfMethod = ProcessorUtil.nameOfMethod(executableElement);
            String nameOfEntity = ProcessorUtil.nameOfEntity(typeEntity);
            String typeOptionals = "java.util.Optional<" + typeEntity.toString().trim() + ">";
            String typeList = "java.util.List<" + typeEntity.toString().trim() + ">";
            String typeSet = "java.util.Set<" + typeEntity.toString().trim() + ">";
            String typeStream = "java.util.stream.Stream<" + typeEntity.toString().trim() + ">";
            Boolean havePaginationParameters = Boolean.FALSE;
            Boolean haveSortedParameters = Boolean.FALSE;

            /**
             *
             */
            TypeMirror returnTypeOfMethod = executableElement.getReturnType();

            if (!returnTypeOfMethod.toString().equals(typeList) && !returnTypeOfMethod.toString().equals(typeSet) && !returnTypeOfMethod.toString().equals(typeStream)) {
                message = nameOfMethod + "() The return type must be a List<" + nameOfEntity + "> or Optional<" + nameOfEntity + "> or Set<" + nameOfEntity + "> or Stream<" + nameOfEntity + ">";
                return Boolean.FALSE;
            }

            List<? extends VariableElement> parameters = executableElement.getParameters();
            if (parameters.size() <= 0) {

                message = nameOfMethod + "() must have a parameter of type Search.java.";
                return Boolean.FALSE;
            } else {
                if (parameters.size() > MAXIMUM_NUMBER_OF_PARAMETERS) {
                    message = nameOfMethod + "() the maximum size of parameters allowed is " + MAXIMUM_NUMBER_OF_PARAMETERS;
                    return Boolean.FALSE;
                }
                Boolean isIncludeTime = Boolean.FALSE;
                Boolean isExcludeTime = Boolean.FALSE;
                for (int i = 0; i < parameters.size(); i++) {

                    VariableElement param = parameters.get(i);
                    
                       /*
                    Valida que para @Query los atributos Date o DateTime utilicen @IncludeTime
                    se usa como sugerencia para que el desarrollador tenga presente su uso.
                    */
                    if (param.asType().toString().equals("java.util.Date") || param.asType().toString().equals("java.time.LocalDateTime")) {
                        if (parameters.get(i).getAnnotation(IncludeTime.class) == null) {
                                                   message = nameOfMethod + "() @IncludeTime is a requirement for attributes of type Date or LocalDate to be used with @Lookup";
                            return Boolean.FALSE;
                        }
                    }
                    
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
                    if (!param.asType().toString().equals("com.jmoordb.core.model.Search")) {
                        message = nameOfMethod + "() must have a parameter of type Search.java.";
                        return Boolean.FALSE;
                    }
                }

            }
            repositoryMethod.setIncludeTimeFields(includeTimeFields);
            repositoryMethod.setExcludeTimeFields(excludeTimeFields);
            if (!nameOfMethod.startsWith("lookup")) {
                message = nameOfMethod + "() should start with lookup";
                return Boolean.FALSE;
            }
            Lookup lookup = executableElement.getAnnotation(Lookup.class);

            return Boolean.TRUE;
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return Boolean.FALSE;
    }
    // </editor-fold>

}
