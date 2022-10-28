package com.jmoordb.core.processor.builder;

import com.jmoordb.core.processor.methods.DateSupportField;
import com.jmoordb.core.processor.model.DateSupportData;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;

public class DateSupplierBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

    
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
    // <editor-fold defaultstate="collapsed" desc="String embeddedProcess(DateSupportData dateSupportData, DateSupportField dateSupportField)">

    /**
     * Procesa los documentos embebidos
     *
     * @param dateSupportData
     * @param dateSupportField
     * @return
     */
    public static String embeddedProcess(DateSupportData dateSupportData, DateSupportField dateSupportField) {
        String result = "";
        String dateSupportNameUpper = JmoordbCoreUtil.letterToUpper(dateSupportData.getDateSupportName());
        String dateSupportNameLower = JmoordbCoreUtil.letterToLower(dateSupportData.getDateSupportName());
        String fieldUpper = JmoordbCoreUtil.letterToUpper(dateSupportField.getNameOfMethod());
        String fieldLower = JmoordbCoreUtil.letterToLower(dateSupportField.getNameOfMethod());
        try {

            if (dateSupportField.getReturnTypeValue().contains("List")) {

                result += "\t// Embedded List<" + fieldLower + ">\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";
                result += "\tList<Document> " + fieldLower + "Doc = (List) document.get(\"" + fieldLower + "\");\n";
                result += "\tfor( Document doc" + fieldUpper + " : " + fieldLower + "Doc){\n";

                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, doc" + fieldUpper + ");\n";
                result += "\t\t" + fieldLower + "List.add(" + fieldLower + ");\n";
                result += "\t};\n";
                result += "\t" + dateSupportNameLower + ".set" + fieldUpper + "(" + fieldLower + "List);\n";

                return result;
            }
            if (dateSupportField.getReturnTypeValue().contains("Set")) {
                result += "\t// Embedded Set<" + fieldLower + ">\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";
                result += "\tList<Document> " + fieldLower + "Doc = (List) document.get(\"" + fieldLower + "\");\n";
                result += "\tfor( Document doc" + fieldUpper + " : " + fieldLower + "Doc){\n";
                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, doc" + fieldUpper + ");\n";

                result += "\t\t" + fieldLower + "List.add(" + fieldLower + ");\n";
                result += "\t};\n";
                result += "\t" + dateSupportNameLower + ".set" + fieldUpper + "(new java.util.HashSet<>(" + fieldLower + "List));\n";
                return result;
            }
            if (dateSupportField.getReturnTypeValue().contains("Stream")) {
                result += "\t// Embedded Stream<" + fieldLower + ">\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";
                result += "\tList<Document> " + fieldLower + "Doc = (List) document.get(\"" + fieldLower + "\");\n";
                result += "\tfor( Document doc" + fieldUpper + " : " + fieldLower + "Doc){\n";

                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, doc" + fieldUpper + ");\n";
                result += "\t\t" + fieldLower + "List.add(" + fieldLower + ");\n";
                result += "\t};\n";
                result += "\t" + dateSupportNameLower + ".set" + fieldUpper + "(" + fieldLower + "List.stream());\n";
                return result;
            }
            result += "\t// Embedded of " + fieldLower + "\n";
            result += "\tDocument " + fieldLower + "Doc = (Document) document.get(\"" + dateSupportField.getNameOfMethod() + "\");\n";
            result += "\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, " + fieldLower + "Doc);\n";
            result += "\t" + dateSupportNameLower + ".set" + fieldUpper + "(" + fieldLower + ");\n";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
    // </editor-fold>
   

    
   
}
