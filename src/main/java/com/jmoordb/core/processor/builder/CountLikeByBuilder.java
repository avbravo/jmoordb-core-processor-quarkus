package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.enumerations.CaseSensitive;
import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;

public class CountLikeByBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

    // <editor-fold defaultstate="collapsed" desc="StringBuilder likeBy(RepositoryData repositoryData, RepositoryMethod repositoryMethod)">
    /**
     * @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not.
     * fecha .gt. @fecha .or. activo .ne. @activo .and. km .gt. km")
     * @param repositoryData
     * @param repositoryMethod
     * @return
     */
    public static StringBuilder countLikeBy(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
        StringBuilder builder = new StringBuilder();
        try {

            String returnValue = "return list;\n";
            String atribute = "";


//            
            String sentence = "";
            String paginationSource = "";

         
            String field = JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0));
            String parametro = repositoryMethod.getParamTypeElement().get(0).getName();
            Integer order = 1;
           
            /**
             * Genera el filtro
             */
            String filter = "";
            if (repositoryMethod.getCaseSensitive().equals(CaseSensitive.NO)) {
                filter = "\t\tcontador = collection.countDocuments(  new Document(\"" + field + "\", new Document(\"$regex\", \"^\"+" + parametro + ")));\n";
            } else {
                filter = "\t\tcontador = collection.countDocuments(  new Document(\"" + field + "\", new Document(\"$regex\", \"^\"+" + parametro + ").append(\"$options\", \"i\")));\n";
            }

            sentence += filter + "\n";
           

            String param = ProcessorUtil.parametersOfMethod(repositoryMethod);
            String code
                    = ProcessorUtil.editorFold(repositoryMethod, param) + "\n\n"
                    + "    @Override\n"
                    + "    public " + repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "(" + param + ") {\n"
                    + "        Long contador = 0L;\n"
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + "               " + sentence + "\n"
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
