package com.jmoordb.core.processor.builder;

import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;

public class DeleteBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;



    // <editor-fold defaultstate="collapsed" desc="StringBuilder delete(RepositoryData repositoryData)">
    /**
     * @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not.
     * fecha .gt. @fecha .or. activo .ne. @activo .and. km .gt. km")
     * @param repositoryData
     * @param repositoryMethod
     * @return
     */
    public static StringBuilder delete(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
        StringBuilder builder = new StringBuilder();
        try {

            String param = ProcessorUtil.parametersOfMethod(repositoryMethod);
            /**
             * Campo se obtiene del token
             */

            String sentence = "";
            if (repositoryMethod.getWhere().equals("") && ProcessorUtil.parametersHaveSearchType(repositoryMethod)) {
                String field = repositoryMethod.getParamTypeElement().get(0).getName();

                sentence = "com.mongodb.client.result.DeleteResult deleteResult = collection.deleteOne(" + field + ".getFilter());\n";
            } else {
                String field = repositoryMethod.getTokenWhere().get(0);
                /**
                 * Obtengo el nombre del pàrametro que tiene el valor del token
                 * a partir de la posicion 1. Es decir elimino el @
                 */

                String valueParam = repositoryMethod.getTokenWhere().get(2).substring(1);

                /**
                 * Es un solo parámetro
                 */
                if (!repositoryMethod.getWhere().equals("") && repositoryMethod.getTokenWhere().size() == 3) {

                    String operator = repositoryMethod.getTokenWhere().get(1).replace(".", "").trim();

                    sentence = "Bson filter = Filters." + operator + "(\"" + field + "\"," + valueParam + ");\n";
                    sentence += "\t\tcom.mongodb.client.result.DeleteResult deleteResult = collection.deleteOne(filter);\n";

                } else {

                    if (!repositoryMethod.getWhere().equals("") && repositoryMethod.getTokenWhere().size() > 3) {
                        /*
                        Se genera el Filter usado para el Delete
                         */
                        sentence = ProcessorUtil.generateFilterForQueryDelete(repositoryMethod);
                        sentence += "\t\tcom.mongodb.client.result.DeleteResult deleteResult = collection.deleteOne(filter);\n";
                    }
                    /**
                     * Más de un parámetro
                     */

                }
            }
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
   

}
