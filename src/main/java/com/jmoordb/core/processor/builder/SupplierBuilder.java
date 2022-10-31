package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.enumerations.ReturnType;
import com.jmoordb.core.annotation.enumerations.TypeReferenced;
import com.jmoordb.core.processor.methods.EntityField;
import com.jmoordb.core.processor.model.EntityData;
import com.jmoordb.core.processor.model.IdData;
import com.jmoordb.core.util.JmoordbCoreFileUtil;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.util.List;
import javax.lang.model.element.Element;
import org.bson.Document;

public class SupplierBuilder {

  public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

    // <editor-fold defaultstate="collapsed" desc="StringBuilder get(EntityData entityData, List<EntityField> entityFieldList, Element element)">
    public static StringBuilder get(EntityData entityData, List<EntityField> entityFieldList, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            Boolean haveEmbedded = SupplierSourceBuilderUtil.haveEmbedded(entityFieldList);
            Boolean haveReferenced = SupplierSourceBuilderUtil.haveReferenced(entityFieldList);

            String sentence = "\t";
            String cast = "";
            for (EntityField entityField : entityFieldList) {
                switch (entityField.getAnnotationType()) {
                    case EMBEDDED:
                        sentence += embeddedProcess(entityData, entityField);
                        break;
                    case REFERENCED:

                        if (entityField.getTypeReferenced().equals(TypeReferenced.EMBEDDED)) {

                            sentence += embeddedProcess(entityData, entityField);
                        } else {
                            sentence += referencedProcess(entityData, entityField, element);
                        }

                        break;
                    case ID:
                        cast = castConverter(entityField.getReturnTypeValue(), entityField.getNameOfMethod());
                        sentence += "\t " + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".set" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "(" + cast + ");\n";
                        break;
                    case COLUMN:
                        if (entityField.getReturnType().equals(ReturnType.DATE)) {
                            // sentence += "\t\tjava.util.Date " + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "IsoDate = com.jmoordb.core.util.JmoordbCoreDateUtil.stringToDate(com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate("+JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "()));\n";
//                            sentence += "\t\tjava.util.Date " + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "IsoDate = com.jmoordb.core.util.JmoordbCoreDateUtil.stringToDate(com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate("+JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "()));\n";
//                         sentence +=" System.out.println(\"Quitar:ISODATE \"+com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate("+JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "() ) );\n";
//                         sentence +=" System.out.println(\"Quitar: stringToISODATE \"+com.jmoordb.core.util.JmoordbCoreDateUtil.stringToDate(com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate("+JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "() ) ));\n";
//                                 
//                       String isoDate ="com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate("+JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "())";
//                            sentence += "\t\tsb.append(\"" + coma + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\\\":\\\"\").append(" +  isoDate+ ").append(\"\\\"\");\n";
//                            sentence += "\t\tsb.append(\"" + coma + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\\\":\\\"\").append(" + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "IsoDate"+ ").append(\"\\\"\");\n";
//                            sentence += "\t\tsb.append(\"" + coma + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\\\":\\\"\").append(" + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "IsoDate"+ ").append(\"\\\"\");\n";

                           // sentence += "System.out.println(\"Quitar.Document.getString()\"+com.jmoordb.core.util.JmoordbCoreDateUtil.stringToISODate(document.getString(\"" + entityField.getNameOfMethod() + "\")));\n";
                            cast = castConverter(entityField.getReturnTypeValue(), entityField.getNameOfMethod());
                            sentence += "\t" + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".set" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "(" + cast + ");\n";
//                            cast = "com.jmoordb.core.util.JmoordbCoreDateUtil.stringToISODate(document.getString(\"" + entityField.getNameOfMethod() + "\"))";
//                            sentence += "\t" + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".set" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "(" + cast + ");\n";
                        } else {
                            cast = castConverter(entityField.getReturnTypeValue(), entityField.getNameOfMethod());
                            sentence += "\t" + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".set" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "(" + cast + ");\n";
                        }

                        break;

                }

            }

            String code
                    = ProcessorUtil.editorFold(entityData) + "\n\n"
                    + "    public " + entityData.getEntityName() + " get(Supplier<? extends " + entityData.getEntityName() + "> s, Document document) {\n"
                    + "        " + JmoordbCoreUtil.letterToUpper(entityData.getEntityName()) + " " + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + "= s.get(); \n"
                    + "        try {\n"
                    + "         // Quitar\n"
                    + "             System.out.println(\"Quitar Supplier \"+document.toJson());"
                    + sentence + "\n"
                    + "         } catch (Exception e) {\n"
                    + "              MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "         }\n"
                    + "         return " + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ";\n"
                    + "     }\n"
                    + "// </editor-fold>\n";

            builder.append(code);
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String castConverter(String returnTypeString, String fieldName)">
    public static String castConverter(String returnTypeString, String fieldName) {
        String result = "";
        try {
            if (returnTypeString.startsWith("java.lang.String")) {
                return "document.getString(\"" + fieldName + "\")";
            }
            if (returnTypeString.startsWith("java.lang.Integer")) {
                return "document.getInteger(\"" + fieldName + "\")";
            }
            if (returnTypeString.startsWith("java.lang.Double")) {
                return "document.getDouble(\"" + fieldName + "\")";
            }
            if (returnTypeString.startsWith("java.util.Date")) {
                return "document.getDate(\"" + fieldName + "\")";
            }
            if (returnTypeString.startsWith("java.time.LocalDateTime")) {
                
               String line ="LocalDateTime.of(JmoordbCoreDateUtil.anioDeUnaFecha(document.getDate(\""+fieldName+"\")), \n" 
                          +"\tJmoordbCoreDateUtil.mesDeUnaFecha(document.getDate(\""+fieldName+"\")), \n" 
                          +"\tJmoordbCoreDateUtil.diaDeUnaFecha(document.getDate(\""+fieldName+"\")), \n" 
                          +"\tJmoordbCoreDateUtil.horaDeUnaFecha(document.getDate(\""+fieldName+"\")), \n" 
                          +"\tJmoordbCoreDateUtil.minutosDeUnaFecha(document.getDate(\""+fieldName+"\")), \n" 
                          +"\tJmoordbCoreDateUtil.segundosDeUnaFecha(document.getDate(\""+fieldName+"\")))\n" ;
                
                return line;
            }
            if (returnTypeString.startsWith("java.lang.Boolean")) {
                return "document.getBoolean(\"" + fieldName + "\")";
            }
            if (returnTypeString.startsWith("java.lang.Long")) {
                return "document.getLong(\"" + fieldName + "\")";
            }
            if (returnTypeString.contains("ObjectId")) {
                return "document.getObjectId(\"" + fieldName + "\")";
            }
            if (returnTypeString.contains("Float")) {
                return "(Float)document.get(\"" + fieldName + "\")";
            }
            if (returnTypeString.contains("int")) {
                return "(int)document.get(\"" + fieldName + "\")";
            }
            if (returnTypeString.contains("List")) {

                String clase = returnTypeString.replace("java.util.List<", "");
                clase = clase.replace(">", "");

                return "document.getList(\"" + fieldName + "\"," + clase + ".class)";
            }
            if (returnTypeString.contains("Set")) {
                String clase = returnTypeString.replace("java.util.Set<", "");
                clase = clase.replace(">", "");

                return "new java.util.HashSet<>(document.getList(\"" + fieldName + "\"," + clase + ".class))";
            }
            if (returnTypeString.contains("Stream")) {
                String clase = returnTypeString.replace("java.util.stream.Stream<", "");
                clase = clase.replace(">", "");

                return "document.getList(\"" + fieldName + "\"," + clase + ".class).stream()";
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String embeddedProcess(EntityData entityData, EntityField entityField)">

    /**
     * Procesa los documentos embebidos
     *
     * @param entityData
     * @param entityField
     * @return
     */
    public static String embeddedProcess(EntityData entityData, EntityField entityField) {
        String result = "";
        String entityNameUpper = JmoordbCoreUtil.letterToUpper(entityData.getEntityName());
        String entityNameLower = JmoordbCoreUtil.letterToLower(entityData.getEntityName());
        String fieldUpper = JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod());
        String fieldLower = JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod());
        try {

            if (entityField.getReturnTypeValue().contains("List")) {

                result += "\t// Embedded List<" + fieldLower + ">\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";
                result += "\tList<Document> " + fieldLower + "Doc = (List) document.get(\"" + fieldLower + "\");\n";
                result += "\tfor( Document doc" + fieldUpper + " : " + fieldLower + "Doc){\n";

                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, doc" + fieldUpper + ");\n";
                result += "\t\t" + fieldLower + "List.add(" + fieldLower + ");\n";
                result += "\t};\n";
                result += "\t" + entityNameLower + ".set" + fieldUpper + "(" + fieldLower + "List);\n";

                return result;
            }
            if (entityField.getReturnTypeValue().contains("Set")) {
                result += "\t// Embedded Set<" + fieldLower + ">\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";
                result += "\tList<Document> " + fieldLower + "Doc = (List) document.get(\"" + fieldLower + "\");\n";
                result += "\tfor( Document doc" + fieldUpper + " : " + fieldLower + "Doc){\n";
                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, doc" + fieldUpper + ");\n";

                result += "\t\t" + fieldLower + "List.add(" + fieldLower + ");\n";
                result += "\t};\n";
                result += "\t" + entityNameLower + ".set" + fieldUpper + "(new java.util.HashSet<>(" + fieldLower + "List));\n";
                return result;
            }
            if (entityField.getReturnTypeValue().contains("Stream")) {
                result += "\t// Embedded Stream<" + fieldLower + ">\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";
                result += "\tList<Document> " + fieldLower + "Doc = (List) document.get(\"" + fieldLower + "\");\n";
                result += "\tfor( Document doc" + fieldUpper + " : " + fieldLower + "Doc){\n";

                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, doc" + fieldUpper + ");\n";
                result += "\t\t" + fieldLower + "List.add(" + fieldLower + ");\n";
                result += "\t};\n";
                result += "\t" + entityNameLower + ".set" + fieldUpper + "(" + fieldLower + "List.stream());\n";
                return result;
            }
            result += "\t// Embedded of " + fieldLower + "\n";
            result += "\tDocument " + fieldLower + "Doc = (Document) document.get(\"" + entityField.getNameOfMethod() + "\");\n";
            result += "\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, " + fieldLower + "Doc);\n";
            result += "\t" + entityNameLower + ".set" + fieldUpper + "(" + fieldLower + ");\n";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String referencedProcess(EntityData entityData, EntityField entityField)">

    /**
     * Procesa los documentos Referenciados
     *
     * @param entityData
     * @param entityField
     * @return
     */
    public static String referencedProcess(EntityData entityData, EntityField entityField, Element element) {
        String result = "";
        String entityNameUpper = JmoordbCoreUtil.letterToUpper(entityData.getEntityName());
        String entityNameLower = JmoordbCoreUtil.letterToLower(entityData.getEntityName());
        String fieldUpper = JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod());
        String fieldLower = JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod());

        IdData idData = new IdData();
        JmoordbCoreFileUtil.readIdAnnotationOfEntityFile(element, fieldUpper + ".java", idData);

        String as = entityField.getReferenced().from();

        String foreignField = idData.getFieldName();
        String from = entityField.getReferenced().from();
        String localField = entityField.getReferenced().localField();
        try {

            if (entityField.getReturnTypeValue().contains("List")) {

                result += "\t// Referenced List<" + fieldLower + ">\n";

                result += "\t List<Document> " + fieldLower + "DocumentList = (List)document.get(\"" + entityField.getReferenced().from() + "\");\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";

                result += "\tfor( Document " + fieldLower + "Doc :" + fieldLower + "DocumentList){\n";
                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new," + fieldLower + "Doc);\n";

                result += "\t\t Optional<" + fieldUpper + "> " + fieldLower + "Optional = " + fieldLower + "Repository.findByPk(" + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getReferenced().localField()) + "());\n";
                result += "\t\tif(" + fieldLower + "Optional.isPresent()){" + "\n";
                result += "\t\t\t" + fieldLower + "List.add(" + fieldLower + "Optional.get());\n";
                result += "\t\t}\n";
                result += "\t}\n";
                result += "\t" + entityNameLower + ".set" + fieldUpper + "(" + fieldLower + "List);\n";
                return result;
            }
            if (entityField.getReturnTypeValue().contains("Set")) {
                result += "\t// Referenced Set<" + fieldLower + ">\n";

                result += "\t List<Document> " + fieldLower + "DocumentList = (List)document.get(\"" + entityField.getReferenced().from() + "\");\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";

                result += "\tfor( Document " + fieldLower + "Doc :" + fieldLower + "DocumentList){\n";
                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new," + fieldLower + "Doc);\n";

                result += "\t\t Optional<" + fieldUpper + "> " + fieldLower + "Optional = " + fieldLower + "Repository.findByPk(" + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getReferenced().localField()) + "());\n";

                result += "\t\tif(" + fieldLower + "Optional.isPresent()){" + "\n";
                result += "\t\t\t" + fieldLower + "List.add(" + fieldLower + "Optional.get());\n";
                result += "\t\t}\n";
                result += "\t}\n";
                result += "\t" + entityNameLower + ".set" + fieldUpper + "(new java.util.HashSet<>(" + fieldLower + "List));\n";

                return result;
            }
            if (entityField.getReturnTypeValue().contains("Stream")) {
                result += "\t// Referenced Stream<" + fieldLower + ">\n";

                result += "\t List<Document> " + fieldLower + "DocumentList = (List)document.get(\"" + entityField.getReferenced().from() + "\");\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";

                result += "\tfor( Document " + fieldLower + "Doc :" + fieldLower + "DocumentList){\n";
                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new," + fieldLower + "Doc);\n";

                result += "\t\t Optional<" + fieldUpper + "> " + fieldLower + "Optional = " + fieldLower + "Repository.findByPk(" + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getReferenced().localField()) + "());\n";

                result += "\t\tif(" + fieldLower + "Optional.isPresent()){" + "\n";
                result += "\t\t\t" + fieldLower + "List.add(" + fieldLower + "Optional.get());\n";
                result += "\t\t}\n";
                result += "\t}\n";
                result += "\t" + entityNameLower + ".set" + fieldUpper + "(" + fieldLower + "List.stream());\n";

                return result;
            }
            result += "\t// Referenced of " + fieldLower + "\n";

            result += "\tDocument  " + fieldLower + "Doc  = (Document) document.get(\"" + entityField.getReferenced().from() + "\");\n";
            result += "\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new," + fieldLower + "Doc);\n";
            if (idData.getFieldType().equals("String")) {
//                result += "\tString id" + fieldUpper + "= document.getString(\"" + entityField.getReferenced().localField() + "\");\n";

                result += "\tString id" + fieldUpper + "= " + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getReferenced().localField()) + "();\n";

            } else {
                if (idData.getFieldType().equals("Long")) {
//                    result += "\tLong id" + fieldUpper + "= document.getLong(\"" + entityField.getReferenced().localField() + "\");\n";
                    result += "\tLong id" + fieldUpper + "= " + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getReferenced().localField()) + "();\n";

                }
            }

            result += "\t Optional<" + fieldUpper + "> " + fieldLower + "Optional = " + fieldLower + "Repository.findByPk(id" + fieldUpper + ");\n";
            result += "\tif(" + fieldLower + "Optional.isPresent()){" + "\n";
            result += "\t\t" + entityNameLower + ".set" + fieldUpper + "(" + fieldLower + "Optional.get());\n";
            result += "\t}\n";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="StringBuilder toJson(EntityData entityData, List<EntityField> entityFieldList, Element element)">
    public static StringBuilder toJson(EntityData entityData, List<EntityField> entityFieldList, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            Boolean haveEmbedded = SupplierSourceBuilderUtil.haveEmbedded(entityFieldList);
            Boolean haveReferenced = SupplierSourceBuilderUtil.haveReferenced(entityFieldList);

            String sentence = "\t StringBuilder sb = new StringBuilder();\n";
            sentence += "\t\tsb.append(\"{\");\n";
            String cast = "";
            String getMethod = "";
            Integer count = 0;
            String coma = "\\n \\\"";
            for (EntityField entityField : entityFieldList) {
                switch (entityField.getAnnotationType()) {
                    case EMBEDDED:
                        if (count > 0) {
                            coma = "\n,";
                        }
                        sentence += coma + embeddedProcess(entityData, entityField);
                        count++;
                        break;
                    case REFERENCED:
                        if (count > 0) {
                            coma = "\\n, \"";
                        }
                        if (entityField.getTypeReferenced().equals(TypeReferenced.EMBEDDED)) {

                            sentence += embeddedProcess(entityData, entityField);
                        } else {
                            sentence += "+" + coma + referencedProcess(entityData, entityField, element);
                        }
                        count++;
                        break;
                    case ID:
                        if (count > 0) {
                            coma = "\\n, \\\"";
                        }

                        getMethod = JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "()";
                        sentence += "\t\tsb.append(\"" + coma + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\\\":\\\"\").append(" + getMethod + ").append(\"\\\"\");\n";
                        count++;
                        break;
                    case COLUMN:
                        if (count > 0) {
                            coma = "\\n, \\\"";
                        }
                        if (entityField.getReturnType().equals(ReturnType.DATE)) {
                            // sentence += "\t\tjava.util.Date " + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "IsoDate = com.jmoordb.core.util.JmoordbCoreDateUtil.stringToDate(com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate("+JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "()));\n";
                            sentence += "\t\tjava.util.Date " + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "IsoDate = com.jmoordb.core.util.JmoordbCoreDateUtil.stringToDate(com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate(" + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "()));\n";
//                            sentence += " System.out.println(\"Quitar:ISODATE \"+com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate(" + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "() ) );\n";
//                            sentence += " System.out.println(\"Quitar: stringToISODATE \"+com.jmoordb.core.util.JmoordbCoreDateUtil.stringToDate(com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate(" + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "() ) ));\n";

                            String isoDate = "com.jmoordb.core.util.JmoordbCoreDateUtil.iSODate(" + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "())";
                            sentence += "\t\tsb.append(\"" + coma + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\\\":\\\"\").append(" + isoDate + ").append(\"\\\"\");\n";
//                            sentence += "\t\tsb.append(\"" + coma + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\\\":\\\"\").append(" + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "IsoDate"+ ").append(\"\\\"\");\n";
//                            sentence += "\t\tsb.append(\"" + coma + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\\\":\\\"\").append(" + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "IsoDate"+ ").append(\"\\\"\");\n";
                        } else {
                            getMethod = JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "()";
                            sentence += "\t\tsb.append(\"" + coma + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\\\":\\\"\").append(" + getMethod + ").append(\"\\\"\");\n";
                        }

                        count++;
                        break;

                }

            }

            sentence += "\t\tsb.append(\n\"}\");\n";
            sentence += "\treturn sb.toString();\n";
            String code
                    = ProcessorUtil.editorFoldToJson(entityData) + "\n\n"
                    + "    public String toJson(" + entityData.getEntityName() + " " + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ") {\n"
                    + sentence + "\n"
                    + "     }\n"
                    + "// </editor-fold>\n";

            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="StringBuilder toDocument(EntityData entityData, List<EntityField> entityFieldList, Element element)">
    public static StringBuilder toDocument(EntityData entityData, List<EntityField> entityFieldList, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            Boolean haveEmbedded = SupplierSourceBuilderUtil.haveEmbedded(entityFieldList);
            Boolean haveReferenced = SupplierSourceBuilderUtil.haveReferenced(entityFieldList);

            String sentence = "\t Document document = new Document();\n";

            String cast = "";
            String getMethod = "";
            Integer count = 0;
            String coma = "\\n \\\"";
            for (EntityField entityField : entityFieldList) {
                switch (entityField.getAnnotationType()) {
                    case EMBEDDED:
                        if (count > 0) {
//                            coma = "\n,";
                            coma = "\n";
                        }
                        sentence += coma + embeddedProcess(entityData, entityField);
                        count++;
                        break;
                    case REFERENCED:
                        if (count > 0) {
//                            coma = "\n, \"";
                            coma = "\n";
                        }
                        if (entityField.getTypeReferenced().equals(TypeReferenced.EMBEDDED)) {

                            sentence += embeddedProcess(entityData, entityField);
                        } else {
                         //   sentence += "+// Embedded of" + coma + referencedProcess(entityData, entityField, element);
                            sentence += " " + coma + referencedProcess(entityData, entityField, element);
                        }
                        count++;
                        break;
                    case ID:
                        if (count > 0) {
                            coma = "\\n, \\\"";
                        }
                        getMethod = JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "()";
                        sentence += "\t\tdocument.put(\"" + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\"," + getMethod + ");\n";
                        count++;
                        break;
                    case COLUMN:
                        if (count > 0) {
                            coma = "\\n, \\\"";
                        }
                        getMethod = JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ".get" + JmoordbCoreUtil.letterToUpper(entityField.getNameOfMethod()) + "()";
                        sentence += "\t\tdocument.put(\"" + JmoordbCoreUtil.letterToLower(entityField.getNameOfMethod()) + "\"," + getMethod + ");\n";

                        count++;
                        break;

                }

            }

            sentence += "\treturn document;\n";
            String code
                    = ProcessorUtil.editorFoldToDocument(entityData) + "\n\n"
                    + "    public Document toDocument(" + entityData.getEntityName() + " " + JmoordbCoreUtil.letterToLower(entityData.getEntityName()) + ") {\n"
                    + sentence + "\n"
                    + "     }\n"
                    + "// </editor-fold>\n";

            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

    // </editor-fold>
}
