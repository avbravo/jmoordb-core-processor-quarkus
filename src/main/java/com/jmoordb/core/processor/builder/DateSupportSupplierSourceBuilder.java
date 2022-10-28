package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.DateSupport;
import com.jmoordb.core.processor.internal.MethodProcessorAux;
import com.jmoordb.core.processor.methods.DateSupportField;
import com.jmoordb.core.processor.model.DateSupportData;
import java.util.*;
import javax.lang.model.element.Element;

/**
 * This class only works if we add elements in proper sequence.
 */
public class DateSupportSupplierSourceBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";

    private StringBuilder builder = new StringBuilder();
    private StringBuilder builderProvider = new StringBuilder();
    private String className;
    private Map<String, String> fields = new LinkedHashMap<>();

    DateSupportSupplierSourceBuilderUtil sourceDateSupportUtilBuilder = new DateSupportSupplierSourceBuilderUtil();

    public DateSupportSupplierSourceBuilder() {

    }

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder init(DateSupport dateSupport, DateSupportData dateSupportData, List<DateSupportField> dateSupportFieldList, String database, String collection,Element element)">
    public DateSupportSupplierSourceBuilder init(DateSupport dateSupport, DateSupportData dateSupportData, List<DateSupportField> dateSupportFieldList, Element element) {
        builder.append(sourceDateSupportUtilBuilder.definePackage(dateSupportData.getPackageOfDateSupport()));
        builder.append(sourceDateSupportUtilBuilder.generateImport(dateSupport, dateSupportData, element));

//        builder.append(sourceDateSupportUtilBuilder.defineClass(dateSupportData.getDateSupportName() + "Supplier", " implements Serializable"));
        builder.append(sourceDateSupportUtilBuilder.defineClass("DateParameterConverter", " implements ParamConverter<Date>"));
        builder.append(sourceDateSupportUtilBuilder.defineParameterConverter(dateSupport, dateSupportData, dateSupportFieldList, element));
//Provider

        builderProvider.append(sourceDateSupportUtilBuilder.definePackage(dateSupportData.getPackageOfDateSupport()));
        builderProvider.append(sourceDateSupportUtilBuilder.generateImportProvider(dateSupport, dateSupportData, element));
        builderProvider.append(sourceDateSupportUtilBuilder.addProviderScoped());
        builderProvider.append(sourceDateSupportUtilBuilder.defineClass("DateParameterConverterProvider", " implements ParamConverterProvider"));

        ///
        builderProvider.append(sourceDateSupportUtilBuilder.defineParameterConverterProvider(dateSupport, dateSupportData, dateSupportFieldList, element));

        //    builder.append(FindByPkOfDateSupportBuilder.findByPKOfDateSupport(repositoryData));
        return this;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addEditorFoldStart(String desc) ">
    /**
     *
     * @param desc. -Utiloce \" si necesita incluir " en el texto
     * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
     */
    public DateSupportSupplierSourceBuilder addEditorFoldStartx(String desc) {
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
    public DateSupportSupplierSourceBuilder addFields(LinkedHashMap<String, String> identifierToTypeMap) {
        for (Map.Entry<String, String> entry : identifierToTypeMap.entrySet()) {
            addField(entry.getValue(), entry.getKey());
        }
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addField(String type, String identifier)">
    public DateSupportSupplierSourceBuilder addField(String type, String identifier) {
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
    public DateSupportSupplierSourceBuilder addConstructor(String accessModifier, List<String> fieldsToBind) {
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
    public DateSupportSupplierSourceBuilder addConstructor(String accessModifier, boolean bindFields) {
        addConstructor(accessModifier,
                bindFields ? new ArrayList(fields.keySet())
                        : new ArrayList<>());
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addMethod(MethodProcessorAux method)">

    public DateSupportSupplierSourceBuilder addMethod(MethodProcessorAux method) {
        builder.append(LINE_BREAK)
                .append(method.end())
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder createSetterForField(String name)">
    public DateSupportSupplierSourceBuilder createSetterForField(String name) {
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

    public DateSupportSupplierSourceBuilder createGetterForField(String name) {
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
// <editor-fold defaultstate="collapsed" desc="endProvider()">
    /**
     *
     * @return
     */
    public String endProvider() {
        builderProvider.append(LINE_BREAK + "}");
        return builderProvider.toString();

    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addEditorFoldStart(String desc) ">
    /**
     *
     * @param desc. -Utiloce \" si necesita incluir " en el texto
     * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
     */
    public DateSupportSupplierSourceBuilder addEditorFoldStart(String desc) {
        builder.append("// <editor-fold defaultstate=\"collapsed\" desc=\"")
                .append(desc)
                .append("\">")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SupplierSourceBuilder addNestedClass(SupplierSourceBuilder jClass)">
    public DateSupportSupplierSourceBuilder addNestedClass(SupplierSourceBuilder jClass) {
        builder.append(LINE_BREAK);
        builder.append(jClass.end());
        builder.append(LINE_BREAK);
        return this;
    }
// </editor-fold>
}
