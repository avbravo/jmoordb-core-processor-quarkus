package com.jmoordb.core.processor.analizer;

import com.jmoordb.core.annotation.date.IncludeTime;
import com.jmoordb.core.processor.analizer.util.RegexAnalizerUtil;
import com.jmoordb.core.annotation.repository.Regex;
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

public class RegexAnalizer {
// <editor-fold defaultstate="collapsed" desc="fields()">

    private static Integer MAXIMUM_NUMBER_OF_PARAMETERS = 2;
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
        RegexAnalizer.message = message;
    }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="analizer(ExecutableElement executableElement,TypeMirror typeEntity, RepositoryMethod repositoryMethod)">

    public static Boolean analizer(Regex regexAnnotation, Element element, ExecutableElement executableElement, TypeMirror typeEntity, RepositoryMethod repositoryMethod) {
        try {
            List<String> includeTimeFields = new ArrayList<>();
            List<String> excludeTimeFields = new ArrayList<>();
            String nameOfMethod = ProcessorUtil.nameOfMethod(executableElement);
            String nameOfEntity = ProcessorUtil.nameOfEntity(typeEntity);
            String typeOptional = "java.util.Optional<" + typeEntity.toString().trim() + ">";
            String typeList = "java.util.List<" + typeEntity.toString().trim() + ">";
            String typeSet = "java.util.Set<" + typeEntity.toString().trim() + ">";
            String typeStream = "java.util.stream.Stream<" + typeEntity.toString().trim() + ">";
            Boolean havePaginationParameters = Boolean.FALSE;
            Boolean haveStringParameters = Boolean.FALSE;
            /**
             *
             */
            /**
             * Imprimo el valor de la anotación Regex
             *
             * /**
             * Imprimo el valor de la anotación Query
             */

            TypeMirror returnTypeOfMethod = executableElement.getReturnType();

            if (!returnTypeOfMethod.toString().equals(typeList) && !returnTypeOfMethod.toString().equals(typeSet) && !returnTypeOfMethod.toString().equals(typeStream)) {
                message = nameOfMethod + "() The return type must be a List<" + nameOfEntity + "> or Optional<" + nameOfEntity + "> or Set<" + nameOfEntity + "> or Stream<" + nameOfEntity + ">";
                return Boolean.FALSE;
            }
            List<? extends VariableElement> parameters = executableElement.getParameters();
            Integer controlPaginationSorted = 0;
            if (parameters.size() <= 0) {

                message = nameOfMethod + "() It must have at least one parameter of type String.";
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
   /*
                    Valida que para @Query los atributos Date o DateTime utilicen @IncludeTime
                    se usa como sugerencia para que el desarrollador tenga presente su uso.
                    */
                    if (param.asType().toString().equals("java.util.Date") || param.asType().toString().equals("java.time.LocalDateTime")) {
                        if (parameters.get(i).getAnnotation(IncludeTime.class) == null) {
                                                   message = nameOfMethod + "() @IncludeTime is a requirement for attributes of type Date or LocalDate to be used with @Regex";
                            return Boolean.FALSE;
                        }
                    }
                    
                    // Verifica que el tipo del parámetro sea un tipo aceptado
                    if (!JmoordbCoreUtil.isJmoordbJavaTypeValid(param.asType().toString())) {
                        message = "Method " + nameOfMethod + "() parameter: " + param.getSimpleName().toString() + " Not a valid data type for a parameter.";
                        return Boolean.FALSE;
                    }
                    if (param.asType().toString().equals("com.jmoordb.core.model.Search") || param.asType().toString().equals("com.jmoordb.core.model.Search[]")) {
                        message = "Method " + nameOfMethod + "() parameter: " + param.getSimpleName().toString() + " Not a valid data type for a parameter.";
                        return Boolean.FALSE;
                    }
                    if (param.asType().toString().equals("com.jmoordb.core.model.Pagination")) {
                        havePaginationParameters = Boolean.TRUE;
                        repositoryMethod.setNameOfParametersPagination(param.getSimpleName().toString());
                        controlPaginationSorted++;
                    }
                    if (param.asType().toString().equals("java.lang.String")) {
                        haveStringParameters = Boolean.TRUE;
                    }
                }
                if (parameters.size() == MAXIMUM_NUMBER_OF_PARAMETERS && (!havePaginationParameters || !haveStringParameters)) {
                    message = nameOfMethod + "() only String and Pagination data are allowed.";
                    return Boolean.FALSE;
                }
                if (!haveStringParameters) {
                    message = nameOfMethod + "() It must have at least one parameter of type String.";
                    return Boolean.FALSE;
                }
            }
            repositoryMethod.setIncludeTimeFields(includeTimeFields);
            repositoryMethod.setExcludeTimeFields(excludeTimeFields);
            if (!nameOfMethod.startsWith("regex")) {
                message = nameOfMethod + "() should start with regex";
                return Boolean.FALSE;
            }
            Regex regex = executableElement.getAnnotation(Regex.class);
            List<String> tokenList = new ArrayList<>();
            if (regex.where().trim().equals("")) {

//                if (parameters.size() > 0) {
                if ((parameters.size() > 1 && !havePaginationParameters)) {
                    message = nameOfMethod + "() has parameters but the where attribute is empty.";
                    return Boolean.FALSE;
                }
            } else {
                /**
                 * Verifica que no se inserten caracteres no validos No esta
                 * permitido ( ) [ ] $ y que contenga @
                 */
                String validQueryMessage = RegexAnalizerUtil.isValidWhereCharacters(regex, parameters, nameOfMethod).trim();
                if (!validQueryMessage.equals("")) {
                    message = validQueryMessage;
                    return Boolean.FALSE;
                }

                /**
                 * Obtiene un List<String> de los tokens del where() de @Query
                 */
                tokenList = Collections.list(new StringTokenizer(regex.where().trim(), " ")).stream()
                        .map(token -> (String) token)
                        .collect(Collectors.toList());

//                if (!RegexAnalizerUtil.isValidSizeComparative(tokenList.size(), parameters.size())) {
//                    message =  nameOfMethod + "() There is no coincidence between the number of attributes and the parameters defined in the where().";
//                    return Boolean.FALSE;
//                }
                /**
                 *
                 */
                if (JmoordbCoreUtil.isPar(tokenList.size())) {
                    message = nameOfMethod + "() The the number of  elements defined in the where() attribute are not correct..";
                    return Boolean.FALSE;
                }
                /**
                 * Verifica que el primer elemento del where() sea un
                 * identificador valido java
                 */
                if (!RegexAnalizerUtil.nameOfJavaIdentifierValid(tokenList.get(0))) {
                    message = nameOfMethod + "() The first attribute must be a valid Java identifier name.";
                    return Boolean.FALSE;
                }
                /**
                 * Valida las reglas comparando el where() con los paràmetros
                 */
                if (!RegexAnalizerUtil.rulesValid(regex, tokenList, nameOfMethod, Boolean.FALSE, haveStringParameters)) {
                    message = nameOfMethod + "() Check the where() attribute is not correctly defined..";
                    return Boolean.FALSE;
                }
                if (!RegexAnalizerUtil.nameOfParametersEqualsToNameOfMethod(tokenList, parameters, nameOfMethod)) {
                    message = nameOfMethod + "() The parameter names defined in where() do not match those declared in the method.";
                    return Boolean.FALSE;
                }
                /**
                 * Si se desea validar que el nombre del identificador coincida
                 * con el nombre de parametros habilite esta secccion.
                 */
                if (NAMEOFIDENTIFIER_EQUALS_NAMEOFPARAMETERSOFMETHOD) {
                    if (!RegexAnalizerUtil.nameOfIdentificatorEqualsToNameOfMethod(tokenList, parameters, nameOfMethod)) {
                        message = nameOfMethod + "() The identifier names defined in where() do not match those declared in the method.";
                        return Boolean.FALSE;
                    }
                }

            }
            /**
             * Actualiza RepositoryMethod
             */
            repositoryMethod.setWhere(regex.where().trim());
            repositoryMethod.setTokenWhere(tokenList);
            repositoryMethod.setHavePagination(havePaginationParameters);
//            repositoryMethod.setHaveSorted(haveSortedParameters);
            repositoryMethod.setCaseSensitive(regex.caseSensitive());
            repositoryMethod.setTypeOrder(regex.typeOrder());
            return Boolean.TRUE;
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return Boolean.FALSE;
    }
    // </editor-fold>

}
