package com.jmoordb.core.processor.analizer;

import com.jmoordb.core.annotation.date.ExcludeTime;
import com.jmoordb.core.annotation.date.IncludeTime;
import com.jmoordb.core.processor.analizer.util.DeleteAnalizerUtil;
import com.jmoordb.core.annotation.repository.Delete;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class DeleteAnalizer {
// <editor-fold defaultstate="collapsed" desc="fields()">

    private static Integer MAXIMUM_NUMBER_OF_PARAMETERS = 5;
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
        DeleteAnalizer.message = message;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="analizer(ExecutableElement executableElement,TypeMirror typeEntity)">

    public static Boolean analizer(Delete deleteAnnotation, Element element, ExecutableElement executableElement, TypeMirror typeEntity, RepositoryMethod repositoryMethod) {
        try {
            List<String> includeTimeFields = new ArrayList<>();
            List<String> excludeTimeFields = new ArrayList<>();
            String nameOfMethod = ProcessorUtil.nameOfMethod(executableElement);
            String nameOfEntity = ProcessorUtil.nameOfEntity(typeEntity);
            String typeOptional = "java.util.Optional<" + typeEntity.toString().trim() + ">";
            String typeList = "java.util.List<" + typeEntity.toString().trim() + ">";
            String typeSet = "java.util.Set<" + typeEntity.toString().trim() + ">";
            Boolean havePaginationParameters = Boolean.FALSE;
            Boolean haveSortedParameters = Boolean.FALSE;
            Boolean haveSearchParameters = Boolean.FALSE;
            /**
             *
             */

            TypeMirror returnTypeOfMethod = executableElement.getReturnType();

            if (!returnTypeOfMethod.toString().equals("java.lang.Long")) {
                message = "Method " + nameOfMethod + "() The return type must be java.lang.Long";

                return Boolean.FALSE;
            }

            List<? extends VariableElement> parameters = executableElement.getParameters();
            if (parameters.size() <= 0) {

                message = "Method " + nameOfMethod + "() Must contain one or more parameters";
                return Boolean.FALSE;

            } else {
                if (parameters.size() > MAXIMUM_NUMBER_OF_PARAMETERS) {
                    message = "Method " + nameOfMethod + "() the maximum size of parameters allowed is " + MAXIMUM_NUMBER_OF_PARAMETERS + " . If you use more than" + MAXIMUM_NUMBER_OF_PARAMETERS + " use @DeleteLookup instead.";
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
                                                   message = nameOfMethod + "() @IncludeTime is a requirement for attributes of type Date or LocalDate to be used with @Query";
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
                    // El Search si se usa no debe ser opcional
                    if (param.asType().toString().equals("com.jmoordb.core.model.Search[]")) {
                        message = "Method " + nameOfMethod + "() parameter: " + param.asType().toString() + " Not a valid data type for a parameter.";
                        return Boolean.FALSE;
                    }
                    if (param.asType().toString().equals("com.jmoordb.core.model.Pagination")) {

                        message = "Method " + nameOfMethod + "() has a parameter of type Pagination but this is not allowed..";
                        return Boolean.FALSE;
                    }
                    if (param.asType().toString().equals("com.jmoordb.core.model.Sorted")) {
                        message = "Method " + nameOfMethod + "() has a parameter of type Sorted but this is not allowed..";
                        return Boolean.FALSE;
                    }
                    if (param.asType().toString().equals("com.jmoordb.core.model.Search")) {
                        haveSearchParameters = Boolean.TRUE;
                    }
                }

            }
            repositoryMethod.setIncludeTimeFields(includeTimeFields);
            repositoryMethod.setExcludeTimeFields(excludeTimeFields);
            if (!nameOfMethod.startsWith("delete")) {
                message = nameOfMethod + "() should start with delete";
                return Boolean.FALSE;
            }
            Delete delete = executableElement.getAnnotation(Delete.class);
            List<String> tokenList = new ArrayList<>();
            if (delete.where().trim().equals("")) {

                if (parameters.size() < 0 || parameters.size() > 1) {
                    message = "Method " + nameOfMethod + "() When using where() =\"\", you can only use a parameter of type Search..";
                    return Boolean.FALSE;
                }
                if (!haveSearchParameters) {
                    message = "Method " + nameOfMethod + "() When using where() = \"\" It is only allowed to use parameter of type Search";
                    return Boolean.FALSE;
                }
            } else {
                if (haveSearchParameters) {
                    message = "Method " + nameOfMethod + "() When using Search where must be empty where() = \"\".";
                    return Boolean.FALSE;
                }
                /**
                 * Verifica que no se inserten caracteres no validos No esta
                 * permitido ( ) [ ] $ y que contenga @
                 */
                String validDeleteMessage = DeleteAnalizerUtil.isValidDeleteCharacters(delete, parameters, nameOfMethod).trim();
                if (!validDeleteMessage.equals("")) {
                    message = validDeleteMessage;
                    return Boolean.FALSE;
                }

                /**
                 * Obtiene un List<String> de los tokens del where() de @Delete
                 */
                tokenList = Collections.list(new StringTokenizer(delete.where().trim(), " ")).stream()
                        .map(token -> (String) token)
                        .collect(Collectors.toList());

                if (!DeleteAnalizerUtil.isValidSizeComparative(tokenList.size(), parameters.size())) {
                    message = "Method " + nameOfMethod + "() There is no coincidence between the number of attributes and the parameters defined in the where().";
                    return Boolean.FALSE;
                }
                /**
                 *
                 */

                if (JmoordbCoreUtil.isPar(tokenList.size())) {
                    message = "Method " + nameOfMethod + "() The the number of  elements defined in the where() attribute are not correct..";
                    return Boolean.FALSE;
                }
                /**
                 * Verifica que el primer elemento del where() sea un
                 * identificador valido java
                 */
                if (!DeleteAnalizerUtil.nameOfJavaIdentifierValid(tokenList.get(0))) {
                    message = "Method " + nameOfMethod + "() The first attribute must be a valid Java identifier name.";
                    return Boolean.FALSE;
                }
                /**
                 * Valida las reglas comparando el where() con los paràmetros
                 */
                if (!DeleteAnalizerUtil.rulesValid(delete, tokenList, nameOfMethod, havePaginationParameters, haveSortedParameters)) {
                    message = "Method " + nameOfMethod + "() Check the where() attribute is not correctly defined..";
                    return Boolean.FALSE;
                }
                if (!DeleteAnalizerUtil.nameOfParametersEqualsToNameOfMethod(tokenList, parameters, nameOfMethod)) {
                    message = "Method " + nameOfMethod + "() The parameter names defined in where() do not match those declared in the method.";
                    return Boolean.FALSE;
                }
                /**
                 * Si se desea validar que el nombre del identificador coincida
                 * con el nombre de parametros habilite esta secccion.
                 */
                if (NAMEOFIDENTIFIER_EQUALS_NAMEOFPARAMETERSOFMETHOD) {
                    if (!DeleteAnalizerUtil.nameOfIdentificatorEqualsToNameOfMethod(tokenList, parameters, nameOfMethod)) {
                        message = "Method " + nameOfMethod + "() The identifier names defined in where() do not match those declared in the method.";
                        return Boolean.FALSE;
                    }
                }

            }

            /**
             * Actualiza RepositoryMethod
             */
            repositoryMethod.setWhere(delete.where().trim());
            repositoryMethod.setTokenWhere(tokenList);
            repositoryMethod.setWhereDescomposed(ProcessorUtil.generateWhereDescomposed(repositoryMethod));
//            repositoryMethod.setHavePagination(havePaginationParameters);
//            repositoryMethod.setHaveSorted(haveSortedParameters);
            return Boolean.TRUE;
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return Boolean.FALSE;
    }
    // </editor-fold>

}
