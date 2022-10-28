package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.enumerations.TypeReferenced;
import com.jmoordb.core.processor.methods.DocumentEmbeddableField;
import com.jmoordb.core.processor.model.DocumentEmbeddableData;
import com.jmoordb.core.processor.model.IdData;
import com.jmoordb.core.util.JmoordbCoreFileUtil;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.util.List;
import javax.lang.model.element.Element;

public class DocumentEmbeddableSupplierBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

    // <editor-fold defaultstate="collapsed" desc="StringBuilder get(DocumentEmbeddableData documentEmbeddableData, List<DocumentEmbeddableField> documentEmbeddableFieldList, Element element)">
    public static StringBuilder get(DocumentEmbeddableData documentEmbeddableData, List<DocumentEmbeddableField> documentEmbeddableFieldList, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            Boolean haveEmbedded = DocumentEmbeddableSupplierSourceBuilderUtil.haveEmbedded(documentEmbeddableFieldList);
            Boolean haveReferenced = DocumentEmbeddableSupplierSourceBuilderUtil.haveReferenced(documentEmbeddableFieldList);

            String sentence = "\t";
            String cast = "";
            for (DocumentEmbeddableField documentEmbeddableField : documentEmbeddableFieldList) {
                switch (documentEmbeddableField.getAnnotationType()) {
                    case EMBEDDED:
                        sentence += embeddedProcess(documentEmbeddableData, documentEmbeddableField);
                        break;
                    case REFERENCED:

                        if (documentEmbeddableField.getTypeReferenced().equals(TypeReferenced.EMBEDDED)) {

                            sentence += embeddedProcess(documentEmbeddableData, documentEmbeddableField);
                        } else {
                            sentence += referencedProcess(documentEmbeddableData, documentEmbeddableField, element);
                        }

                        break;
                    case ID:
                        cast = castConverter(documentEmbeddableField.getReturnTypeValue(), documentEmbeddableField.getNameOfMethod());
                        sentence += "\t " + JmoordbCoreUtil.letterToLower(documentEmbeddableData.getDocumentEmbeddableName()) + ".set" + JmoordbCoreUtil.letterToUpper(documentEmbeddableField.getNameOfMethod()) + "(" + cast + ");\n";
                        break;
                    case COLUMN:
                        cast = castConverter(documentEmbeddableField.getReturnTypeValue(), documentEmbeddableField.getNameOfMethod());
                        sentence += "\t" + JmoordbCoreUtil.letterToLower(documentEmbeddableData.getDocumentEmbeddableName()) + ".set" + JmoordbCoreUtil.letterToUpper(documentEmbeddableField.getNameOfMethod()) + "(" + cast + ");\n";
                        break;

                }

            }

            String code
                    = ProcessorUtil.editorFold(documentEmbeddableData) + "\n\n"
                    + "    public " + documentEmbeddableData.getDocumentEmbeddableName() + " get(Supplier<? extends " + documentEmbeddableData.getDocumentEmbeddableName() + "> s, Document document) {\n"
                    + "        " + JmoordbCoreUtil.letterToUpper(documentEmbeddableData.getDocumentEmbeddableName()) + " " + JmoordbCoreUtil.letterToLower(documentEmbeddableData.getDocumentEmbeddableName()) + "= s.get(); \n"
                    + "        try {\n"
                    
                    + sentence + "\n"
                    + "         } catch (Exception e) {\n"
                    + "              MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "         }\n"
                    + "         return " + JmoordbCoreUtil.letterToLower(documentEmbeddableData.getDocumentEmbeddableName()) + ";\n"
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
    // <editor-fold defaultstate="collapsed" desc="String embeddedProcess(DocumentEmbeddableData documentEmbeddableData, DocumentEmbeddableField documentEmbeddableField)">

    /**
     * Procesa los documentos embebidos
     *
     * @param documentEmbeddableData
     * @param documentEmbeddableField
     * @return
     */
    public static String embeddedProcess(DocumentEmbeddableData documentEmbeddableData, DocumentEmbeddableField documentEmbeddableField) {
        String result = "";
        String documentEmbeddableNameUpper = JmoordbCoreUtil.letterToUpper(documentEmbeddableData.getDocumentEmbeddableName());
        String documentEmbeddableNameLower = JmoordbCoreUtil.letterToLower(documentEmbeddableData.getDocumentEmbeddableName());
        String fieldUpper = JmoordbCoreUtil.letterToUpper(documentEmbeddableField.getNameOfMethod());
        String fieldLower = JmoordbCoreUtil.letterToLower(documentEmbeddableField.getNameOfMethod());
        try {

            if (documentEmbeddableField.getReturnTypeValue().contains("List")) {

                result += "\t// Embedded List<" + fieldLower + ">\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";
                result += "\tList<Document> " + fieldLower + "Doc = (List) document.get(\"" + fieldLower + "\");\n";
                result += "\tfor( Document doc" + fieldUpper + " : " + fieldLower + "Doc){\n";

                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, doc" + fieldUpper + ");\n";
                result += "\t\t" + fieldLower + "List.add(" + fieldLower + ");\n";
                result += "\t};\n";
                result += "\t" + documentEmbeddableNameLower + ".set" + fieldUpper + "(" + fieldLower + "List);\n";

                return result;
            }
            if (documentEmbeddableField.getReturnTypeValue().contains("Set")) {
                result += "\t// Embedded Set<" + fieldLower + ">\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";
                result += "\tList<Document> " + fieldLower + "Doc = (List) document.get(\"" + fieldLower + "\");\n";
                result += "\tfor( Document doc" + fieldUpper + " : " + fieldLower + "Doc){\n";
                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, doc" + fieldUpper + ");\n";

                result += "\t\t" + fieldLower + "List.add(" + fieldLower + ");\n";
                result += "\t};\n";
                result += "\t" + documentEmbeddableNameLower + ".set" + fieldUpper + "(new java.util.HashSet<>(" + fieldLower + "List));\n";
                return result;
            }
            if (documentEmbeddableField.getReturnTypeValue().contains("Stream")) {
                result += "\t// Embedded Stream<" + fieldLower + ">\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";
                result += "\tList<Document> " + fieldLower + "Doc = (List) document.get(\"" + fieldLower + "\");\n";
                result += "\tfor( Document doc" + fieldUpper + " : " + fieldLower + "Doc){\n";

                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, doc" + fieldUpper + ");\n";
                result += "\t\t" + fieldLower + "List.add(" + fieldLower + ");\n";
                result += "\t};\n";
                result += "\t" + documentEmbeddableNameLower + ".set" + fieldUpper + "(" + fieldLower + "List.stream());\n";
                return result;
            }
            result += "\t// Embedded of " + fieldLower + "\n";
            result += "\tDocument " + fieldLower + "Doc = (Document) document.get(\"" + documentEmbeddableField.getNameOfMethod() + "\");\n";
            result += "\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new, " + fieldLower + "Doc);\n";
            result += "\t" + documentEmbeddableNameLower + ".set" + fieldUpper + "(" + fieldLower + ");\n";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String referencedProcess(DocumentEmbeddableData documentEmbeddableData, DocumentEmbeddableField documentEmbeddableField)">

    /**
     * Procesa los documentos Referenciados
     *
     * @param documentEmbeddableData
     * @param documentEmbeddableField
     * @return
     */
    public static String referencedProcess(DocumentEmbeddableData documentEmbeddableData, DocumentEmbeddableField documentEmbeddableField, Element element) {
        String result = "";
        String documentEmbeddableNameUpper = JmoordbCoreUtil.letterToUpper(documentEmbeddableData.getDocumentEmbeddableName());
        String documentEmbeddableNameLower = JmoordbCoreUtil.letterToLower(documentEmbeddableData.getDocumentEmbeddableName());
        String fieldUpper = JmoordbCoreUtil.letterToUpper(documentEmbeddableField.getNameOfMethod());
        String fieldLower = JmoordbCoreUtil.letterToLower(documentEmbeddableField.getNameOfMethod());

        IdData idData = new IdData();
        JmoordbCoreFileUtil.readIdAnnotationOfDocumentEmbeddableFile(element, fieldUpper + ".java", idData);

        String as = documentEmbeddableField.getReferenced().from();

        String foreignField = idData.getFieldName();
        String from = documentEmbeddableField.getReferenced().from();
        String localField = documentEmbeddableField.getReferenced().localField();
        try {

          if (documentEmbeddableField.getReturnTypeValue().contains("List")) {

                result += "\t// Referenced List<" + fieldLower + ">\n";

                result += "\t List<Document> " + fieldLower + "DocumentList = (List)document.get(\"" + documentEmbeddableField.getReferenced().from() + "\");\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";

                result += "\tfor( Document " + fieldLower + "Doc :" + fieldLower + "DocumentList){\n";
                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new," + fieldLower + "Doc);\n";

                result += "\t\t Optional<" + fieldUpper + "> " + fieldLower + "Optional = " + fieldLower + "Repository.findByPk(" + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(documentEmbeddableField.getReferenced().localField()) + "());\n";
                result += "\t\tif(" + fieldLower + "Optional.isPresent()){" + "\n";
                result += "\t\t\t" + fieldLower + "List.add(" + fieldLower + "Optional.get());\n";
                result += "\t\t}\n";
                result += "\t}\n";
                result += "\t" + documentEmbeddableNameLower + ".set" + fieldUpper + "(" + fieldLower + "List);\n";
                return result;
            }
            if (documentEmbeddableField.getReturnTypeValue().contains("Set")) {
                result += "\t// Referenced Set<" + fieldLower + ">\n";

                 result += "\t List<Document> " + fieldLower + "DocumentList = (List)document.get(\"" + documentEmbeddableField.getReferenced().from() + "\");\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";

                result += "\tfor( Document " + fieldLower + "Doc :" + fieldLower + "DocumentList){\n";
                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new," + fieldLower + "Doc);\n";

                result += "\t\t Optional<" + fieldUpper + "> " + fieldLower + "Optional = " + fieldLower + "Repository.findByPk(" + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(documentEmbeddableField.getReferenced().localField()) + "());\n";

                result += "\t\tif(" + fieldLower + "Optional.isPresent()){" + "\n";
                result += "\t\t\t" + fieldLower + "List.add(" + fieldLower + "Optional.get());\n";
                result += "\t\t}\n";
                result += "\t}\n";
                result += "\t" + documentEmbeddableNameLower + ".set" + fieldUpper + "(new java.util.HashSet<>(" + fieldLower + "List));\n";

                return result;
            }
            if (documentEmbeddableField.getReturnTypeValue().contains("Stream")) {
                result += "\t// Referenced Stream<" + fieldLower + ">\n";

                 result += "\t List<Document> " + fieldLower + "DocumentList = (List)document.get(\"" + documentEmbeddableField.getReferenced().from() + "\");\n";
                result += "\tList<" + fieldUpper + "> " + fieldLower + "List = new ArrayList<>();\n";

                result += "\tfor( Document " + fieldLower + "Doc :" + fieldLower + "DocumentList){\n";
                result += "\t\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new," + fieldLower + "Doc);\n";

                result += "\t\t Optional<" + fieldUpper + "> " + fieldLower + "Optional = " + fieldLower + "Repository.findByPk(" + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(documentEmbeddableField.getReferenced().localField()) + "());\n";

                result += "\t\tif(" + fieldLower + "Optional.isPresent()){" + "\n";
                result += "\t\t\t" + fieldLower + "List.add(" + fieldLower + "Optional.get());\n";
                result += "\t\t}\n";
                result += "\t}\n";
                result += "\t" + documentEmbeddableNameLower + ".set" + fieldUpper + "(" + fieldLower + "List.stream());\n";

                return result;
            }
            result += "\t// Referenced of " + fieldLower + "\n";

            result += "\tDocument  " + fieldLower + "Doc  = (Document) document.get(\"" + documentEmbeddableField.getReferenced().from() + "\");\n";
            result += "\t" + fieldUpper + " " + fieldLower + " = " + fieldLower + "Supplier.get(" + fieldUpper + "::new," + fieldLower + "Doc);\n";
            if (idData.getFieldType().equals("String")) {
//                result += "\tString id" + fieldUpper + "= document.getString(\"" + documentEmbeddablField.getReferenced().localField() + "\");\n";

                result += "\tString id" + fieldUpper + "= " + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(documentEmbeddableField.getReferenced().localField()) + "();\n";

            } else {
                if (idData.getFieldType().equals("Long")) {
//                    result += "\tLong id" + fieldUpper + "= document.getLong(\"" + documentEmbeddablField.getReferenced().localField() + "\");\n";
                    result += "\tLong id" + fieldUpper + "= " + fieldLower + ".get" + JmoordbCoreUtil.letterToUpper(documentEmbeddableField.getReferenced().localField()) + "();\n";

                }
            }

            result += "\t Optional<" + fieldUpper + "> " + fieldLower + "Optional = " + fieldLower + "Repository.findByPk(id" + fieldUpper + ");\n";
            result += "\tif(" + fieldLower + "Optional.isPresent()){" + "\n";
            result += "\t\t" + documentEmbeddableNameLower + ".set" + fieldUpper + "(" + fieldLower + "Optional.get());\n";
            result += "\t}\n";

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
    // </editor-fold>
}
