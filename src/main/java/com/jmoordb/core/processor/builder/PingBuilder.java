package com.jmoordb.core.processor.builder;

import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;

public class PingBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

   

    // <editor-fold defaultstate="collapsed" desc="StringBuilder ping(RepositoryData repositoryData, RepositoryMethod repositoryMethod)">
    public static StringBuilder ping(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
        StringBuilder builder = new StringBuilder();
        try {
            String param = ProcessorUtil.parametersOfMethod(repositoryMethod);
            String EditorFoldStart = repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "()";
            String code
                    = ProcessorUtil.editorFold(repositoryMethod, param) + "\n\n"
                    + "    @Override\n"
                    + "    public " + repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "() {\n"
                    + "        Boolean conected = Boolean.FALSE;\n"
                    + "        try{\n"
                    + "            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "            Bson command = new BsonDocument(\"ping\", new BsonInt64(1));\n"
                    + "            Document commandResult = database.runCommand(command);\n"
                    + "            conected = Boolean.TRUE;\n"
                    + "        } catch (Exception e) {\n"
                    + "            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "        }\n"
                    + "        return conected;\n"
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
