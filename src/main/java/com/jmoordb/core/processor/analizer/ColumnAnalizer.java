package com.jmoordb.core.processor.analizer;

import com.jmoordb.core.annotation.Column;
import com.jmoordb.core.processor.methods.EntityField;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public class ColumnAnalizer {
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
        ColumnAnalizer.message = message;
    }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="analizer(ExecutableElement executableElement,TypeMirror typeEntity,RepositoryMethod repositoryMethod)">

    public static Boolean analizer(Column columnAnnotation, Element element, ExecutableElement executableElement, TypeMirror typeEntity, EntityField entityField) {
        try {
            String nameOfMethod = ProcessorUtil.nameOfMethod(executableElement);
            String nameOfEntity = ProcessorUtil.nameOfEntity(typeEntity);
            String typeOptional = "java.util.Optional<" + typeEntity.toString().trim() + ">";
            String typeList = "java.util.List<" + typeEntity.toString().trim() + ">";
            String typeSet = "java.util.Set<" + typeEntity.toString().trim() + ">";
            String typeStream = "java.util.stream.Stream<" + typeEntity.toString().trim() + ">";
            Boolean havePaginationParameters = Boolean.FALSE;
            Boolean haveSortedParameters = Boolean.FALSE;
            /**
             *
             */

            TypeMirror returnTypeOfMethod = executableElement.getReturnType();

//            if (!returnTypeOfMethod.toString().equals(typeList) && !returnTypeOfMethod.toString().equals(typeOptional) && !returnTypeOfMethod.toString().equals(typeSet) && !returnTypeOfMethod.toString().equals(typeStream)) {
//                message =  nameOfMethod + "() The return type must be a List<" + nameOfEntity + "> or Optional<" + nameOfEntity + "> or Set<" + nameOfEntity + "> or Stream<"+ nameOfEntity + ">";
//                return Boolean.FALSE;
//            }

//            List<? extends VariableElement> parameters = executableElement.getParameters();
//            
//             Integer controlPaginationSorted =0;
//            if (parameters.size() <= 0) {
//                //   MessagesUtil.msg(" [NO TIENE PARAMETROS ] ");
//            } else {
//                if (parameters.size() > MAXIMUM_NUMBER_OF_PARAMETERS) {
//                    message =  nameOfMethod + "() the maximum size of parameters allowed is " + MAXIMUM_NUMBER_OF_PARAMETERS + " . If you use more than" + MAXIMUM_NUMBER_OF_PARAMETERS + " use @QueryJSON instead.";
//                    return Boolean.FALSE;
//                }
//
//                for (int i = 0; i < parameters.size(); i++) {
//                    VariableElement param = parameters.get(i);
//
//                    // Verifica que el tipo del parámetro sea un tipo aceptado
//                    if (!JmoordbCoreUtil.isJmoordbJavaTypeValid(param.asType().toString())) {
//                        message = "Method " + nameOfMethod + "() parameter: " + param.getSimpleName().toString() + " Not a valid data type for a parameter.";
//                        return Boolean.FALSE;
//                    }
//
//                    if (param.asType().toString().equals("com.jmoordb.core.model.Search") || param.asType().toString().equals("com.jmoordb.core.model.Search[]")) {
//                        message = "Method " + nameOfMethod + "() parameter: " + param.getSimpleName().toString() + " Not a valid data type for a parameter.";
//                        return Boolean.FALSE;
//                    }
//                    if (param.asType().toString().equals("com.jmoordb.core.model.Pagination")) {
//                        havePaginationParameters = Boolean.TRUE;
//                        controlPaginationSorted++;
//                        repositoryMethod.setNameOfParametersPagination(param.getSimpleName().toString());
//                    }
//                    if (param.asType().toString().equals("com.jmoordb.core.model.Sorted")) {
//                        haveSortedParameters = Boolean.TRUE;
//                        controlPaginationSorted++;
//                        repositoryMethod.setNameOfParametersSorted(param.getSimpleName().toString());
//                    }
//                }
//
//            }
//            
//             if (!nameOfMethod.startsWith("query") ) {
//                message = "Method " + nameOfMethod + "() name should start with query";
//                return Boolean.FALSE;
//            }
//            Query query = executableElement.getAnnotation(Query.class);
////Almacena el where
//
//            List<String> tokenList = new ArrayList<>();
//            if (query.where().trim().equals("")) {
//               
//
////                if (parameters.size() > 0) {
//                if ((parameters.size() - controlPaginationSorted) != 0) {
//                    message =  nameOfMethod + "() has parameters but the where attribute is empty.";
//                    return Boolean.FALSE;
//                }
//            } else {
//                /**
//                 * Verifica que no se inserten caracteres no validos No esta
//                 * permitido ( ) [ ] $ y que contenga @
//                 */
//                String validQueryMessage = QueryAnalizerUtil.isValidQueryCharacters(query, parameters, nameOfMethod).trim();
//                if (!validQueryMessage.equals("")) {
//                    message = validQueryMessage;
//                    return Boolean.FALSE;
//                }
//
//                /**
//                 * Obtiene un List<String> de los tokens del where() de @Query
//                 */
//                tokenList = Collections.list(new StringTokenizer(query.where().trim(), " ")).stream()
//                        .map(token -> (String) token)
//                        .collect(Collectors.toList());
//
//                if (!QueryAnalizerUtil.isValidSizeComparative(tokenList.size(), (parameters.size() - controlPaginationSorted))) {
//                    message =  nameOfMethod + "() There is no coincidence between the number of attributes and the parameters defined in the where().";
//                    return Boolean.FALSE;
//                }
//                /**
//                 *
//                 */
//
//                if (JmoordbCoreUtil.isPar(tokenList.size())) {
//                    message =  nameOfMethod + "() The the number of  elements defined in the where() attribute are not correct..";
//                    return Boolean.FALSE;
//                }
//                /**
//                 * Verifica que el primer elemento del where() sea un
//                 * identificador valido java
//                 */
//                if (!QueryAnalizerUtil.nameOfJavaIdentifierValid(tokenList.get(0))) {
//                    message =  nameOfMethod + "() The first attribute must be a valid Java identifier name.";
//                    return Boolean.FALSE;
//                }
//                /**
//                 * Valida las reglas comparando el where() con los paràmetros
//                 */
////                if (!QueryAnalizerUtil.rulesValid(query, tokenList, nameOfMethod, havePaginationParameters, haveSortedParameters)) {
//                if (!QueryAnalizerUtil.rulesValid(query, tokenList, nameOfMethod, Boolean.FALSE, Boolean.FALSE)) {
//                    message =  nameOfMethod + "() Check the where() attribute is not correctly defined..";
//                    return Boolean.FALSE;
//                }
//                if (!QueryAnalizerUtil.nameOfParametersEqualsToNameOfMethod(tokenList, parameters, nameOfMethod)) {
//                    message =  nameOfMethod + "() The parameter names defined in where() do not match those declared in the method.";
//                    return Boolean.FALSE;
//                }
//                /**
//                 * Si se desea validar que el nombre del identificador coincida
//                 * con el nombre de parametros habilite esta secccion.
//                 */
//                if (NAMEOFIDENTIFIER_EQUALS_NAMEOFPARAMETERSOFMETHOD) {
//                    if (!QueryAnalizerUtil.nameOfIdentificatorEqualsToNameOfMethod(tokenList, parameters, nameOfMethod)) {
//                        message =  nameOfMethod + "() The identifier names defined in where() do not match those declared in the method.";
//                        return Boolean.FALSE;
//                    }
//                }
//
//            }
            /**
             * Actualiza RepositoryMethod
             */

//            entityMethod.setWhereDescomposed(ProcessorUtil.generateWhereDescomposed(repositoryMethod));

            return Boolean.TRUE;
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return Boolean.FALSE;
    }
    // </editor-fold>

}