/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.analizer.util;

import com.jmoordb.core.util.MessagesUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author avbravo
 */
public class NameOfMethodAnalizerUtil {
    // <editor-fold defaultstate="collapsed" desc="fields()">

    static List<String> logical = Arrays.asList("And", "Or", "Not");
    static List<String> lexemas = new ArrayList<>();
    static List<String> worldAndToken = new ArrayList<>();
    static List<String> comparator = Arrays.asList("Lessthan", "Greaterthan", "Lessthanequals","Lessthanequal", "Between",
          "Greaterthanequals",  "Greaterthanequal", "Equal", "Notequal");

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean isLogical(String text)">
    public static Boolean isLogical(String text) {
        Boolean result = Boolean.FALSE;
        try {
            for (String s : logical) {
                if (text.equals(s)) {
                    result = Boolean.TRUE;
                    break;
                }
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return result;

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean isField(String text)">
    public static Boolean isField(String text) {
        Boolean result = Boolean.FALSE;
        try {

            if (isLogical(text)) {
                result = Boolean.FALSE;
            } else {
                if (isComparator(text)) {
                    result = Boolean.FALSE;
                } else {
                    if (isPagination(text)) {
                        result = Boolean.FALSE;
                    } else {
                        if (isSorted(text)) {
                            result = Boolean.FALSE;
                        } else {
                            result = Boolean.TRUE;
                        }
                    }
                }

            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return result;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean isPagination(String text)">
    public static Boolean isPagination(String text) {
        Boolean result = Boolean.FALSE;
        try {

            if (text.equals("Pagination")) {
                result = Boolean.TRUE;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return result;

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean isSorted(String text()">
    public static Boolean isSorted(String text) {
        Boolean result = Boolean.FALSE;
        try {

            if (text.equals("Sorted")) {
                result = Boolean.TRUE;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return result;

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Boolean isComparator(String text) ">
    public static Boolean isComparator(String text) {
        Boolean result = Boolean.FALSE;
        try {
            for (String s : comparator) {
                if (text.equals(s)) {
                    result = Boolean.TRUE;
                    break;
                }
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return result;

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String simplifyText(String text)">
    /**
     * Simplifica el texto convirtiendo los comparadores en sintaxis correcta
     *
     * @param text
     * @return
     */
    public static String simplifyText(String text) {
        try {
            
            if (text.contains("GreaterThanEquals")) {
                text = text.replace("GreaterThanEquals", "Greaterthanequals").trim();
            }
            if (text.contains("GreaterThanEqual")) {
                text = text.replace("GreaterThanEqual", "Greaterthanequal").trim();
            }
            
            if (text.contains("GreaterThan")) {
                text = text.replace("GreaterThan", "Greaterthan").trim();
            }
            
            if (text.contains("LessThanEquals")) {
                text = text.replace("LessThanEquals", "Lessthanequals").trim();
            }
            if (text.contains("LessThanEqual")) {
                text = text.replace("LessThanEqual", "Lessthanequal").trim();
            }
           
            if(text.contains("LessThan")){
                 text = text.replace("LessThan", "Lessthan").trim();
            }

           

            text = text.replace("NotEqual", "Notequal").trim();
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return text;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="generarTokens(List<String> tokens, String text)">
    public static List<String> generarTokens(String text) {
        List<String> wordls = new ArrayList<>();
        try {
            String[] r = text.split("(?<=.)(?=\\p{Lu})");

            for (String s : r) {
                if (!s.equals("")) {
                    wordls.add(s);
                }

            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return wordls;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List<String> generarLexemas(List<String> worldAndToken)">
    public static List<String> generarLexemas(List<String> worldAndToken) {
        List<String> result = new ArrayList<>();
        try {

            Integer count = 0;
            for (String s : worldAndToken) {
                if (isLogical(s)) {
                    result.add("L");
                } else {
                    if (isComparator(s)) {
                        result.add("C");
                    } else {
                        if (isPagination(s)) {
                            result.add("P");
                        } else {
                            if (isSorted(s)) {
                                result.add("S");
                            } else {
                                if (s.equals("")) {

                                } else {
                                    result.add("F");
                                }
                            }
                        }
                    }

                }

            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return result;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Boolean isComparator(String text, Integer numberOfParameters) ">
    public static String validRules(List<String> lexema, Integer numberOfParameters) {
        String result = "";
        try {
            if (!lexema.get(0).equals("F")) {
                // Debe iniciar en Field
                return "You must start the Field";
            }

            if (lexema.get(lexema.size() - 1).equals("L")) {
                // Nuca debe finalizar en LOGICAL
                return "should not end with Logical";
            }

            if (!validSecuenceLogic(lexemas)) {
                return "Logical Operators used inappropriately";
            }
            if (!validSecuenceComparator(lexemas)) {
                return "Comparison operators used inappropriately";
            }
            if (!validSecuencePagination(lexemas)) {
                return "After a Pagination must be final or use a Sorted";
            }
            if (!validSecuenceSorted(lexemas)) {
                return "After Sorted, no other operator can be used";
            }

            Integer totalLogical = 0;
            totalLogical = lexema.stream().filter(s -> (s.equals("L"))).map(_item -> 1).reduce(totalLogical, Integer::sum);

            Integer totalComparator = 0;
            totalComparator = lexema.stream().filter(s -> (s.equals("C"))).map(_item -> 1).reduce(totalComparator, Integer::sum);

            Integer totalPagination = 0;
            totalPagination = lexema.stream().filter(s -> (s.equals("P"))).map(_item -> 1).reduce(totalPagination, Integer::sum);

            Integer totalSorted = 0;
            totalSorted = lexema.stream().filter(s -> (s.equals("S"))).map(_item -> 1).reduce(totalSorted, Integer::sum);

            Integer totalField = 0;
            totalField = lexema.stream().filter(s -> (s.equals("F"))).map(_item -> 1).reduce(totalField, Integer::sum);

            if (lexema.size() == numberOfParameters) {
                return "";
            }

            if ((totalField + totalPagination + totalSorted) != numberOfParameters) {
                return "The declared parameters do not match the method definition.";
            }
//            System.out.println("\t\t\t---(((lexema.size() - 1 "+ (lexema.size() - 1)+" totalLogical"+ totalLogical+" numberOfParameters ]] "+numberOfParameters+ "))))");
//            if (((lexema.size() - 1) - totalLogical) != numberOfParameters) {
//                return "The declared parameters do not match the method definition.";
//            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return result;

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" List<String> joinFields(List<String> lexemas, Integer numberOfParameters, List<String> worldAndToken) ">
    /**
     * Une los campos que se divieron por ejemplo IdOceano , se dividido en Id
     * Oceano el lo une
     *
     * @param lexemas
     * @param numberOfParameters
     * @param worldAndToken
     * @return
     */
    public static List<String> joinFields(List<String> lexemas, Integer numberOfParameters, List<String> worldAndToken) {

        List<String> result = new ArrayList<>();
        try {
            Integer iteraciones = 0;
            Integer size = lexemas.size();
            Integer nextIndex = 0;
            for (Integer index = 0; index < size; index++) {

                result = new ArrayList<>();

                Integer indexFound = -1;
                // Encuentra el elemento cuyo anterior es tambien un Field
                for (Integer i = 0; i < size; i++) {
                    nextIndex = i + 1;

                    if (nextIndex < size) {
                        if (lexemas.get(i).equals(lexemas.get(nextIndex)) && lexemas.get(i).equals("F") && (nextIndex <= size)) {
                            indexFound = i;
                            break;
                        }
                    }

                }

                /**
                 * Cargo el ArrayList<>
                 */
                for (Integer i = 0; i < size; i++) {
                    if (i == indexFound) {
                        result.add(worldAndToken.get(i) + worldAndToken.get(i + 1));
                        i++;
                    } else {
                        result.add(worldAndToken.get(i));
                    }
                }

                worldAndToken = result;
                lexemas = generarLexemas(worldAndToken);
                size = lexemas.size();

                if (indexFound == -1) {
                    /*
                    No se necesitan más interaciones 
                     */
                    break;
                } else {
                    iteraciones++;
                }

            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return worldAndToken;

    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean validSecuenceLogic(List<String> lexemas) ">
    /**
     *
     * @param lexemas
     * @return Verifica que los operadodres Logicos esten colocados
     * adecuadamente
     */
    public static Boolean validSecuenceLogic(List<String> lexemas) {

        Boolean isValid = Boolean.TRUE;
        try {
            Integer iteraciones = 0;
            Integer size = lexemas.size();
            Integer nextIndex = 0;

            Optional<String> logicalFound = lexemas
                    .stream()
                    .filter(token -> token.equals("L"))
                    .findAny();
            if (!logicalFound.isPresent()) {
                return Boolean.TRUE;
            }

            Integer indexFound = -1;
            // Encuentra el elemento cuyo anterior es tambien un Field
            for (Integer i = 0; i < size; i++) {
                nextIndex = i + 1;

                if (nextIndex < size) {
                    if (lexemas.get(i).equals(lexemas.get(nextIndex)) && lexemas.get(i).equals("L") && (nextIndex <= size)) {
                        isValid = Boolean.FALSE;
                        break;
                    }
                }

            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;

    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean validSecuencePagination(List<String> lexemas) ">
    static Boolean validSecuencePagination(List<String> lexemas) {

        Boolean isValid = Boolean.FALSE;
        try {
            Optional<String> logicalFound = lexemas
                    .stream()
                    .filter(token -> token.equals("P"))
                    .findAny();
            if (!logicalFound.isPresent()) {
                return Boolean.TRUE;
            }

            Integer iteraciones = 0;
            Integer size = lexemas.size() - 1;
            Integer nextIndex = 0;
            if (lexemas.get(size).equals("P")) {
                return Boolean.TRUE;
            }
            Integer before = size - 1;
            if (lexemas.get(before).equals("P") && lexemas.get(size).equals("S")) {
                return Boolean.TRUE;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;

    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean validSecuenceSorted(List<String> lexemas) ">
    static Boolean validSecuenceSorted(List<String> lexemas) {

        Boolean isValid = Boolean.FALSE;
        try {
            Optional<String> logicalFound = lexemas
                    .stream()
                    .filter(token -> token.equals("S"))
                    .findAny();
            if (!logicalFound.isPresent()) {
                return Boolean.TRUE;
            }

            Integer iteraciones = 0;
            Integer size = lexemas.size() - 1;
            Integer nextIndex = 0;
            if (lexemas.get(size).equals("S")) {
                return Boolean.TRUE;

            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;

    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean validSecuenceLogic(List<String> lexemas) ">
    /**
     *
     * @param lexemas
     * @return Verifica que los operadodres Logicos esten colocados
     * adecuadamente
     */
    public static Boolean validSecuenceComparator(List<String> lexemas) {

        Boolean isValid = Boolean.TRUE;
        try {
            Optional<String> logicalFound = lexemas
                    .stream()
                    .filter(token -> token.equals("C"))
                    .findAny();
            if (!logicalFound.isPresent()) {
                return Boolean.TRUE;
            }

            Integer size = lexemas.size();
            Integer nextIndex = 0;

            Integer indexFound = -1;
            // Encuentra el elemento cuyo anterior es tambien un Field
            for (Integer i = 0; i < size; i++) {
                nextIndex = i + 1;

                if (nextIndex < size) {
                    if (lexemas.get(i).equals(lexemas.get(nextIndex)) && lexemas.get(i).equals("C") && (nextIndex <= size)) {
                        isValid = Boolean.FALSE;
                        break;
                    }
                }

            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;

    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Integer countOfTypeOfLexema(List<String> lexema, String type){">
    /**
     * Cuenta los lexemas de un tipo especificado F, C, L, P, S
     *
     * @param lexema
     * @param type
     * @return Integer
     */
    public static Integer countOfTypeOfLexema(List<String> lexema, String type) {
        Integer total = 0;
        try {

            total = lexema.stream().filter(s -> (s.equals(type))).map(_item -> 1).reduce(total, Integer::sum);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return total;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List<Integer> positionOfTypeOfLexema(List<String> lexema, String type)">

    /**
     *
     * @param lexema
     * @param type
     * @return List<Integer> con las posicionesde los lexemas
     */
    public static List<Integer> positionOfTypeOfLexema(List<String> lexema, String type) {
        List<Integer> result = new ArrayList<>();
        try {
            Integer c = 0;
            for (String s : lexema) {
                if (s.equals(type)) {
                    result.add(c);
                }
                c++;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean validLexemaOrder(List<String> lexemas)">
    /**
     *
     * @param lexemas
     * @return Verifica que los operadodres Logicos esten colocados
     * adecuadamente
     */
    public static Boolean validLexemaComparatorOrder(List<String> lexemas) {

        Boolean isValid = Boolean.TRUE;
        try {

            Optional<String> logicalFound = lexemas
                    .stream()
                    .filter(token -> token.equals("L"))
                    .findAny();
            Optional<String> comparatorFound = lexemas
                    .stream()
                    .filter(token -> token.equals("C"))
                    .findAny();

            Optional<String> paginationFound = lexemas
                    .stream()
                    .filter(token -> token.equals("P"))
                    .findAny();
            Optional<String> sortedFound = lexemas
                    .stream()
                    .filter(token -> token.equals("S"))
                    .findAny();
            Optional<String> fieldFound = lexemas
                    .stream()
                    .filter(token -> token.equals("F"))
                    .findAny();

            if (!logicalFound.isPresent() && !comparatorFound.isPresent()
                    && !paginationFound.isPresent() && !sortedFound.isPresent()
                    && !fieldFound.isPresent()) {
                return Boolean.TRUE;
            }

            Integer size = lexemas.size();
            Integer nextIndex = 0;

            Integer indexFound = -1;
            // Encuentra el elemento cuyo anterior es tambien un Field
            for (Integer i = 0; i < size; i++) {
                nextIndex = i + 1;

                if (comparatorFound.isPresent()) {
                    if (nextIndex < size) {

                        if (lexemas.get(i).equals("C") && (lexemas.get(nextIndex).equals("C") || lexemas.get(nextIndex).equals("F"))) {
                            isValid = Boolean.FALSE;
                            break;
                        }
                    }
                }
                if (logicalFound.isPresent()) {
                    if (nextIndex < size) {

                        if (lexemas.get(i).equals("L") && (lexemas.get(nextIndex).equals("L") || lexemas.get(nextIndex).equals("C") || lexemas.get(nextIndex).equals("P") || lexemas.get(nextIndex).equals("S"))) {
                            isValid = Boolean.FALSE;
                            break;
                        }
                    }
                }
                if (fieldFound.isPresent()) {
                    if (nextIndex < size) {

                        if (lexemas.get(i).equals("F") && (lexemas.get(nextIndex).equals("F"))) {
                            isValid = Boolean.FALSE;
                            break;
                        }
                    }
                }
                if (paginationFound.isPresent()) {
                    if (nextIndex < size) {

                        if (lexemas.get(i).equals("P") && (lexemas.get(nextIndex).equals("F") || lexemas.get(nextIndex).equals("C") || lexemas.get(nextIndex).equals("L") || lexemas.get(nextIndex).equals("P"))) {
                            isValid = Boolean.FALSE;
                            break;
                        }
                    }
                }
                if (sortedFound.isPresent()) {
                    if (nextIndex < size) {

                        if (lexemas.get(i).equals("S") && (lexemas.get(nextIndex).equals("F") || lexemas.get(nextIndex).equals("C") || lexemas.get(nextIndex).equals("L") || lexemas.get(nextIndex).equals("P") || lexemas.get(nextIndex).equals("S"))) {
                            isValid = Boolean.FALSE;
                            break;
                        }
                    }
                }

            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;

    }

    // </editor-fold>
}
