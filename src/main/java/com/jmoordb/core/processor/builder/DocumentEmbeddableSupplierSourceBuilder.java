package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.DocumentEmbeddable;
import com.jmoordb.core.processor.model.DocumentEmbeddableData;
import com.jmoordb.core.processor.internal.MethodProcessorAux;
import com.jmoordb.core.processor.methods.DocumentEmbeddableField;
import java.util.*;
import javax.lang.model.element.Element;

/**
 * This class only works if we add elements in proper sequence.
 */
public class DocumentEmbeddableSupplierSourceBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";

    private StringBuilder builder = new StringBuilder();
    private String className;
    private Map<String, String> fields = new LinkedHashMap<>();

    DocumentEmbeddableSupplierSourceBuilderUtil sourceDocumentEmbeddableUtilBuilder = new DocumentEmbeddableSupplierSourceBuilderUtil();

    public DocumentEmbeddableSupplierSourceBuilder() {

    }

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder init(DocumentEmbeddable documentEmbeddable, DocumentEmbeddableData documentEmbeddableData, List<DocumentEmbeddableField> documentEmbeddableFieldList, String database, String collection,Element element)">
    public DocumentEmbeddableSupplierSourceBuilder init(DocumentEmbeddable documentEmbeddable, DocumentEmbeddableData documentEmbeddableData, List<DocumentEmbeddableField> documentEmbeddableFieldList, String database, String collection,Element element) {
        builder.append(sourceDocumentEmbeddableUtilBuilder.definePackage(documentEmbeddableData.getPackageOfDocumentEmbeddable()));
        builder.append(sourceDocumentEmbeddableUtilBuilder.generateImport(documentEmbeddable, documentEmbeddableData,element));
        builder.append(sourceDocumentEmbeddableUtilBuilder.addRequestScoped());
        builder.append(sourceDocumentEmbeddableUtilBuilder.defineClass(documentEmbeddableData.getDocumentEmbeddableName() + "Supplier", " implements Serializable"));
//
        Boolean haveEmbedded = DocumentEmbeddableSupplierSourceBuilderUtil.haveEmbedded(documentEmbeddableFieldList);
        Boolean haveReferenced = DocumentEmbeddableSupplierSourceBuilderUtil.haveReferenced(documentEmbeddableFieldList);
        if(haveReferenced || haveEmbedded){
            builder.append(sourceDocumentEmbeddableUtilBuilder.inject(documentEmbeddable, documentEmbeddableData, database, collection, documentEmbeddableFieldList,element,haveReferenced,haveEmbedded));
        }
          

        /**
         * Generar los metodos encontrados
         */
        if (documentEmbeddableFieldList.isEmpty()) {
         //   MessagesUtil.warning("No hay información de los métodos");
        } else {
           
             
         
                 builder.append(DocumentEmbeddableSupplierBuilder.get(documentEmbeddableData, documentEmbeddableFieldList,element));


            }
        
        //    builder.append(FindByPkOfDocumentEmbeddableBuilder.findByPKOfDocumentEmbeddable(repositoryData));
        return this;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder addEditorFoldStart(String desc) ">
    /**
     *
     * @param desc. -Utiloce \" si necesita incluir " en el texto
     * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
     */
    public DocumentEmbeddableSupplierSourceBuilder addEditorFoldStartx(String desc) {
        builder.append("// <editor-fold defaultstate=\"collapsed\" desc=\"")
                .append(desc)
                .append("\">")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder addFields(LinkedHashMap<String, String> identifierToTypeMap)">
    /**
     *
     * @param identifierToTypeMap
     * @return
     */
    public DocumentEmbeddableSupplierSourceBuilder addFields(LinkedHashMap<String, String> identifierToTypeMap) {
        for (Map.Entry<String, String> entry : identifierToTypeMap.entrySet()) {
            addField(entry.getValue(), entry.getKey());
        }
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder addField(String type, String identifier)">
    public DocumentEmbeddableSupplierSourceBuilder addField(String type, String identifier) {
        fields.put(identifier, type);
        builder.append("private ")
                .append(type)
                .append(" ")
                .append(identifier)
                .append(";")
                .append(LINE_BREAK);

        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder addConstructor(String accessModifier, List<String> fieldsToBind) ">
    /**
     *
     * @param accessModifier
     * @param fieldsToBind
     * @return
     */
    public DocumentEmbeddableSupplierSourceBuilder addConstructor(String accessModifier, List<String> fieldsToBind) {
        builder.append(LINE_BREAK)
                .append(accessModifier)
                .append(" ")
                .append(className)
                .append("(");

        for (int i = 0; i < fieldsToBind.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            String name = fieldsToBind.get(i);
            builder.append(fields.get(name))
                    .append(" ")
                    .append(name);
        }
        builder.append(") {");
        for (int i = 0; i < fieldsToBind.size(); i++) {
            builder.append(LINE_BREAK);

            String name = fieldsToBind.get(i);
            builder.append("this.")
                    .append(name)
                    .append(" = ")
                    .append(name)
                    .append(";");
        }
        builder.append(LINE_BREAK);
        builder.append("}");
        builder.append(LINE_BREAK);

        return this;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder addConstructor(String accessModifier, boolean bindFields)">
    public DocumentEmbeddableSupplierSourceBuilder addConstructor(String accessModifier, boolean bindFields) {
        addConstructor(accessModifier,
                bindFields ? new ArrayList(fields.keySet())
                        : new ArrayList<>());
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder addMethod(MethodProcessorAux method)">

    public DocumentEmbeddableSupplierSourceBuilder addMethod(MethodProcessorAux method) {
        builder.append(LINE_BREAK)
                .append(method.end())
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder createSetterForField(String name)">
    public DocumentEmbeddableSupplierSourceBuilder createSetterForField(String name) {
        if (!fields.containsKey(name)) {
            throw new IllegalArgumentException("Field not found for setter: " + name);
        }
        addMethod(new MethodProcessorAux()
                .defineSignature("public", false, "void")
                .name("set" + Character.toUpperCase(name.charAt(0)) + name.substring(1))
                .defineBody(" this." + name + " = " + name + ";"));
        return this;
    }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder createGetterForField(String name)">

    public DocumentEmbeddableSupplierSourceBuilder createGetterForField(String name) {
        if (!fields.containsKey(name)) {
            throw new IllegalArgumentException("Field not found for Getter: " + name);
        }
        addMethod(new MethodProcessorAux()
                .defineSignature("public", false, fields.get(name))
                .name("get" + Character.toUpperCase(name.charAt(0)) + name.substring(1))
                .defineBody(" return this." + name + ";"));
        return this;
    }
    // </editor-fold> 
// <editor-fold defaultstate="collapsed" desc="end()">

    /**
     *
     * @return
     */
    public String end() {
        builder.append(LINE_BREAK + "}");
        return builder.toString();

    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder addEditorFoldStart(String desc) ">
    /**
     *
     * @param desc. -Utiloce \" si necesita incluir " en el texto
     * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
     */
    public DocumentEmbeddableSupplierSourceBuilder addEditorFoldStart(String desc) {
        builder.append("// <editor-fold defaultstate=\"collapsed\" desc=\"")
                .append(desc)
                .append("\">")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableSupplierSourceBuilder addNestedClass(SupplierSourceBuilder jClass)">
    public DocumentEmbeddableSupplierSourceBuilder addNestedClass(DocumentEmbeddableSupplierSourceBuilder jClass) {
        builder.append(LINE_BREAK);
        builder.append(jClass.end());
        builder.append(LINE_BREAK);
        return this;
    }
// </editor-fold>
}
