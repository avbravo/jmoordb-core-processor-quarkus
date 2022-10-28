package com.jmoordb.core.processor.builder;

import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;

public class UpdateBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;


    // <editor-fold defaultstate="collapsed" desc="StringBuilder update(RepositoryData repositoryData, RepositoryMethod repositoryMethod)">
    public static StringBuilder update(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
        StringBuilder builder = new StringBuilder();
        try {
            String param = ProcessorUtil.parametersOfMethod(repositoryMethod);

            String calculateReturn = "";
            String catchReturn = "";

            String code
                    = ProcessorUtil.editorFold(repositoryMethod, param) + "\n\n"
                    + "    @Override\n"
                    + "    public " + repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "(" + param + ") {\n"
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + "               if (!findByPk(" + repositoryData.getNameOfEntityLower() + ".get" + JmoordbCoreUtil.letterToUpper(repositoryData.getFieldPk()) + "()).isPresent()) { \n"
                    + "                   MessagesUtil.warning(\"Not found a record with that id\");\n"
                    + "                    return Boolean.FALSE;\n"
                    + "               }\n"
                    + "               Bson filter = Filters.empty();\n"
                    + "               filter = Filters.eq(\"" + repositoryData.getFieldPk() + "\"," + repositoryData.getNameOfEntityLower() + ".get" + JmoordbCoreUtil.letterToUpper(repositoryData.getFieldPk()) + "());\n"                    
                    + "               UpdateResult result = collection.updateOne(filter,"+ repositoryData.getNameOfEntityLower()+"Supplier.toDocument(" + repositoryData.getNameOfEntityLower() + "));\n"
                    + "               if (result.getModifiedCount() > 0) {\n"
                    + "                  return Boolean.TRUE;\n"
                    + "               }\n"
                    + "         } catch (Exception e) {\n"
                    + "              MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "         }\n"
                    + "         return Boolean.FALSE;\n"
                    + "     }\n"
                    + "// </editor-fold>\n";

            builder.append(code);
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="StringBuilder update(RepositoryData repositoryData)">
    public static StringBuilder updateOfCrud(RepositoryData repositoryData) {
        StringBuilder builder = new StringBuilder();
        try {
            
             String nameOfEntityUpper = JmoordbCoreUtil.letterToUpper(repositoryData.getNameOfEntity());
            String nameOfEntityLower = JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity());

            
        
            String calculateReturn = "";
            String catchReturn = "";

            String code
                    = ProcessorUtil.editorFold("Boolean update("+ nameOfEntityUpper + " "+nameOfEntityLower+")")+ "\n\n"
                    + "    @Override\n"
                    + "    public Boolean update("+ nameOfEntityUpper + " "+nameOfEntityLower + ") {\n"
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + "               if (!findByPk(" + repositoryData.getNameOfEntityLower() + ".get" + JmoordbCoreUtil.letterToUpper(repositoryData.getFieldPk()) + "()).isPresent()) { \n"
                    + "                   MessagesUtil.warning(\"Not found a record with that id\");\n"
                    + "                    return Boolean.FALSE;\n"
                    + "               }\n"
                    + "               Bson filter = Filters.empty();\n"
                    + "               filter = Filters.eq(\"" + repositoryData.getFieldPk() + "\"," + repositoryData.getNameOfEntityLower() + ".get" + JmoordbCoreUtil.letterToUpper(repositoryData.getFieldPk()) + "());\n"
                    + "               //Jsonb jsonb = JsonbBuilder.create();\n"
                    + "               //UpdateResult result = collection.updateOne(filter, Document.parse(jsonb.toJson(" + repositoryData.getNameOfEntityLower() + ")));\n"
                    + "               //UpdateResult result = collection.updateOne(filter, Document.parse("+repositoryData.getNameOfEntityLower()+"Supplier.toJson(" + repositoryData.getNameOfEntityLower() + ")));\n"
                    + "               UpdateResult result = collection.updateOne(filter,"+ repositoryData.getNameOfEntityLower()+"Supplier.toDocument(" + repositoryData.getNameOfEntityLower() + "));\n"
                    + "               if (result.getModifiedCount() > 0) {\n"
                    + "                  return Boolean.TRUE;\n"
                    + "               }\n"
                    + "         } catch (Exception e) {\n"
                    + "              MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "         }\n"
                    + "         return Boolean.FALSE;\n"
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
