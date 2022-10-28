/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.util;

import com.jmoordb.core.annotation.Column;
import com.jmoordb.core.annotation.Embedded;
import com.jmoordb.core.annotation.Id;
import com.jmoordb.core.annotation.Referenced;
import com.jmoordb.core.annotation.enumerations.ParamType;
import com.jmoordb.core.annotation.enumerations.ReturnType;
import com.jmoordb.core.annotation.repository.Count;
import com.jmoordb.core.annotation.repository.CountBy;
import com.jmoordb.core.annotation.repository.CountLikeBy;
import com.jmoordb.core.annotation.repository.Delete;
import com.jmoordb.core.annotation.repository.DeleteBy;
import com.jmoordb.core.annotation.repository.Find;
import com.jmoordb.core.annotation.repository.LikeBy;
import com.jmoordb.core.annotation.repository.Lookup;
import com.jmoordb.core.annotation.repository.Ping;
import com.jmoordb.core.annotation.repository.Query;
import com.jmoordb.core.annotation.repository.Regex;
import com.jmoordb.core.annotation.repository.RegexCount;
import com.jmoordb.core.annotation.repository.Save;
import com.jmoordb.core.annotation.repository.Update;
import com.jmoordb.core.processor.analizer.util.NameOfMethodAnalizerUtil;
import com.jmoordb.core.processor.methods.ParamTypeElement;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.processor.methods.WhereDescomposed;
import com.jmoordb.core.processor.model.DateSupportData;
import com.jmoordb.core.processor.model.DocumentEmbeddableData;
import com.jmoordb.core.processor.model.EntityData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

/**
 *
 * @author avbravo
 */
public class ProcessorUtil {

    // <editor-fold defaultstate="collapsed" desc="nameOfClassAndMethod()">
    public static String nameOfClassAndMethod() {
        final StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        final String s = e.getClassName();
        return s.substring(s.lastIndexOf('.') + 1, s.length()) + "." + e.getMethodName();
    }// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="nameOfClass()">

    public static String nameOfClass() {
        final StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        final String s = e.getClassName();
        return s.substring(s.lastIndexOf('.') + 1, s.length());
    }    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="nameOfMethod(">
    public static String nameOfMethod() {
        final StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        final String s = e.getClassName();
        return e.getMethodName();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String nameOfFileInPath(String filenamePath)">
    /**
     *
     * @param filenamePath
     * @return el nombre del archivo que esta en un path
     */
    public static String nameOfFileInPath(String filenamePath) {
        String name = "";
        try {
            name = filenamePath.substring(filenamePath.lastIndexOf('.') + 1, filenamePath.length());
        } catch (Exception e) {
            System.out.println(nameOfMethod() + " " + e.getLocalizedMessage());
        }
        return name;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String packageOfFileInPath(String filenamePath)">
    /**
     *
     * @param filenamePath
     * @return el paquete del path pasado
     */
    public static String packageOfFileInPath(String filenamePath) {
        String name = "";
        try {
            name = filenamePath.substring(filenamePath.lastIndexOf(System.getProperty("file.separator")) + 1,
                    filenamePath.lastIndexOf('.'));
        } catch (Exception e) {
            System.out.println(nameOfMethod() + " " + e.getLocalizedMessage());
        }
        return name;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="TypeMirror mirror(Supplier<Class<?>> classValue)">
    public static TypeMirror mirror(Supplier<Class<?>> classValue) {
        try {
            var ignored = classValue.get();
            throw new IllegalStateException("Expected a MirroredTypeException to be thrown but got " + ignored);
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        } // </editor-fold>
    }
// <editor-fold defaultstate="collapsed" desc="List<? extends TypeMirror> mirrorAll(Supplier<Class<?>[]> classValues) ">

    public static List<? extends TypeMirror> mirrorAll(Supplier<Class<?>[]> classValues) {
        try {
            var ignored = classValues.get();
            throw new IllegalStateException("Expected a MirroredTypesException to be thrown but got " + ignored);
        } catch (MirroredTypesException e) {
            return e.getTypeMirrors();
        }
    } // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="error(String classAndMethod, String msg) ">
    public static void error(String classAndMethod, String msg) {
        System.out.println(msg);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String packageOfTypeMirror(TypeMirror typeMirror)">
    /**
     *
     * @param typeMirror
     * @return En package de un entity con el formato com.mypackage.
     */
    public static String packageOfTypeMirror(TypeMirror typeMirror) {
        String packageOfEntity = "";
        try {
            String nameOfEntity = ProcessorUtil.nameOfFileInPath(typeMirror.toString());
            packageOfEntity = typeMirror.toString().replace(nameOfEntity, "");
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return packageOfEntity;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String nameOfEntity(TypeMirror typeMirror)">

    /**
     *
     * @param typeMirror
     * @return devuelve el nombre del entity de un typeMirror
     */
    public static String nameOfEntity(TypeMirror typeMirror) {
        String nameOfEntity = "";
        try {
            nameOfEntity = ProcessorUtil.nameOfFileInPath(typeMirror.toString());

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return nameOfEntity;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String nameOfMethod(ExecutableElement executableElement)">
    public static String nameOfMethod(ExecutableElement executableElement) {
        String name = "";
        try {
            name = executableElement.getSimpleName().toString();
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return name;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="convertToReturnType(String returnFound, TypeMirror typeEntity)">
    public static ReturnType convertToReturnType(String returnTypeOfMethod, TypeMirror typeEntity) {
        ReturnType returnType = ReturnType.NONE;
        try {
            String typeOptional = "java.util.Optional<" + typeEntity.toString().trim() + ">";
            String typeList = "java.util.List<" + typeEntity.toString().trim() + ">";
            String typeSet = "java.util.Set<" + typeEntity.toString().trim() + ">";
            String typeStream = "java.util.stream.Stream<" + typeEntity.toString().trim() + ">";
            String typeInteger = "java.lang.Integer";
            String typeBoolean = "java.lang.Boolean";

            if (returnTypeOfMethod.toString().equals(typeOptional)) {
                returnType = ReturnType.OPTIONAL;
            } else {
                if (returnTypeOfMethod.toString().equals(typeList)) {
                    returnType = ReturnType.LIST;
                } else {
                    if (returnTypeOfMethod.toString().equals(typeSet)) {
                        returnType = ReturnType.SET;
                    } else {
                        if (returnTypeOfMethod.toString().equals(typeInteger)) {
                            returnType = ReturnType.INTEGER;
                        } else {
                            if (returnTypeOfMethod.toString().equals(typeBoolean)) {
                                returnType = ReturnType.BOOLEAN;
                            } else {
                                if (returnTypeOfMethod.toString().equals(typeStream)) {
                                    returnType = ReturnType.STREAM;
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return returnType;

    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="convertToReturnType(String returnFound, TypeMirror typeEntity)">
    public static ParamType convertToParamType(String paramAstype, TypeMirror typeEntity) {
        ParamType paramType = ParamType.NONE;
        try {
            String typeEntityString = typeEntity.toString().trim();
            paramAstype = paramAstype.trim();

            if (paramAstype.equals(typeEntityString)) {
                return ParamType.ENTITY;
            }
            if (paramAstype.equals("java.util.Date")) {
                return ParamType.DATE;
            }
            if (paramAstype.equals("java.lang.Boolean")) {
                return ParamType.BOOLEAN;
            }
            if (paramAstype.equals("java.lang.Integer")) {
                return ParamType.INTEGER;
            }
            if (paramAstype.equals("java.lang.Double")) {
                return ParamType.DOUBLE;
            }
            if (paramAstype.equals("java.lang.Float")) {
                return ParamType.FLOAT;
            }
            if (paramAstype.equals("com.jmoordb.core.model.PAGINATI0N")) {
                return ParamType.PAGINATION;
            }
            if (paramAstype.equals("com.jmoordb.core.model.SORTED")) {
                return ParamType.SORTED;
            }
            if (paramAstype.equals("com.jmoordb.core.model.SEARCH")) {
                return ParamType.SEARCH;
            }
            if (paramAstype.equals("java.lang.String")) {
                return ParamType.STRING;
            }

        } catch (Exception e) {
            MessagesUtil.msg(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return paramType;

    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="WhereDescomposed generateWhereDescomposed(RepositoryMethod repositoryMethod)">
    /**
     * *
     * Descompone el where() en elmentos de tipo List<String> almancenado en la
     * clase WhereDescomposer
     *
     * @param repositoryMethod
     * @return
     */
    public static WhereDescomposed generateWhereDescomposed(RepositoryMethod repositoryMethod) {
        WhereDescomposed whereDescomposed = new WhereDescomposed();
        List<String> fieldList = new ArrayList<>();
        List<String> paramList = new ArrayList<>();
        List<String> comparatorList = new ArrayList<>();
        List<String> logicalList = new ArrayList<>();

        Integer fieldPos = 0;
        Integer comparatorPos = 1;
        Integer paramPos = 2;
        Integer logicalPos = 3;
        try {
            Integer index = 3;

            while (index <= repositoryMethod.getTokenWhere().size()) {

                fieldList.add(repositoryMethod.getTokenWhere().get(fieldPos));
                if (index != repositoryMethod.getTokenWhere().size()) {
                    logicalList.add(repositoryMethod.getTokenWhere().get(logicalPos).replace(".", "").trim());
                }

                comparatorList.add(repositoryMethod.getTokenWhere().get(comparatorPos).replace(".", "").trim());
                paramList.add(repositoryMethod.getTokenWhere().get(paramPos).replace("@", "").trim());
                index += 4;
                fieldPos += 4;
                comparatorPos += 4;
                paramPos += 4;
                logicalPos += 4;
            }
            whereDescomposed.setComparatorList(comparatorList);
            whereDescomposed.setFieldList(fieldList);
            whereDescomposed.setLogicalList(logicalList);
            whereDescomposed.setParamList(paramList);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return whereDescomposed;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" String generateFilterFoQueryDelete(RepositoryMethod repositoryMethod)">
    /**
     *
     * @param repositoryMethod
     * @return Genera la sentencia Filter para el Delete Guia de implementación
     * https://github.com/avbravo/books/blob/main/01.jmoordb/01.00%20jmoordb-core-processor/01.14%20@Delete.md
     */
    public static String generateFilterForQueryDelete(RepositoryMethod repositoryMethod) {
        String filter = "Bson filter =";
        try {
            //                          0        1     2         3    4      5    6       7    8     9     10      11   12     13    14        15  16   17   18
            // 4 --> @Delete(where =  "idoceano .eq. @idoceano  ")
            // 7 --> @Delete(where =  "idoceano .eq. @idoceano .and. oceano .eq. @oceano ")
            //  10--> @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not. fecha .gt. @fecha")
            //  15--> @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not. fecha .gt. @fecha   .or. activo .ne. @activo")
            //  19--> @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not. fecha .gt. @fecha   .or. activo .ne. @activo  .and.  km  .gt. @km")

            WhereDescomposed whereDescomposed = repositoryMethod.getWhereDescomposed();
            switch (repositoryMethod.getTokenWhere().size()) {
                case 3:
                    /**
                     * @Delete(where = "idoceano .eq. @idoceano")
                     * Filter.{comparatorList[0]} ( "{fieldList[0]}",
                     * {paramList[0]})
                     */
                    filter += "Filters." + whereDescomposed.comparator(0) + "(" + "\"" + whereDescomposed.field(0) + "\"," + whereDescomposed.param(0) + ");\n";

                    break;

                case 7:
                    /**
                     * @Delete(where = "idoceano .eq. @idoceano .and. oceano
                     * .ne. @oceano ") Filter.{logicalList[0]} (
                     * Filter.{comparatorList[0]} ( "{fieldList[0]}",
                     * {paramList[0]}) ,Filter.{comparatorList[1]} (
                     * "{fieldList[1]}", {paramList[1]}) );
                     */
                    filter += "Filters." + whereDescomposed.logical(0) + "(\n"
                            + "\t\tFilters." + whereDescomposed.comparator(0) + "(" + "\"" + whereDescomposed.field(0) + "\"," + whereDescomposed.param(0) + ")\n"
                            + "\t\t,Filters." + whereDescomposed.comparator(1) + "(" + "\"" + whereDescomposed.field(1) + "\"," + whereDescomposed.param(1) + ")\n"
                            + "\t\t);\n";

                    break;
                case 11:
                    /**
                     * Filter.{logicalList[0]} ( Filter.{comparatorList[0]} (
                     * "{fieldList[0]}", {paramList[0]})
                     * ,Filter.{comparatorList[1]} ( "{fieldList[1]}",
                     * {paramList[1]}) ,Filter.{logicalList[1]}(
                     * Filter.{comparatorList[2]} ( "{fieldList[2]}",
                     * {paramList[2]}) ) );
                     */
                    //@Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not. fecha .gt. @fecha")
                    filter += "Filters." + whereDescomposed.logical(0) + "(\n"
                            + "\t\t\tFilters." + whereDescomposed.comparator(0) + "(" + "\"" + whereDescomposed.field(0) + "\"," + whereDescomposed.param(0) + ")\n"
                            + "\t\t\t,Filters." + whereDescomposed.comparator(1) + "(" + "\"" + whereDescomposed.field(1) + "\"," + whereDescomposed.param(1) + ")\n"
                            + "\t\t\t,Filters." + whereDescomposed.logical(1) + "(\n"
                            + "\t\t\t\tFilters." + whereDescomposed.comparator(2) + "(" + "\"" + whereDescomposed.field(2) + "\"," + whereDescomposed.param(2) + ")\n"
                            + "\t\t\t\t)\n"
                            + "\t\t);\n";
                    break;
                case 15:
                    /**
                     * @Delete(where = "idoceano .eq. @idoceano .and. oceano
                     * .ne. @oceano .not. fecha .gt. @fecha .or. activo .ne.
                     * @activo") Filter.{logicalList[0]} (
                     * Filter.{comparatorList[0]} ( "{fieldList[0]}",
                     * {paramList[0]}) ,Filter.{comparatorList[1]} (
                     * "{fieldList[1]}", {paramList[1]})
                     * ,Filter.{logicalList[1]}( Filter.{comparatorList[2]} (
                     * "{fieldList[2]}", {paramList[2]}) )
                     * ,Filter.{logicalList[2]}( Filter.{comparatorList[3]} (
                     * "{fieldList[3]}", {paramList[3]}) ) );
                     */
                    filter += "Filters." + whereDescomposed.logical(0) + "(\n"
                            + "\t\t\tFilters." + whereDescomposed.comparator(0) + "(" + "\"" + whereDescomposed.field(0) + "\"," + whereDescomposed.param(0) + ")\n"
                            + "\t\t\t,Filters." + whereDescomposed.comparator(1) + "(" + "\"" + whereDescomposed.field(1) + "\"," + whereDescomposed.param(1) + ")\n"
                            + "\t\t\t,Filters." + whereDescomposed.logical(1) + "(\n"
                            + "\t\t\t\tFilters." + whereDescomposed.comparator(2) + "(" + "\"" + whereDescomposed.field(2) + "\"," + whereDescomposed.param(2) + ")\n"
                            + "\t\t\t\t)\n"
                            + "\t\t\t,Filters." + whereDescomposed.logical(2) + "(\n"
                            + "\t\t\t\tFilters." + whereDescomposed.comparator(3) + "(" + "\"" + whereDescomposed.field(3) + "\"," + whereDescomposed.param(3) + ")\n"
                            + "\t\t\t\t)\n"
                            + "\t\t);\n";
                    break;
                case 19:
                    /**
                     * @Delete(where = "idoceano .eq. @idoceano .and. oceano
                     * .ne. @oceano .not. fecha .gt. @fecha .or. activo .ne.
                     * @activo .and. km .gt. @km") Filter.{logicalList[0]} (
                     * Filter.{comparatorList[0]} ( "{fieldList[0]}",
                     * {paramList[0]}) ,Filter.{comparatorList[1]} (
                     * "{fieldList[1]}", {paramList[1]})
                     * ,Filter.{logicalList[1]}( Filter.{comparatorList[2]} (
                     * "{fieldList[2]}", {paramList[2]}) )
                     * ,Filter.{logicalList[2]}( Filter.{comparatorList[3]} (
                     * "{fieldList[3]}", {paramList[3]}) )
                     *
                     * ,Filter.{logicalList[3]}( Filter.{comparatorList[4]} (
                     * "{fieldList[4]}", {paramList[4]}) ) );
                     */
                    filter += "Filters." + whereDescomposed.logical(0) + "(\n"
                            + "\t\t\tFilters." + whereDescomposed.comparator(0) + "(" + "\"" + whereDescomposed.field(0) + "\"," + whereDescomposed.param(0) + ")\n"
                            + "\t\t\t,Filters." + whereDescomposed.comparator(1) + "(" + "\"" + whereDescomposed.field(1) + "\"," + whereDescomposed.param(1) + ")\n"
                            + "\t\t\t,Filters." + whereDescomposed.logical(1) + "(\n"
                            + "\t\t\t\tFilters." + whereDescomposed.comparator(2) + "(" + "\"" + whereDescomposed.field(2) + "\"," + whereDescomposed.param(2) + ")\n"
                            + "\t\t\t\t)\n"
                            + "\t\t\t,Filters." + whereDescomposed.logical(2) + "(\n"
                            + "\t\t\t\tFilters." + whereDescomposed.comparator(3) + "(" + "\"" + whereDescomposed.field(3) + "\"," + whereDescomposed.param(3) + ")\n"
                            + "\t\t\t\t)\n"
                            + "\t\t\t,Filters." + whereDescomposed.logical(3) + "(\n"
                            + "\t\t\t\tFilters." + whereDescomposed.comparator(4) + "(" + "\"" + whereDescomposed.field(4) + "\"," + whereDescomposed.param(4) + ")\n"
                            + "\t\t\t\t)\n"
                            + "\t\t);\n";
                    break;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return filter;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String generateFilterForFindAndCountAndDelet(RepositoryMethod repositoryMethod)">
    /**
     *
     * @param repositoryMethod
     * @return Genera la sentencia Filter para el Delete Guia de implementación
     * https://github.com/avbravo/books/blob/main/01.jmoordb/01.00%20jmoordb-core-processor/01.14%20@Delete.md
     */
    public static String generateFilterForFindAndCountAndDelete(RepositoryMethod repositoryMethod) {
        String codeBeforeForDate = "";
        String filter = "Bson filter =";
        try {
            //                          0        1     2         3    4      5    6       7    8     9     10      11   12     13    14        15  16   17   18
            // 4 --> @Delete(where =  "idoceano .eq. @idoceano  ")
            // 7 --> @Delete(where =  "idoceano .eq. @idoceano .and. oceano .eq. @oceano ")
            //  10--> @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not. fecha .gt. @fecha")
            //  15--> @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not. fecha .gt. @fecha   .or. activo .ne. @activo")
            //  19--> @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not. fecha .gt. @fecha   .or. activo .ne. @activo  .and.  km  .gt. @km")

            /**
             * Se obtiene la posición donde estan los operadores logicos
             */
            /**
             * Wordl and Lexemas locales
             */
            List<Integer> positionLogicalList = NameOfMethodAnalizerUtil.positionOfTypeOfLexema(repositoryMethod.getLexemas(), "L");

            Integer count = 0;

//            aqui validar si el nombre del campo esta incluido
//                    con includeTimes o excludetimes, no si esta se asume que es excludeTime
            System.out.println("Quitar++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("[getNameOfMethod()] " + repositoryMethod.getNameOfMethod());
            System.out.println(" [getName()]" + repositoryMethod.getParamTypeElement().get(0).getName());
            System.out.println(" [getType()]" + repositoryMethod.getParamTypeElement().get(0).getType());
//             if(repositoryMethod.getExcludeTimeFields().size()==0 ){
            System.out.println("  [ getEcludeTimeFields.size()  " + repositoryMethod.getExcludeTimeFields().size());
            System.out.println("  [ getIncludeTimeFields().size()  " + repositoryMethod.getIncludeTimeFields().size());
//             }

            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");

//            AQUI FALTA VALIDAR CUANDO ES EXCLUDE TIME QUE PASA CUANDO ES EN HORAS 0
//            Y HORA 23.
//            COMO SABERLO PARECE QUE SE DEBE USAR OTRAS ANOTACIONES
//                    @STARTIME00
//                    @ENDTIME23 O SI EL COMPARADOR ES GT
//            , GTE USAR HORA 0,0,0,
//           SI EL COMPARADOR ES LT
//            , LTE USAR HORA 23,59,59
            if (isTypeDate(repositoryMethod.getParamTypeElement().get(0).getType())) {
                System.out.println("[ Es tipo Fecha ]");
                if (isIncludeTime(repositoryMethod, repositoryMethod.getParamTypeElement().get(0).getName())) {
                    System.out.println(" [ Es IncludeTiem la hora debe viajar igual ]");
                } else {
                    System.out.println(" [ Es ExcludeTiem se debe genera los isodate con valores 0,0,0]");
                    String comparatorVar = lexemaToMongoComparator(repositoryMethod.getWorldAndToken().get(1));
                    if (comparatorVar.equals("gt") || comparatorVar.equals("gte")) {
                        //  Aqui SE USA EL METODO  que asigne a 0 las horas mimnutos y segundos
                        codeBeforeForDate += "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ");\n";
                    } else {
                        if (comparatorVar.equals("lt") || comparatorVar.equals("lte")) {
//                            Aqui SE USA EL METODO que asigne a 23 las horas 59 mimnutos y 59 segundos JmoordbCoreDateUtil
//                            .dateToLocalDateTimeLasttHourOfDay(Date fecha
//                        
//                    
//                
//            
//            ) 
                        }
                    }
                }
            }

            if (repositoryMethod.getParamTypeElement().size() == 1) {
                // Un solo parametro se crea el filtro en base al nombre
                /**
                 * Verificar si es de tipo Fecha , se debe analizar si es
                 * IncludeTime
                 */
                String comparator = lexemaToMongoComparator(repositoryMethod.getWorldAndToken().get(1));
                if (isTypeDate(repositoryMethod.getParamTypeElement().get(0).getType())) {

                    if (isIncludeTime(repositoryMethod, repositoryMethod.getParamTypeElement().get(0).getName())) {
                        /**
                         * Si es IncludeTime no se debe realizar ninguna
                         * operación especial.
                         */

                        filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + repositoryMethod.getParamTypeElement().get(0).getName() + ");\n";
                    } else {
                        /**
                         * Si es ExcludeTime se debe validar el operador
                         */

                        if (comparator.equals("gt") || comparator.equals("gte")) {
                            // mayor igual se debe generar primeras horas del dia para que lo incluya
                            if (comparator.equals("gte")) {
                                codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";
                            } else {
                                // Si es mayor se debe indicar como base la primera hora del dia siguiente
                                codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeNextDayFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";
                            }

                        } else {
                            if (comparator.equals("lt") || comparator.equals("lte")) {
                                // Si es menor o menor o igual se debe generar las ultimas horas del dia
                                if (comparator.equals("lte")) {
                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                    filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";
                                } else {
//                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeBeforeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                    filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";
                                }

                            } else {
                                if (comparator.equals("eq")) {
// Si es igual, se debe generar un filtro between, es decir mayor o igual que las horas 0 t menor o igual que als horas 24.

                                    filter += "Filters.and(\n";
                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                    filter += "Filters.gte" + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + "),\n";

                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                    filter += "Filters.lte" + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ")\n";
                                    filter += ");\n";
                                }
                            }
                        }
                    }
                } else {
// No es una fecha
                    filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + repositoryMethod.getParamTypeElement().get(0).getName() + ");\n";
                }

            } else {
                /**
                 * MAS DE UN PARAMETRO
                 */
                if (positionLogicalList.size() == 0) {
                    //No tiene operadores logicos
//AQUI APLICAR LAS CONDICIONES DE SI ES FECHA Y LOS DEMAS
                    String comparator = lexemaToMongoComparator(repositoryMethod.getWorldAndToken().get(1));
                    if (isTypeDate(repositoryMethod.getParamTypeElement().get(0).getType())) {
                        if (isIncludeTime(repositoryMethod, repositoryMethod.getParamTypeElement().get(0).getName())) {
                            filter += "Filters." + lexemaToMongoComparator(repositoryMethod.getWorldAndToken().get(1)) + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + repositoryMethod.getParamTypeElement().get(0).getName() + ");\n";
                        } else {
                            /**
                             * Si es excludeTime se debe validar el operador
                             */

                            if (comparator.equals("gt") || comparator.equals("gte")) {
                                //  Si es mayor| mayor igual se debe generar primeras horas del dia
//                                codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
//                                filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";

                                // mayor igual se debe generar primeras horas del dia para que lo incluya
                                if (comparator.equals("gte")) {
                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                    filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";
                                } else {
                                    // Si es mayor se debe indicar como base la primera hora del dia siguiente
                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeNextDayFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                    filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";
                                }

                            } else {
                                if (comparator.equals("lt") || comparator.equals("lte")) {
                                    // Si es menor o menor o igual se debe generar las ultimas horas del dia
//                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
//
//                                    filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";
                                    if (comparator.equals("lte")) {
                                        codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                        filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";
                                    } else {
//                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                        codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeBeforeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                        filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ");\n";
                                    }
                                } else {
                                    if (comparator.equals("eq")) {
// Si es igual, se debe generar un filtro between, es decir mayor o igual que las horas 0 t menor o igual que als horas 24.

//                                        filter += "Filters.and(\n";
//                                        codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
//                                        filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + "),\n";
//
//                                        codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
//                                        filter += "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ")\n";
//                                        filter += ");\n";
                                        filter += "Filters.and(\n";
                                        codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                        filter += "Filters.gte" + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + "),\n";

                                        codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                        filter += "Filters.lte" + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + codeBeforeForDate + ")\n";
                                        filter += ");\n";
                                    }
                                }
                            }
                        }
                    } else {
                        filter += "Filters." + lexemaToMongoComparator(repositoryMethod.getWorldAndToken().get(1)) + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0)) + "\"," + repositoryMethod.getParamTypeElement().get(0).getName() + ");\n";
                    }

                } else {
                    /**
                     * Tiene operadores Logicos
                     */
                    Integer contadorParametro = 0;
                    Integer indexLogical = 0;
                    filter += "Filters." + lexemaToMongoComparator(repositoryMethod.getWorldAndToken().get(positionLogicalList.get(indexLogical))) + "(\n";
                    Integer next = 0;

                    Boolean controlLogical = Boolean.FALSE;
                    String comaSeparator = "";
                    for (int i = 0; i < repositoryMethod.getWorldAndToken().size(); i++) {

                        if (repositoryMethod.getLexemas().get(i).equals("F")) {
                            if (i != 0 && !controlLogical) {
                                comaSeparator = ",";
                                // filter += "\t\t\t,";
                            } else {
                                comaSeparator = "";

                            }
                            String field = JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(i));
//                            String parametro = repositoryMethod.getParamTypeElement().get(contadorParametro++).getName();
                            String parametro = repositoryMethod.getParamTypeElement().get(contadorParametro).getName();
                            String comparator = lexemaToMongoComparator(repositoryMethod.getWorldAndToken().get(i + 1));

                            //AQUI APLICAR LAS CONDICIONES DE SI ES FECHA Y LOS DEMAS 
                            if (isTypeDate(repositoryMethod.getParamTypeElement().get(contadorParametro).getType())) {
                                if (isIncludeTime(repositoryMethod, repositoryMethod.getParamTypeElement().get(contadorParametro).getName())) {
                                    filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + field + "\"," + parametro + ")\n";
                                } else {
                                    /**
                                     * Si es excludeTime se debe validar el
                                     * operador
                                     */

                                    if (comparator.equals("gt") || comparator.equals("gte")) {
                                        //  Si es mayor| mayor igual se debe generar primeras horas del dia
//                                        codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
//
//                                        filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + field + "\"," + codeBeforeForDate + ")\n";
//                                        
                                        if (comparator.equals("gte")) {
                                            codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
                                            filter +="\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(contadorParametro)) + "\"," + codeBeforeForDate + ")\n";
                                        } else {
                                            // Si es mayor se debe indicar como base la primera hora del dia siguiente
                                            codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeNextDayFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
                                            filter += "\t\t\t" + comaSeparator +"Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(contadorParametro)) + "\"," + codeBeforeForDate + ")\n";
                                        }

                                    } else {
                                        if (comparator.equals("lt") || comparator.equals("lte")) {
                                            // Si es menor o menor o igual se debe generar las ultimas horas del dia
//                                            codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
//                                            filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + field + "\"," + codeBeforeForDate + ")\n";
//                                            

                                            if (comparator.equals("lte")) {

                                                codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
//                                                filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(contadorParametro)) + "\"," + codeBeforeForDate + ")\n";
                                                filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + field + "\"," + codeBeforeForDate + ")\n";
                                            } else {
//                                    codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(0).getName() + ")";
                                                codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeBeforeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
//                                                filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(contadorParametro)) + "\"," + codeBeforeForDate + ")\n";
                                                filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + field + "\"," + codeBeforeForDate + ")\n";
                                            }

                                        } else {
                                            if (comparator.equals("eq")) {
// Si es igual, se debe generar un filtro between, es decir mayor o igual que las horas 0 t menor o igual que als horas 24.

//                                                filter += "Filters.and(\n";
//                                                codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
//                                                filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + field + "\"," + codeBeforeForDate + "),\n";
//
//                                                codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
//                                                filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + field + "\"," + codeBeforeForDate + ")\n";
//                                                filter += ")\n";
                                                filter += "Filters.and(\n";
                                                codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeFirstHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
                                                filter += "\t\t\t" + comaSeparator + "Filters.gte" + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(contadorParametro)) + "\"," + codeBeforeForDate + "),\n";

                                                codeBeforeForDate = "JmoordbCoreDateUtil.dateToLocalDateTimeLastHourOfDay(" + repositoryMethod.getParamTypeElement().get(contadorParametro).getName() + ")";
                                                filter += "\t\t\t" + comaSeparator +  "Filters.lte" + "(" + "\"" + JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(contadorParametro)) + "\"," + codeBeforeForDate + ")\n";
                                                filter += ")\n";
                                            }
                                        }
                                    }
                                }

                            } else {
                                filter += "\t\t\t" + comaSeparator + "Filters." + comparator + "(" + "\"" + field + "\"," + parametro + ")\n";
                            }
                            contadorParametro++;
                            if (controlLogical) {
                                //Cierra el Filter interno que se creo
                                filter += "\t\t\t)\n";
                                controlLogical = Boolean.FALSE;
                            }
                        } else {
                            if (repositoryMethod.getLexemas().get(i).equals("C") || repositoryMethod.getLexemas().get(i).equals("P") || repositoryMethod.getLexemas().get(i).equals("S")) {
                                //si es Comparator, Pagination, Sorted no se realiza ninguna acción
                            } else {
                                if (repositoryMethod.getLexemas().get(i).equals("L")) {
                                    if (indexLogical == 0) {
                                        // indica que es el primer logico que se usa como principal de todas las condiciones
                                        indexLogical++;
                                    } else {
                                        controlLogical = Boolean.TRUE;
                                        filter += "\t\t\t,Filters." + lexemaToMongoComparator(repositoryMethod.getWorldAndToken().get(positionLogicalList.get(indexLogical++))) + "(\n";
                                    }

                                }
                            }
                        }

                    }

                    filter += "\t\t);\n";
                }

            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return filter;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String paramPagination(List<String> tokenList) ">
    /**
     *
     * @param tokenList
     * @return El campo usado como pagination este es seguido del atributo
     * .skip.
     */
    public static String paramPagination(List<String> tokenList) {
        String fieldPagination = "";
        try {
            Boolean skipFound = Boolean.FALSE;
            for (String s : tokenList) {
                if (skipFound) {
                    fieldPagination = s.trim().substring(1);
                    break;
                }
                if (s.trim().equals(".skip.")) {
                    skipFound = Boolean.TRUE;
                }

            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return fieldPagination;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String lexemaToMongoComparator(String lexema)">
    private static String lexemaToMongoComparator(String lexema) {
        String result = "eq";
        try {
            switch (lexema.toLowerCase()) {
                case "equal":
                    result = "eq";
                    break;
                case "notequal":
                    result = "ne";
                    break;
                case "notequals":
                    result = "ne";
                    break;
                case "lessthanequal":
                    result = "lte";
                    break;
                case "lessthanequals":
                    result = "lte";
                    break;
                case "lessthan":
                    result = "lt";
                    break;

                case "greaterthanequal":
                    result = "gte";
                    break;
                case "greaterthanequals":
                    result = "gte";
                    break;
                case "greaterthan":
                    result = "gt";
                    break;

                case "like":
                    result = "like";
                    break;
                case "and":
                    result = "and";
                    break;
                case "or":
                    result = "or";
                    break;
                case "not":
                    result = "not";
                    break;
            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String parametersOfMethod(RepositoryMethod repositoryMethod)">
    /**
     *
     * @param repositoryMethod
     * @return Los parametros del metodo como una cadena
     */
    public static String parametersOfMethod(RepositoryMethod repositoryMethod) {
        String param = "";
        try {
            for (ParamTypeElement p : repositoryMethod.getParamTypeElement()) {
                if (param != "") {
                    param += ",";
                }
                param += p.getType() + " " + p.getName();
            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return param;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String editorFold(RepositoryMethod repositoryMethod, String param)">
    public static String editorFold(RepositoryMethod repositoryMethod, String param) {
        String editorFoldStart = "";
        String result = "";
        try {
            editorFoldStart = repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "(" + param + " ) ";
            result = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + editorFoldStart + "\">";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String editorFold(String text)">

    public static String editorFold(String text) {
        String editorFoldStart = "";
        String result = "";
        try {
            editorFoldStart = text;
            result = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + editorFoldStart + "\">";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String editorFold(EntityData entityData)">

    public static String editorFold(EntityData entityData) {
        String editorFoldStart = "";
        String result = "";
        try {

            editorFoldStart = " public " + JmoordbCoreUtil.letterToUpper(entityData.getEntityName()) + " get(Supplier<? extends" + entityData.getEntityName() + "> s, Document document) ";
            result = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + editorFoldStart + "\">";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String editorFold(DateSupportData dateSupportData)">

    public static String editorFold(DateSupportData dateSupportData) {
        String editorFoldStart = "";
        String result = "";
        try {

            editorFoldStart = " public " + JmoordbCoreUtil.letterToUpper(dateSupportData.getDateSupportName()) + " get(Supplier<? extends" + dateSupportData.getDateSupportName() + "> s, Document document) ";
            result = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + editorFoldStart + "\">";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String editorFold(EntityData entityData)">

    public static String editorFoldToJson(EntityData entityData) {
        String editorFoldStart = "";
        String result = "";
        try {

            editorFoldStart = " public String toJson (" + JmoordbCoreUtil.letterToUpper(entityData.getEntityName()) + " " + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ") ";
            result = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + editorFoldStart + "\">";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String editorFold(EntityDateSupportData dateSupportData)">

    public static String editorFoldToJson(DateSupportData dateSupportData) {
        String editorFoldStart = "";
        String result = "";
        try {

            editorFoldStart = " public String toJson (" + JmoordbCoreUtil.letterToUpper(dateSupportData.getDateSupportName()) + " " + JmoordbCoreUtil.letterToLower(dateSupportData.getDateSupportName()) + ") ";
            result = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + editorFoldStart + "\">";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String editorFoldToDocument(EntityData entityData)">

    public static String editorFoldToDocument(EntityData entityData) {
        String editorFoldStart = "";
        String result = "";
        try {

            editorFoldStart = " public Document toDocument (" + JmoordbCoreUtil.letterToUpper(entityData.getEntityName()) + " " + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ") ";
            result = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + editorFoldStart + "\">";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String editorFoldToDocument(DateSupportData dateSupportData)">

    public static String editorFoldToDocument(DateSupportData dateSupportData) {
        String editorFoldStart = "";
        String result = "";
        try {

            editorFoldStart = " public Document toDocument (" + JmoordbCoreUtil.letterToUpper(dateSupportData.getDateSupportName()) + " " + JmoordbCoreUtil.letterToLower(dateSupportData.getDateSupportName()) + ") ";
            result = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + editorFoldStart + "\">";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String editorFold(EntityData entityData)">

    public static String editorFold(DocumentEmbeddableData documentEmbeddableData) {
        String editorFoldStart = "";
        String result = "";
        try {

            editorFoldStart = " public " + JmoordbCoreUtil.letterToUpper(documentEmbeddableData.getDocumentEmbeddableName()) + " get(Supplier<? extends" + documentEmbeddableData.getDocumentEmbeddableName() + "> s, Document document) ";
            result = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + editorFoldStart + "\">";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean parametersHaveSearchType(RepositoryMethod repositoryMethod)">
    /**
     *
     * @param repositoryMethod
     * @return Valida si un parametro es de tipo eearch
     */
    public static Boolean parametersHaveSearchType(RepositoryMethod repositoryMethod) {
        try {
            for (ParamTypeElement p : repositoryMethod.getParamTypeElement()) {
                if (p.getType().toString().equals("com.jmoordb.core.model.Search")) {
                    return Boolean.TRUE;
                }

            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return Boolean.FALSE;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String getPackageName(Element element)">
    public static String getPackageName(Element element) {
        List<PackageElement> packageElements
                = ElementFilter.packagesIn(Arrays.asList(element.getEnclosingElement()));

        Optional<PackageElement> packageElement = packageElements.stream().findAny();
        return packageElement.isPresent()
                ? packageElement.get().getQualifiedName().toString() : null;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String getTypeName(Element e)">
    /**
     * Get the simple name of the TypeMirror
     */
    public static String getTypeName(Element e) {
        TypeMirror typeMirror = e.asType();
        String[] split = typeMirror.toString().split("\\.");
        try {
        } catch (Exception ex) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + ex.getLocalizedMessage());
        }
        return split.length > 0 ? split[split.length - 1] : null;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ReturnType convertToReturnTypeOfField(String returnTypeField)">
    public static ReturnType convertToReturnTypeOfField(String returnTypeField) {
        ReturnType returnType = ReturnType.NONE;
        try {
            String typeOptional = "java.util.Optional<";
            String typeList = "java.util.List<";
            String typeSet = "java.util.Set<";
            String typeStream = "java.util.stream.Stream<";
            String typeInteger = "java.lang.Integer";
            String typeBoolean = "java.lang.Boolean";

            if (returnTypeField.startsWith(typeOptional)) {
                return ReturnType.OPTIONAL;
            }
            if (returnTypeField.startsWith(typeList)) {
                return ReturnType.LIST;
            }
            if (returnTypeField.startsWith(typeSet)) {
                return ReturnType.SET;
            }
            if (returnTypeField.startsWith(typeInteger)) {
                return ReturnType.INTEGER;
            }
            if (returnTypeField.startsWith(typeBoolean)) {
                return ReturnType.BOOLEAN;
            }
            if (returnTypeField.startsWith(typeStream)) {
                return ReturnType.STREAM;
            }

            if (returnTypeField.startsWith("java.lang.String")) {
                return ReturnType.STRING;
            }
            if (returnTypeField.startsWith("java.util.Date")) {
                return ReturnType.DATE;
            }

            if (returnTypeField.startsWith("java.lang.Double")) {
                return ReturnType.DOUBLE;
            }
            if (returnTypeField.startsWith("java.lang.Float")) {
                return ReturnType.FLOAT;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return returnType;

    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String getTypeOfField(VariableElement variable">
    /**
     *
     * @param variable
     * @return Devuelve el tipo de Field declarado
     */
    public static String getTypeOfField(VariableElement variable) {
        return variable.asType().toString();

    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" public static Boolean isValidAnnotationForEntity(ExecutableElement executableElement)">
    /**
     * Valida que sea una anotación valida para un Entity
     *
     * @param executableElement
     * @return
     */
    public static Boolean isValidAnnotationForEntity(VariableElement variableElement) {
        Boolean isValid = Boolean.FALSE;
        try {
            Id id = variableElement.getAnnotation(Id.class);
            Column column = variableElement.getAnnotation(Column.class);
            Embedded embedded = variableElement.getAnnotation(Embedded.class);
            Referenced referenced = variableElement.getAnnotation(Referenced.class);

            if (id == null && column == null && embedded == null && referenced == null) {

            } else {
                return Boolean.TRUE;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" public static Boolean isValidAnnotationForEntity(ExecutableElement executableElement)">
    /**
     * Valida que sea una anotación valida para un Entity
     *
     * @param executableElement
     * @return
     */
    public static Boolean isValidAnnotationForDocumentEmbeddable(VariableElement variableElement) {
        Boolean isValid = Boolean.FALSE;
        try {
            Id id = variableElement.getAnnotation(Id.class);
            Column column = variableElement.getAnnotation(Column.class);
            Embedded embedded = variableElement.getAnnotation(Embedded.class);
            Referenced referenced = variableElement.getAnnotation(Referenced.class);

            if (id == null && column == null && embedded == null && referenced == null) {

            } else {
                return Boolean.TRUE;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="checkMethod(ExecutableElement method)">
    public static void checkMethod(ExecutableElement method) {
        // check for valid name
        String name = method.getSimpleName().toString();

    }
// </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="analizarMetodosQuery(Element element)">
    /**
     * Esto es para analizar los metodos
     *
     * @param typeElement
     * @return
     */
    public static void analizarMetodosQueryd(Element element) {
        try {

            if (element.getKind() == ElementKind.METHOD) {
                // only handle methods as targets
                //MessagesUtil.msg("Es un metodo" + element.getSimpleName());
                checkMethod((ExecutableElement) element);
            } else {
                //  MessagesUtil.msg("No es un metodo " + element.getSimpleName());
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + e.getLocalizedMessage());
        }
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Boolean isValidAnnotationOfRepository(ExecutableElement executableElement)">
    /**
     * Verifica que tenga un anotación valida para el repositorio
     *
     * @param method
     * @return
     */
    public static Boolean isValidAnnotationOfRepository(ExecutableElement executableElement) {
        Boolean isValid = Boolean.FALSE;
        try {
            Query query = executableElement.getAnnotation(Query.class);
            Find find = executableElement.getAnnotation(Find.class);
            Lookup searcher = executableElement.getAnnotation(Lookup.class);
            Regex queryRegex = executableElement.getAnnotation(Regex.class);
            Count count = executableElement.getAnnotation(Count.class);
            RegexCount countRegex = executableElement.getAnnotation(RegexCount.class);
            Ping ping = executableElement.getAnnotation(Ping.class);
            Save save = executableElement.getAnnotation(Save.class);
            Delete delete = executableElement.getAnnotation(Delete.class);
            DeleteBy deleteBy = executableElement.getAnnotation(DeleteBy.class);
            Update update = executableElement.getAnnotation(Update.class);
            CountBy countBy = executableElement.getAnnotation(CountBy.class);
            LikeBy likeBy = executableElement.getAnnotation(LikeBy.class);
            CountLikeBy countlikeBy = executableElement.getAnnotation(CountLikeBy.class);
            if (countlikeBy == null && likeBy == null && countBy == null && deleteBy == null && find == null && query == null && searcher == null && queryRegex == null && count == null && countRegex == null && ping == null && save == null && delete == null && update == null) {

            } else {
                return Boolean.TRUE;
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return isValid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String methodToFieldNameOfRepository(String methodName)">
    /**
     *
     * @param methodName
     * @return
     */
    public static String methodToFieldNameOfRepository(String methodName) {
        if (methodName.startsWith("get")) {
            String str = methodName.substring(3);
            if (str.length() == 0) {
                return null;
            } else if (str.length() == 1) {
                return str.toLowerCase();
            } else {
                return Character.toLowerCase(str.charAt(0)) + str.substring(1);
            }
        } else {

        }
        if (methodName.startsWith("find")) {
            String str = methodName.substring(4);
            if (str.length() == 0) {
                return null;
            } else if (str.length() == 1) {
                return str.toLowerCase();
            } else {
                return Character.toLowerCase(str.charAt(0)) + str.substring(1);
            }
        } else {
            if (methodName.startsWith("delete")) {
                String str = methodName.substring(6);
                if (str.length() == 0) {
                    return null;
                } else if (str.length() == 1) {
                    return str.toLowerCase();
                } else {
                    return Character.toLowerCase(str.charAt(0)) + str.substring(1);
                }
            }
        }
        return null;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="method()">
    private static Boolean isIncludeTime(RepositoryMethod repositoryMethod, String parameter) {
        Boolean result = Boolean.FALSE;
        try {
            if (repositoryMethod.getIncludeTimeFields().size() == 0) {

            } else {
                for (String field : repositoryMethod.getIncludeTimeFields()) {
                    if (field.equals(parameter)) {
                        result = Boolean.TRUE;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("isIncludeTime() " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Boolean isTypeDate(String typeParametro) ">
    private static Boolean isTypeDate(String typeParametro) {
        Boolean result = Boolean.FALSE;

        try {
            if (typeParametro.equals("java.util.Date") || typeParametro.equals("java.time.LocalDateTime")) {
                result = Boolean.TRUE;
            }

        } catch (Exception e) {
            System.out.println("isTypeDate() " + e.getLocalizedMessage());
        }

        return result;
    }
// </editor-fold>
}
