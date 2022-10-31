package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.autosecuence.AutosecuenceRepository;
import com.jmoordb.core.annotation.enumerations.JakartaSource;
import com.jmoordb.core.processor.internal.MethodProcessorAux;
import com.jmoordb.core.util.MessagesUtil;
import java.util.*;

/**
 * This class only works if we add elements in proper sequence.
 */
public class AutosecuenceRepositorySourceBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";

    private StringBuilder builder = new StringBuilder();
    private String className;
    private Map<String, String> fields = new LinkedHashMap<>();

    public AutosecuenceRepositorySourceBuilder() {

    }

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder definePackage(String packageName)">
    /**
     *
     * @param packageName
     * @return inserta en package en la clase
     */
    public AutosecuenceRepositorySourceBuilder definePackage(String packageName) {
        if (packageName != null) {
            builder.append("package ")
                    .append(packageName)
                    .append(";")
                    .append(LINE_BREAK);
        }
        return this;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addImport(String importPackage)">
    /**
     *
     * @param importPackage
     * @return
     */
    public AutosecuenceRepositorySourceBuilder addImport(String importPackage) {
        builder.append("import ")
                .append(importPackage)
                .append(";")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addImportJavaEELegacy()">

    /**
     *
     * @param importPackage
     * @return
     */
    public AutosecuenceRepositorySourceBuilder addImportJavaEELegacy() {
        addImport("javax.enterprise.context.ApplicationScoped");
        addImport("javax.inject.Inject");
        addImport("javax.json.bind.Jsonb");
        addImport("javax.json.bind.JsonbBuilder");
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addImportJakarta()">

    /**
     *
     * @param importPackage
     * @return
     */
    public AutosecuenceRepositorySourceBuilder addImportJakarta() {
        addImport("jakarta.enterprise.context.ApplicationScoped");
        addImport("jakarta.inject.Inject");
        addImport("jakarta.json.bind.Jsonb");
        addImport("javax.json.bind.JsonbBuilder");

        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addImportMongoDBAndMicroprofileAndUtil()">

    /**
     *
     * @param importPackage
     * @return
     */
    public AutosecuenceRepositorySourceBuilder addImportMongoDBAndMicroprofileAndUtil() {
        addImport("org.eclipse.microprofile.config.Config");
        addImport("org.eclipse.microprofile.config.inject.ConfigProperty");
        addImport("com.mongodb.client.MongoDatabase;");
        addImport("static com.mongodb.client.model.Filters.eq");
        addImport("com.mongodb.client.MongoClient");
        addImport("com.mongodb.client.MongoCollection");
        addImport("com.mongodb.client.MongoCursor");
        addImport("java.util.ArrayList");
        addImport("java.util.List");
        addImport("java.util.Optional");

        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addAnnotations(String annotation)">

    /**
     *
     * @param annotation
     * @return agrega anotaciones
     */
    public AutosecuenceRepositorySourceBuilder addAnnotations(String annotation) {
        builder.append(annotation)
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addInject(String injectSentence) ">

    /**
     *
     * @param injectSentence
     * @return
     */
    public AutosecuenceRepositorySourceBuilder addInject(String injectSentence) {
        builder.append(TAB + "@Inject ")
                .append(LINE_BREAK)
                .append(TAB + injectSentence)
                .append(";")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addInjectConfigProperties(String injectSentence)">

    /**
     * Injecta config properties
     *
     * @param injectSentence
     * @return
     */
    public AutosecuenceRepositorySourceBuilder addInjectConfigProperties(String nameConfigProperty, String javatype, String javaNameVariable) {
        builder.append(TAB + "@Inject ")
                .append(LINE_BREAK)
                .append(TAB + "@ConfigProperty(name = \"" + nameConfigProperty + "\")")
                .append(LINE_BREAK)
                .append(TAB + javatype + " " + javaNameVariable)
                .append(";")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addComment(String comment) ">

    /**
     *
     * @param comment
     * @return inserta comentarios
     */
    public AutosecuenceRepositorySourceBuilder addComment(String comment) {
        builder.append("/*")
                .append(LINE_BREAK)
                .append(TAB + comment)
                .append(LINE_BREAK)
                .append("*/")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addSentence(String sentence) ">

    /**
     *
     * @param comment
     * @return inserta comentarios
     */
    public AutosecuenceRepositorySourceBuilder addSentence(String sentence) {
        builder.append("")
                .append(LINE_BREAK)
                .append(TAB + sentence)
                .append(LINE_BREAK)
                .append("")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addEditorFoldStart(String desc) ">
    /**
     *
     * @param desc. -Utiloce \" si necesita incluir " en el texto
     * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
     */
    public AutosecuenceRepositorySourceBuilder addEditorFoldStart(String desc) {
        builder.append("// <editor-fold defaultstate=\"collapsed\" desc=\"")
                .append(desc)
                .append("\">")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addEditorFoldClose() ">

    /**
     *
     * @param desc
     * @return cierra un editor fold que sirve como ayuda a NetBeans IDE
     */
    public AutosecuenceRepositorySourceBuilder addEditorFoldEnd() {
        builder.append("// </editor-fold>")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder defineClass(String startPart, String name, String extendPart)">
    /**
     *
     * @param startPart
     * @param name
     * @param extendPart
     * @return
     */
    public AutosecuenceRepositorySourceBuilder defineClass(String startPart, String name, String extendPart) {
        className = name;
        builder.append(LINE_BREAK).append(LINE_BREAK)
                .append(startPart)
                .append(" ")
                .append(name);
        if (extendPart != null) {
            builder.append(" ")
                    .append(extendPart);
        }

        builder.append(" {")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addFields(LinkedHashMap<String, String> identifierToTypeMap)">
    /**
     *
     * @param identifierToTypeMap
     * @return
     */
    public AutosecuenceRepositorySourceBuilder addFields(LinkedHashMap<String, String> identifierToTypeMap) {
        for (Map.Entry<String, String> entry : identifierToTypeMap.entrySet()) {
            addField(entry.getValue(), entry.getKey());
        }
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addField(String type, String identifier)">
    public AutosecuenceRepositorySourceBuilder addField(String type, String identifier) {
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

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addConstructor(String accessModifier, List<String> fieldsToBind) ">
    /**
     *
     * @param accessModifier
     * @param fieldsToBind
     * @return
     */
    public AutosecuenceRepositorySourceBuilder addConstructor(String accessModifier, List<String> fieldsToBind) {
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

    public AutosecuenceRepositorySourceBuilder addConstructor(String accessModifier, boolean bindFields) {
        addConstructor(accessModifier,
                bindFields ? new ArrayList(fields.keySet())
                        : new ArrayList<>());
        return this;
    }

    public AutosecuenceRepositorySourceBuilder addMethod(MethodProcessorAux method) {
        builder.append(LINE_BREAK)
                .append(method.end())
                .append(LINE_BREAK);
        return this;
    }

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder addNestedClass(RepositorySourceBuilder jClass)">
    public AutosecuenceRepositorySourceBuilder addNestedClass(AutosecuenceRepositorySourceBuilder jClass) {
        builder.append(LINE_BREAK);
        builder.append(jClass.end());
        builder.append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder createSetterForField(String name)">

    public AutosecuenceRepositorySourceBuilder createSetterForField(String name) {
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
// <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder createGetterForField(String name)">

    public AutosecuenceRepositorySourceBuilder createGetterForField(String name) {
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

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder generateImport(RepositorySourceBuilder classProccessorAux ,Repository repository)">
    /**
     *
     * @param classProccessorAux
     * @param repository
     * @return Genera los imports para el Repository
     */
    public AutosecuenceRepositorySourceBuilder generateImport(AutosecuenceRepository autosecuencerepository) {

        try {
            if (autosecuencerepository.jakartaSource() == JakartaSource.JAVAEE_LEGACY) {
                /*
            Java EE
                 */
                addImport("javax.enterprise.context.ApplicationScoped");
                addImport("javax.inject.Inject");
//                addImport("javax.json.bind.Jsonb");
//                addImport("javax.json.bind.JsonbBuilder");
            } else {
                /**
                 * Jakarta EE
                 */
                addImport("jakarta.enterprise.context.ApplicationScoped");
                addImport("jakarta.inject.Inject");
                addImport("jakarta.json.bind.Jsonb");
                addImport("jakarta.json.bind.JsonbBuilder");

            }
            /**
             * Microprofile
             */

            addImport("org.eclipse.microprofile.config.Config");
            addImport("org.eclipse.microprofile.config.inject.ConfigProperty");
            /**
             * MongoDB
             */
            addImport("com.mongodb.client.MongoDatabase;");
            addImport("static com.mongodb.client.model.Filters.eq");
            addImport("com.mongodb.client.MongoClient");
            addImport("com.mongodb.client.MongoCollection");
            addImport("com.mongodb.client.MongoCursor");
            addImport("org.bson.Document");
            addImport("com.mongodb.client.model.FindOneAndUpdateOptions");
            addImport("com.mongodb.client.model.ReturnDocument");
            addImport("com.mongodb.client.result.InsertOneResult");
            /**
             * Java
             */
            addImport("java.util.ArrayList");
            addImport("java.util.List");
            addImport("java.util.Optional");
            addImport("java.util.function.Supplier");
            /**
             * Agrega Autosequence
             */
            addImport("com.jmoordb.core.model.Autosequence");
            /**
             * MessageUtil
             */
            addImport("com.jmoordb.core.util.MessagesUtil");
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return this;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder findOneAndUpdate() ">
    /**
     *
     * @param comment
     * @return inserta comentarios
     */
    public AutosecuenceRepositorySourceBuilder findOneAndUpdate() {
        builder.append("")
                .append(LINE_BREAK)
                .append(TAB + "public Optional<Autosequence> findOneAndUpdate(String databasecollection) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + " try {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " Long  increment = new Long(1);")
                .append(TAB + TAB + TAB + TAB + "// Integer increment = 1;")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " Document doc = new Document(\"databasecollection\", databasecollection);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " Document inc = new Document(\"$inc\", new Document(\"sequence\", increment));")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " FindOneAndUpdateOptions findOneAndUpdateOptions = new FindOneAndUpdateOptions();")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " findOneAndUpdateOptions.upsert(true);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " findOneAndUpdateOptions.returnDocument(ReturnDocument.AFTER);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " MongoCollection<Document> collection = database.getCollection(mongodbCollection);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " Document iterable = collection.findOneAndUpdate(doc, inc, findOneAndUpdateOptions);")
                .append(LINE_BREAK)
                
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " Autosequence autosequence = new Autosequence(databasecollection, iterable.getLong(\"sequence\"));")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + "//Autosequence autosequence = get(Autosequence::new, iterable);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " return Optional.of(autosequence);")
                .append(LINE_BREAK)
                .append(TAB + TAB + " } catch (Exception e) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + "MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());")
                .append(LINE_BREAK)
                .append(TAB + TAB + " }")
                .append(LINE_BREAK)
                .append(TAB + TAB + "return Optional.empty();")
                .append(LINE_BREAK)
                .append(TAB + "}")
                .append("")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder get() ">
    /**
     *
     * @param comment
     * @return inserta comentarios
     */
    public AutosecuenceRepositorySourceBuilder get() {
        builder.append("")
                .append(LINE_BREAK)
                .append(TAB + "public Autosequence get(Supplier<? extends Autosequence> s, Document document) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + "Autosequence autosequence = s.get();")
                .append(LINE_BREAK)
                .append(TAB + TAB + " try {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " autosequence.setSecuence(document.getLong(\"secuence\"));")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " autosequence.setDatabasecollection(document.getString(\"databasecollection\"));")
                .append(LINE_BREAK)
                .append(TAB + TAB + " } catch (Exception e) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + "MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());")
                .append(LINE_BREAK)
                .append(TAB + TAB + " }")
                .append(LINE_BREAK)
                .append(TAB + TAB + "return autosequence;")
                .append(LINE_BREAK)
                .append(TAB + "}")
                .append("")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder save() ">

    /**
     *
     * @param comment
     * @return inserta comentarios
     */
    public AutosecuenceRepositorySourceBuilder save() {
        builder.append("")
                .append(LINE_BREAK)
                .append(TAB + "public Optional<Autosequence> save(Autosequence autosequence) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + " try {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " MongoCollection<Document> collection = database.getCollection(mongodbCollection);")
                .append(LINE_BREAK)
                
                .append(TAB + TAB + TAB + TAB + " InsertOneResult insertOneResult = collection.insertOne(Document.parse(autosequence.toJson(autosequence)));")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " if (insertOneResult.getInsertedId() != null) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + TAB + " return Optional.of(autosequence);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " }")
                .append(LINE_BREAK)
                .append(TAB + TAB + " } catch (Exception e) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + "MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());")
                .append(LINE_BREAK)
                .append(TAB + TAB + " }")
                .append(LINE_BREAK)
                .append(TAB + TAB + "return Optional.empty();")
                .append(LINE_BREAK)
                .append(TAB + "}")
                .append("")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder findById() ">

    /**
     *
     * @param comment
     * @return inserta comentarios
     */
    public AutosecuenceRepositorySourceBuilder findById() {
        builder.append("")
                .append(LINE_BREAK)
                .append(TAB + "public Optional<Autosequence> findById(String databasecollection) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + " try {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " MongoCollection<Document> collection = database.getCollection(mongodbCollection);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " Document doc = collection.find(eq(\"databasecollection\", databasecollection)).first();")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " Autosequence autosequence = get(Autosequence::new, doc);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " return Optional.of(autosequence);")
                .append(LINE_BREAK)
                .append(TAB + TAB + " } catch (Exception e) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + "MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());")
                .append(LINE_BREAK)
                .append(TAB + TAB + " }")
                .append(LINE_BREAK)
                .append(TAB + TAB + "return Optional.empty();")
                .append(LINE_BREAK)
                .append(TAB + "}")
                .append("")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AutosecuenceRepositorySourceBuilder generate() ">

    /**
     *
     * @param comment
     * @return inserta comentarios
     */
    public AutosecuenceRepositorySourceBuilder generate() {
        builder.append("")
                .append(LINE_BREAK)
                .append(TAB + "@Override")
                .append(LINE_BREAK)
                .append(TAB + "public Long generate(String database, String collection) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + " try {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " Optional<Autosequence> autosequenceOptional = findById(database + \"_\" + collection);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " if (!autosequenceOptional.isPresent()) {")
                .append(LINE_BREAK)
                .append(TAB+TAB+TAB+TAB+TAB+"Long l = new Long(0);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + TAB + " Autosequence autosequence = new Autosequence(database + \"_\" + collection, l);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + TAB + " //Autosequence autosequence = new Autosequence(database + \"_\" + collection, 0L);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + TAB + " save(autosequence);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " }")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " Optional<Autosequence> autosequenceIncrementOptional = findOneAndUpdate(database + \"_\" + collection);")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " if (!autosequenceIncrementOptional.isPresent()) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + TAB + " return -1L;")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " } else {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + TAB + " return autosequenceIncrementOptional.get().getSecuence();")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + TAB + " }")
                .append(LINE_BREAK)
                .append(TAB + TAB + " } catch (Exception e) {")
                .append(LINE_BREAK)
                .append(TAB + TAB + TAB + "MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());")
                .append(LINE_BREAK)
                .append(TAB + TAB + " }")
                .append(LINE_BREAK)
                .append(TAB + TAB + "return -1L;")
                .append(LINE_BREAK)
                .append(TAB + "}")
                .append("")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    
    
     
}
