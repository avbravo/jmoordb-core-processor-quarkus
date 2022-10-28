package com.jmoordb.core.processor.builder;

import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;

public class DeleteByBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

    // <editor-fold defaultstate="collapsed" desc="StringBuilder deleteBy(RepositoryData repositoryData, RepositoryMethod repositoryMethod)">
    /**
     * @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not.
     * fecha .gt. @fecha .or. activo .ne. @activo .and. km .gt. km")
     * @param repositoryData
     * @param repositoryMethod
     * @return
     */
    public static StringBuilder deleteBy(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
        StringBuilder builder = new StringBuilder();
        try {

            String param = ProcessorUtil.parametersOfMethod(repositoryMethod);
            /**
             * Campo se obtiene del token
             */

            String sentence = "";

            /**
             * Es un solo parámetro
             */
            /*
                        Se genera el Filter usado para el Delete
             */
            String filter = ProcessorUtil.generateFilterForFindAndCountAndDelete(repositoryMethod);

            sentence += filter + "\n";

            sentence += "\t\tcom.mongodb.client.result.DeleteResult deleteResult = collection.deleteOne(filter);\n";

            /**
             * Más de un parámetro
             */
            String code
                    = ProcessorUtil.editorFold(repositoryMethod, param) + "\n\n"
                    + "    @Override\n"
                    + "    public " + repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "(" + param + ") {\n"
                    + "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n"
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + "               MongoCursor<Document> cursor;\n"
                    + "               " + sentence + "\n"
                    + "               return deleteResult.getDeletedCount();\n"
                    + "         } catch (Exception e) {\n"
                    + "              MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "         }\n"
                    + "         return 0L;\n"
                    + "     }\n"
                    + "// </editor-fold>\n";

            builder.append(code);
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="StringBuilder deleteByPK(RepositoryData repositoryData)">
    /**
     * @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not.
     * fecha .gt. @fecha .or. activo .ne. @activo .and. km .gt. km")
     * @param repositoryData
     * @param repositoryMethod
     * @return
     */
    public static StringBuilder deleteByPk(RepositoryData repositoryData) {
        StringBuilder builder = new StringBuilder();
        try {

            String sentence = "";

            String filter = "Bson filter = Filters.eq(\"" + repositoryData.getFieldPk() + "\",id);\n";

            sentence += filter + "\n";

            sentence += "\t\tcom.mongodb.client.result.DeleteResult deleteResult = collection.deleteOne(filter);\n";

            /**
             * Más de un parámetro
             */
            String code
                    = ProcessorUtil.editorFold("Long deleteByPK(" + repositoryData.getTypeParameter() + " id )") + "\n\n"
                    + "    @Override\n"
                    + "    public Long deleteByPk(" + repositoryData.getTypeParameter() + " id)" + "{\n"
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + "               MongoCursor<Document> cursor;\n"
                    + "               " + sentence + "\n"
                    + "               return deleteResult.getDeletedCount();\n"
                    + "         } catch (Exception e) {\n"
                    + "              MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "         }\n"
                    + "         return 0L;\n"
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
