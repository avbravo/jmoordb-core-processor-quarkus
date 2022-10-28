package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.enumerations.CaseSensitive;
import com.jmoordb.core.annotation.enumerations.JakartaSource;
import com.jmoordb.core.annotation.enumerations.TypeOrder;
import com.jmoordb.core.annotation.enumerations.ReturnType;
import com.jmoordb.core.annotation.repository.Repository;
import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.model.Sorted;
import com.jmoordb.core.processor.methods.ParamTypeElement;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import org.bson.Document;

public class RegexCountBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;


    
    // <editor-fold defaultstate="collapsed" desc="StringBuilder regexCount(RepositoryData repositoryData)">
    public static StringBuilder regexCount(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
        StringBuilder builder = new StringBuilder();
        try {
            /**
             * Campo se obtiene del token
             */
            String field = repositoryMethod.getTokenWhere().get(0);
            /**
             * Obtengo el nombre del pàrametro que tiene el valor del token a
             * partir de la posicion 1. Es decir elimino el @
             */
            String valueParam = repositoryMethod.getTokenWhere().get(2).substring(1);

            String param = ProcessorUtil.parametersOfMethod(repositoryMethod);

            String sentence = "";
            if (repositoryMethod.getCaseSensitive().equals(CaseSensitive.NO)) {
                sentence = "contador = collection.countDocuments(new Document(\"" + field + "\", new Document(\"$regex\", \"^\"+" + valueParam + ")));";
            } else {
                sentence = "contador = collection.countDocuments(new Document(\"" + field + "\", new Document(\"$regex\", \"^\"+" + valueParam + ").append(\"$options\", \"i\")));";
            }
            String code
                    = ProcessorUtil.editorFold(repositoryMethod, param) + "\n\n"
                    + "    @Override\n"
                    + "    public " + repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "(" + param + ") {\n"
                    + "        Long contador = 0L;\n"
                    + "        try {\n"
                    + "             MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "             MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + "             " + sentence + "\n"
                    + "         } catch (Exception e) {\n"
                    + "              MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "         }\n"
                    + "         return contador;\n"
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
