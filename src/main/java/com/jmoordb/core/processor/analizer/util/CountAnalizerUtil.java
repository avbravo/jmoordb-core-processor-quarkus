/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.analizer.util;

import com.jmoordb.core.annotation.repository.Count;
import com.jmoordb.core.annotation.repository.Regex;

import static com.jmoordb.core.util.JmoordbCoreUtil.isFirstCharacter;
import static com.jmoordb.core.util.JmoordbCoreUtil.removeFirstCharacter;
import com.jmoordb.core.util.MessagesUtil;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.element.VariableElement;

/**
 *
 * @author avbravo
 */
public class CountAnalizerUtil {
    // <editor-fold defaultstate="collapsed" desc="Boolean nameOfParametersEqualsToNameOfMethod((List<String> tokenList, List<? extends VariableElement> parameters String nameOfMethod) ">

    /**
     * Verifica que los nombres de parametros @ coindican con los parametros
     *
     * @param tokenList
     * @param parameters
     * @param nameOfMethod
     * @return
     */
    public static Boolean nameOfParametersEqualsToNameOfMethod(List<String> tokenList, List<? extends VariableElement> parameters, String nameOfMethod) {
        Boolean isValid = Boolean.FALSE;
        try {
// Evalua la columna @PARA --> Parametros           
//| size | iden       | LOG          | @PARA        | BOOL     | ()|
//| ---  | ------     | ---          |---           |---       |-- |
//|  3   | 0          |  1           | 2            |--        | 1 |
//|  7   | 0,4        |  1,5         | 2,6          | 3        | 2 |
//|  11  | 0,4,8      |  1,5,9       | 2,6,10       | 3,7      | 3 |
//|  15  | 0,4,8,12   |  1,5,9,13    | 2,6,10,14    | 3,7,11   | 4 |
//|  19  | 0,4,8,12,16|  1,5,9,13,17 | 2,6,10,14,18 | 3,7,11,15| 5 |
            Boolean position2 = Boolean.FALSE;
            Boolean position6 = Boolean.FALSE;
            Boolean position10 = Boolean.FALSE;
            Boolean position14 = Boolean.FALSE;
            Boolean position18 = Boolean.FALSE;

            switch (tokenList.size()) {
                case 3:
// verifica que el parametro este dentro del where()
                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if (removeFirstCharacter(tokenList.get(2)).equals(param.getSimpleName().toString())) {
                            isValid = Boolean.TRUE;
                            break;
                        }

                    }

                    break;
                case 7:

                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if (removeFirstCharacter(tokenList.get(2)).equals(param.getSimpleName().toString())) {
                            position2 = Boolean.TRUE;

                        }
                        if (removeFirstCharacter(tokenList.get(6)).equals(param.getSimpleName().toString())) {
                            position6 = Boolean.TRUE;

                        }

                    }
                    if (position2 && position6) {
                        isValid = Boolean.TRUE;
                    }
                    break;
                case 11:

                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if (removeFirstCharacter(tokenList.get(2)).equals(param.getSimpleName().toString())) {
                            position2 = Boolean.TRUE;

                        }
                        if (removeFirstCharacter(tokenList.get(6)).equals(param.getSimpleName().toString())) {
                            position6 = Boolean.TRUE;

                        }
                        if (removeFirstCharacter(tokenList.get(10)).equals(param.getSimpleName().toString())) {
                            position10 = Boolean.TRUE;
                        }

                    }

                    if (position2 && position6 && position10) {
                        isValid = Boolean.TRUE;

                    }
                    break;
                case 15:
                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if (removeFirstCharacter(tokenList.get(2)).equals(param.getSimpleName().toString())) {
                            position2 = Boolean.TRUE;

                        }
                        if (removeFirstCharacter(tokenList.get(6)).equals(param.getSimpleName().toString())) {
                            position6 = Boolean.TRUE;

                        }
                        if (removeFirstCharacter(tokenList.get(10)).equals(param.getSimpleName().toString())) {
                            position10 = Boolean.TRUE;
                        }
                        if (removeFirstCharacter(tokenList.get(14)).equals(param.getSimpleName().toString())) {
                            position14 = Boolean.TRUE;
                        }

                    }
                    if (position2 && position6 && position10 && position14) {
                        isValid = Boolean.TRUE;

                    }
                    break;

                case 19:
                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if (removeFirstCharacter(tokenList.get(2)).equals(param.getSimpleName().toString())) {
                            position2 = Boolean.TRUE;

                        }
                        if (removeFirstCharacter(tokenList.get(6)).equals(param.getSimpleName().toString())) {
                            position6 = Boolean.TRUE;

                        }
                        if (removeFirstCharacter(tokenList.get(10)).equals(param.getSimpleName().toString())) {
                            position10 = Boolean.TRUE;
                        }
                        if (removeFirstCharacter(tokenList.get(14)).equals(param.getSimpleName().toString())) {
                            position14 = Boolean.TRUE;
                        }
                        if (removeFirstCharacter(tokenList.get(18)).equals(param.getSimpleName().toString())) {
                            position18 = Boolean.TRUE;
                        }

                    }
                    if (position2 && position6 && position10 && position14 && position18) {
                        isValid = Boolean.TRUE;

                    }

                    break;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean nameOfIdentificatorEqualsToNameOfMethod<String> tokenList, List<? extends VariableElement> parameters, String nameOfMethod)">

    /**
     * Verifica que nos nombres de identificadores coincidan con los parameros
     * de los métodos
     *
     * @param tokenList
     * @param parameters
     * @param nameOfMethod
     * @return
     */
    public static Boolean nameOfIdentificatorEqualsToNameOfMethod(List<String> tokenList, List<? extends VariableElement> parameters, String nameOfMethod) {
        Boolean isValid = Boolean.FALSE;
        try {
// Evalua la column iden
//| size | iden       | LOG          | @PARA        | BOOL     | ()|
//| ---  | ------     | ---          |---           |---       |-- |
//|  3   | 0          |  1           | 2            |--        | 1 |
//|  7   | 0,4        |  1,5         | 2,6          | 3        | 2 |
//|  11  | 0,4,8      |  1,5,9       | 2,6,10       | 3,7      | 3 |
//|  15  | 0,4,8,12   |  1,5,9,13    | 2,6,10,14    | 3,7,11   | 4 |
//|  19  | 0,4,8,12,16|  1,5,9,13,17 | 2,6,10,14,18 | 3,7,11,15| 5 |
            Boolean position0 = Boolean.FALSE;
            Boolean position4 = Boolean.FALSE;
            Boolean position8 = Boolean.FALSE;
            Boolean position12 = Boolean.FALSE;
            Boolean position16 = Boolean.FALSE;

            switch (tokenList.size()) {
                case 3:
// verifica que el parametro este dentro del where()
                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if ((tokenList.get(0)).equals(param.getSimpleName().toString())) {
                            isValid = Boolean.TRUE;
                            break;
                        }

                    }

                    break;
                case 7:

                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if ((tokenList.get(0)).equals(param.getSimpleName().toString())) {
                            position0 = Boolean.TRUE;
                        }
                        if ((tokenList.get(4)).equals(param.getSimpleName().toString())) {
                            position4 = Boolean.TRUE;

                        }

                    }
                    if (position0 && position4) {
                        isValid = Boolean.TRUE;

                    }
                    break;
                case 11:

                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if ((tokenList.get(0)).equals(param.getSimpleName().toString())) {
                            position0 = Boolean.TRUE;

                        }
                        if ((tokenList.get(4)).equals(param.getSimpleName().toString())) {
                            position4 = Boolean.TRUE;

                        }
                        if ((tokenList.get(8)).equals(param.getSimpleName().toString())) {
                            position8 = Boolean.TRUE;
                        }

                    }
                    if (position0 && position4 && position8) {
                        isValid = Boolean.TRUE;
                    }
                    break;
                case 15:
                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if ((tokenList.get(0)).equals(param.getSimpleName().toString())) {
                            position0 = Boolean.TRUE;

                        }
                        if ((tokenList.get(4)).equals(param.getSimpleName().toString())) {
                            position4 = Boolean.TRUE;

                        }
                        if ((tokenList.get(8)).equals(param.getSimpleName().toString())) {
                            position8 = Boolean.TRUE;
                        }
                        if ((tokenList.get(12)).equals(param.getSimpleName().toString())) {
                            position12 = Boolean.TRUE;
                        }

                    }
                    if (position0 && position4 && position8 && position12) {
                        isValid = Boolean.TRUE;
                    }
                    break;
                case 19:
                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement param = parameters.get(i);
                        if ((tokenList.get(0)).equals(param.getSimpleName().toString())) {
                            position0 = Boolean.TRUE;

                        }
                        if ((tokenList.get(4)).equals(param.getSimpleName().toString())) {
                            position4 = Boolean.TRUE;

                        }
                        if ((tokenList.get(8)).equals(param.getSimpleName().toString())) {
                            position8 = Boolean.TRUE;
                        }
                        if ((tokenList.get(12)).equals(param.getSimpleName().toString())) {
                            position12 = Boolean.TRUE;
                        }
                        if ((tokenList.get(16)).equals(param.getSimpleName().toString())) {
                            position16 = Boolean.TRUE;
                        }

                    }
                    if (position0 && position4 && position8 && position12 && position16) {
                        isValid = Boolean.TRUE;
                    }
                    break;

            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean rulesValid(Count regex, List<String> tokenList, String nameOfMethod, Boolean havePaginationParameters, Boolean haveString)">
    /**
     * Valida que se cumplan las reglas del regex, en cuanto a que elementos
     * deben estar incluidos y en qye posicion verifica los nombres de atributos
     *
     * @param regex
     * @param tokenList
     * @param nameOfMethod
     * @param havePaginationParameters
     * @param haveString
     * @return
     */
    public static Boolean rulesValid(Count regex, List<String> tokenList, String nameOfMethod,  Boolean haveStringParameters) {
        Boolean isValid = Boolean.FALSE;
        try {
// Valida posicion por posicio los elementos
// text .like. @text
//| size | iden       | LOG                       | @PARA        | BOOL             | ()|
//| ---  | ------     | ---                       |---           |---               |-- |
//|  3   | 0          | 1=.like.                  | 2            |--                | 1 |
//|  7   | 0,4        | 1= .like. 5= .skip.       | 2,6          | 3 =.limit.       | 2 |

            /**
             * Analiza si el metodo tiene solo Pagination y Sorted
             *
             * @Regex(where="pagination .skip. @pagination .order. sorted .by.
             * @sorted") public List<Oceano> findAllPaginationSorted(Pagination
             * pagination, Sorted sorted);
             *
             */
       
           

            if (haveStringParameters) {
                Optional<String> likeFound = tokenList
                        .stream()
                        .filter(token -> token.equals(".like."))
                        .findAny();

                if (!likeFound.isPresent()) {
                    return Boolean.FALSE;
                }

            }



            switch (tokenList.size()) {
                case 3:
                    /**
                     * Verificar que no tenga paginación y ordenación
                     *
                     * @Regex(id .eq. @id) findAll(String id)
                     */
              

                        if (nameOfJavaIdentifierValid(tokenList.get(0))
                                && isValidLogicalOperator(tokenList.get(1))
                                && isFirstCharacter(tokenList.get(2), "@")) {
                            isValid = Boolean.TRUE;
                       
                        }

                   

                    break;

               
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
    
        return isValid;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="Boolean isValidLogicalOperator(String regex)">
    /**
     *
     * @param regex
     * @return TRUE/FALSE si es un operador valido Los operadores validos estan
     * en las posiciones impares id .eq. @id .and. name .eq. @name Si contien (
     * se debe agrupar) generalmente estas codnciones vienen con un JSOnQUery
     * (id .eq. @id .and. name .eq. @name) or active .eq.
     * @active
     */
    public static Boolean isValidLogicalOperator(String regex) {
        Boolean isValid = Boolean.FALSE;
        try {
            switch (regex) {
                case ".like.":
                    isValid = Boolean.TRUE;
                    break;
               

            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean compare(String regex, String operator)">

    /**
     * Compara los valores
     *
     * @param regex
     * @param operator
     * @return
     */
    public static Boolean compare(String regex, String operator) {
        Boolean isValid = Boolean.FALSE;
        try {
            if (regex.equals(operator)) {
                isValid = Boolean.TRUE;
            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean isValidBooleanOperador(String regex) ">
    /**
     * Veririca los operadores Booleanos permitodos
     *
     * @param regex
     * @return
     */
    public static Boolean isValidBooleanOperador(String regex) {
        Boolean isValid = Boolean.FALSE;
        try {
            switch (regex) {

                case ".limit.":
                    isValid = Boolean.TRUE;
                    break;

            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean isValidSizeComparative(Integer regexSize, Integer parameterSize)>
    /**
     *
     * @param regexSize
     * @param parameterSize
     * @return Boolean si los tamaños de parametros y de Regex.where cumplen las
     * reglas
     */
    public static Boolean isValidSizeComparative(Integer regexSize, Integer parameterSize) {
        Boolean isValid = Boolean.FALSE;
        try {
// regex incremento de 4
// paramSize incremento de 1
            if ((regexSize == 3 && parameterSize == 1)
                    || (regexSize == 7 && parameterSize == 2)
                    || (regexSize == 11 && parameterSize == 3)
                    || (regexSize == 15 && parameterSize == 4)
                    || (regexSize == 19 && parameterSize == 5)) {
                isValid = Boolean.TRUE;
            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }
// </editor-fold>

    

    // <editor-fold defaultstate="collapsed" desc="Boolean isValidPaginationInWhere(Regex regex)">
    /**
     * Verifica que Pagination en where() cuente con las anotaciones .skip. y
     * .limit.
     *
     * @param regex
     * @return
     */
    public static Boolean isValidPaginationInWhere(Regex regex) {
        Boolean isValid = Boolean.FALSE;
        try {
            if (regex.where().contains(".skip.") || regex.where().contains(".limit.")) {
                isValid = Boolean.TRUE;
            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean isValidSortedInWhere(Regex regex)">

    /**
     * Verifica que contenga las anotaciones .order. y .by.
     *
     * @param regex
     * @return
     */
    public static Boolean isValidLikeInWhere(Regex regex) {
        Boolean isValid = Boolean.FALSE;
        try {
            if (regex.where().contains(".like.")) {
                isValid = Boolean.TRUE;
            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Predicate<String> findToken(String token)">
    public static Predicate<String> findToken(String token) {
        return (String l) -> {
            return l.equals(token);
        };
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" boolean  nameOfJavaIdentifierValid(String identifier)">
    public static boolean nameOfJavaIdentifierValid(String identifier) {
        try {
            String regex = "^([a-zA-Z_$][a-zA-Z\\d_$]*)$";

            Pattern p = Pattern.compile(regex);

            if (identifier == null) {
                return false;
            }

            Matcher m = p.matcher(identifier);

            return m.matches();
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return Boolean.FALSE;
    }
// </editor-fold>

}
