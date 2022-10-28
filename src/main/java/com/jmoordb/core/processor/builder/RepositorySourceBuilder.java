package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.repository.Repository;
import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.internal.MethodProcessorAux;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.MessagesUtil;
import java.util.*;

/**
 * This class only works if we add elements in proper sequence.
 */
public class RepositorySourceBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";

    private StringBuilder builder = new StringBuilder();
    private String className;
    private Map<String, String> fields = new LinkedHashMap<>();

    RepositorySourceBuilderUtil sourceUtilBuilder = new RepositorySourceBuilderUtil();

    public RepositorySourceBuilder() {

    }

    // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder init(Repository repository, RepositoryData repositoryData, List<RepositoryMethod> repositoryMethodList, String database, String collection)">
    public RepositorySourceBuilder init(Repository repository, RepositoryData repositoryData, List<RepositoryMethod> repositoryMethodList, String database, String collection) {
        builder.append(sourceUtilBuilder.definePackage(repositoryData.getPackageOfRepository()));
        builder.append(sourceUtilBuilder.generateImport(repository, repositoryData));
        builder.append(sourceUtilBuilder.addApplicationScoped());
        builder.append(sourceUtilBuilder.defineClass(repositoryData.getInterfaceName() + "Impl", " implements " + repositoryData.getInterfaceName()));

        builder.append(sourceUtilBuilder.inject(repository, repositoryData, database, collection));

        /**
         * Generar los metodos encontrados
         */
        if (repositoryMethodList.isEmpty()) {
         //   MessagesUtil.warning("No hay información de los métodos");
        } else {
           
            for (RepositoryMethod repositoryMethod : repositoryMethodList) {
                switch (repositoryMethod.getAnnotationType()) {
                    case SAVE:
                        builder.append(SaveBuilder.save(repositoryData, repositoryMethod));
                        break;
                    case PING:

                        builder.append(PingBuilder.ping(repositoryData, repositoryMethod));
                        break;
                    case UPDATE:
                        builder.append(UpdateBuilder.update(repositoryData, repositoryMethod));
                        break;

                    case LOOKUP:
                        builder.append(LookupBuilder.lookup(repositoryData, repositoryMethod));
                        break;
                    case COUNT:
                        builder.append(CountBuilder.count(repositoryData, repositoryMethod));
                        break;
                    case REGEXCOUNT:
                        builder.append(RegexCountBuilder.regexCount(repositoryData, repositoryMethod));
                        break;
                    case REGEX:
                        builder.append(RegexBuilder.regex(repositoryData, repositoryMethod));
                        break;
                    case DELETE:
                        builder.append(DeleteBuilder.delete(repositoryData, repositoryMethod));
                        break;
                    case QUERY:
                        builder.append(QueryBuilder.query(repositoryData, repositoryMethod));
                        break;
                    case FIND:
                        builder.append(FindBuilder.find(repositoryData, repositoryMethod));
                        break;
                    case DELETEBY:
                        builder.append(DeleteByBuilder.deleteBy(repositoryData, repositoryMethod));
                        break;
                    case COUNTBY:
                        builder.append(CountByBuilder.countBy(repositoryData, repositoryMethod));
                        break;
                    case LIKEBY:
                        builder.append(LikeByBuilder.likeBy(repositoryData, repositoryMethod));
                        break;
                    case COUNTLIKEBY:
                        builder.append(CountLikeByBuilder.countLikeBy(repositoryData, repositoryMethod));
                        break;

                }


            }
        }
        builder.append(SaveBuilder.saveOfCrud(repositoryData));
       
        builder.append(UpdateBuilder.updateOfCrud(repositoryData));
        
         builder.append(FindBuilder.findAllPaginationSortedOfCrud(repositoryData));
         builder.append(FindBuilder.findAllOfCrud(repositoryData));
         builder.append(FindBuilder.findAllPaginationOfCrud(repositoryData));
         builder.append(FindBuilder.findAllSortedOfCrud(repositoryData));
       
        builder.append(FindByPkBuilder.findByPK(repositoryData));
        builder.append(DeleteByBuilder.deleteByPk(repositoryData));
 
            
        return this;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder addEditorFoldStart(String desc) ">
    /**
     *
     * @param desc. -Utiloce \" si necesita incluir " en el texto
     * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
     */
    public RepositorySourceBuilder addEditorFoldStartx(String desc) {
        builder.append("// <editor-fold defaultstate=\"collapsed\" desc=\"")
                .append(desc)
                .append("\">")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder addFields(LinkedHashMap<String, String> identifierToTypeMap)">
    /**
     *
     * @param identifierToTypeMap
     * @return
     */
    public RepositorySourceBuilder addFields(LinkedHashMap<String, String> identifierToTypeMap) {
        for (Map.Entry<String, String> entry : identifierToTypeMap.entrySet()) {
            addField(entry.getValue(), entry.getKey());
        }
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder addField(String type, String identifier)">
    public RepositorySourceBuilder addField(String type, String identifier) {
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

    // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder addConstructor(String accessModifier, List<String> fieldsToBind) ">
    /**
     *
     * @param accessModifier
     * @param fieldsToBind
     * @return
     */
    public RepositorySourceBuilder addConstructor(String accessModifier, List<String> fieldsToBind) {
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

    // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder addConstructor(String accessModifier, boolean bindFields)">
    public RepositorySourceBuilder addConstructor(String accessModifier, boolean bindFields) {
        addConstructor(accessModifier,
                bindFields ? new ArrayList(fields.keySet())
                        : new ArrayList<>());
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder addMethod(MethodProcessorAux method)">

    public RepositorySourceBuilder addMethod(MethodProcessorAux method) {
        builder.append(LINE_BREAK)
                .append(method.end())
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder createSetterForField(String name)">

    public RepositorySourceBuilder createSetterForField(String name) {
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
// <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder createGetterForField(String name)">

    public RepositorySourceBuilder createGetterForField(String name) {
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
   // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder addEditorFoldStart(String desc) ">
    /**
     *
     * @param desc. -Utiloce \" si necesita incluir " en el texto
     * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
     */
    public RepositorySourceBuilder addEditorFoldStart(String desc) {
        builder.append("// <editor-fold defaultstate=\"collapsed\" desc=\"")
                .append(desc)
                .append("\">")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    
        // <editor-fold defaultstate="collapsed" desc="RepositorySourceBuilder addNestedClass(RepositorySourceBuilder jClass)">

    public RepositorySourceBuilder addNestedClass(RepositorySourceBuilder jClass) {
        builder.append(LINE_BREAK);
        builder.append(jClass.end());
        builder.append(LINE_BREAK);
        return this;
    }
// </editor-fold>
}
