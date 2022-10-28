package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.Entity;
import com.jmoordb.core.processor.model.EntityData;
import com.jmoordb.core.processor.internal.MethodProcessorAux;
import com.jmoordb.core.processor.methods.EntityField;
import com.jmoordb.core.util.MessagesUtil;
import java.util.*;
import javax.lang.model.element.Element;

/**
 * This class only works if we add elements in proper sequence.
 */
public class SupplierSourceBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";

    private StringBuilder builder = new StringBuilder();
    private String className;
    private Map<String, String> fields = new LinkedHashMap<>();

    SupplierSourceBuilderUtil sourceEntityUtilBuilder = new SupplierSourceBuilderUtil();

    public SupplierSourceBuilder() {

    }

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder init(Entity entity, EntityData entityData, List<EntityField> entityFieldList, String database, String collection,Element element)">
    public SupplierSourceBuilder init(Entity entity, EntityData entityData, List<EntityField> entityFieldList, String database, String collection,Element element) {
        builder.append(sourceEntityUtilBuilder.definePackage(entityData.getPackageOfEntity()));
        builder.append(sourceEntityUtilBuilder.generateImport(entity, entityData,element));
        builder.append(sourceEntityUtilBuilder.addRequestScoped());
        builder.append(sourceEntityUtilBuilder.defineClass(entityData.getEntityName() + "Supplier", " implements Serializable"));
//
        Boolean haveEmbedded = SupplierSourceBuilderUtil.haveEmbedded(entityFieldList);
        Boolean haveReferenced = SupplierSourceBuilderUtil.haveReferenced(entityFieldList);
        if(haveReferenced || haveEmbedded){
            builder.append(sourceEntityUtilBuilder.inject(entity, entityData, database, collection, entityFieldList,element,haveReferenced,haveEmbedded));
        }
          

        /**
         * Generar los metodos encontrados
         */
        if (entityFieldList.isEmpty()) {
         //   MessagesUtil.warning("No hay información de los métodos");
        } else {
           
             
         
                 builder.append(SupplierBuilder.get(entityData, entityFieldList,element));
                 //builder.append(SupplierBuilder.toJson(entityData, entityFieldList,element));
                 builder.append(SupplierBuilder.toDocument(entityData, entityFieldList,element));


            }
        
        //    builder.append(FindByPkOfEntityBuilder.findByPKOfEntity(repositoryData));
        return this;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addEditorFoldStart(String desc) ">
    /**
     *
     * @param desc. -Utiloce \" si necesita incluir " en el texto
     * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
     */
    public SupplierSourceBuilder addEditorFoldStartx(String desc) {
        builder.append("// <editor-fold defaultstate=\"collapsed\" desc=\"")
                .append(desc)
                .append("\">")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addFields(LinkedHashMap<String, String> identifierToTypeMap)">
    /**
     *
     * @param identifierToTypeMap
     * @return
     */
    public SupplierSourceBuilder addFields(LinkedHashMap<String, String> identifierToTypeMap) {
        for (Map.Entry<String, String> entry : identifierToTypeMap.entrySet()) {
            addField(entry.getValue(), entry.getKey());
        }
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addField(String type, String identifier)">
    public SupplierSourceBuilder addField(String type, String identifier) {
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

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addConstructor(String accessModifier, List<String> fieldsToBind) ">
    /**
     *
     * @param accessModifier
     * @param fieldsToBind
     * @return
     */
    public SupplierSourceBuilder addConstructor(String accessModifier, List<String> fieldsToBind) {
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

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addConstructor(String accessModifier, boolean bindFields)">
    public SupplierSourceBuilder addConstructor(String accessModifier, boolean bindFields) {
        addConstructor(accessModifier,
                bindFields ? new ArrayList(fields.keySet())
                        : new ArrayList<>());
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addMethod(MethodProcessorAux method)">

    public SupplierSourceBuilder addMethod(MethodProcessorAux method) {
        builder.append(LINE_BREAK)
                .append(method.end())
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder createSetterForField(String name)">
    public SupplierSourceBuilder createSetterForField(String name) {
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
// <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder createGetterForField(String name)">

    public SupplierSourceBuilder createGetterForField(String name) {
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
    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addEditorFoldStart(String desc) ">
    /**
     *
     * @param desc. -Utiloce \" si necesita incluir " en el texto
     * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
     */
    public SupplierSourceBuilder addEditorFoldStart(String desc) {
        builder.append("// <editor-fold defaultstate=\"collapsed\" desc=\"")
                .append(desc)
                .append("\">")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addNestedClass(SupplierSourceBuilder jClass)">
    public SupplierSourceBuilder addNestedClass(SupplierSourceBuilder jClass) {
        builder.append(LINE_BREAK);
        builder.append(jClass.end());
        builder.append(LINE_BREAK);
        return this;
    }
// </editor-fold>
}
