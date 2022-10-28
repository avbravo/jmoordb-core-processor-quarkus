package com.jmoordb.core.processor.builder;

import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.util.MessagesUtil;

public class FindByPkBuilder { 



    // <editor-fold defaultstate="collapsed" desc="StringBuilder findByPK(RepositoryData repositoryData)">
    /**
     *
     * @param comment
     * @return inserta comentarios
     */
    public static StringBuilder findByPK(RepositoryData repositoryData) {
        StringBuilder builder = new StringBuilder();
        try {
            String EditorFoldStart = "public Optional<" + repositoryData.getNameOfEntity() + "> findByPk(" + repositoryData.getTypeParameter() + " id )";
            String code
                    = "// <editor-fold defaultstate=\"collapsed\" desc=\"" + EditorFoldStart + "\">\n\n"
                    + "    public Optional<" + repositoryData.getNameOfEntity() + "> findByPk(" + repositoryData.getTypeParameter() + " id ) {\n"
                    + "        try {\n"
                    + "            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "            MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + "            Document doc = collection.find(eq(\"" + repositoryData.getFieldPk() + "\", id)).first();\n"
                    + "            if(doc == null){\n"
                    + "               return Optional.empty();\n"
                    + "            }\n"
                    + "            " + repositoryData.getNameOfEntity() + " " + repositoryData.getNameOfEntityLower() + " = " + repositoryData.getNameOfEntityLower() + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, doc);\n"
                    + "            return Optional.of(" + repositoryData.getNameOfEntityLower() + ");\n"
                    + "       } catch (Exception e) {\n"
                    + "            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "       }\n"
                    + "       return Optional.empty();\n"
                    + "    }\n"
                    + "// </editor-fold>\n";
            builder.append(code);
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }
// </editor-fold>
  

   
}
