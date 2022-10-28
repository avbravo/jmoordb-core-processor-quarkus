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

public class RegexBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;


    

    // <editor-fold defaultstate="collapsed" desc="StringBuilder regex(RepositoryData repositoryData)">
    public static StringBuilder regex(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
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

            String returnValue = "return list;\n";
            if (repositoryMethod.getReturnType().equals(ReturnType.SET)) {
                returnValue = "Set<" + repositoryData.getNameOfEntity() + "> targetSet = new HashSet<>(list);\n"
                        + "return targetSet;\n";
            } else {
                if (repositoryMethod.getReturnType().equals(ReturnType.STREAM)) {
                    returnValue = "return list.stream();\n";

                }
            }
            /**
             * Genera el paginator
             */
            String paginationSource = "";
            if (repositoryMethod.getHavePagination()) {

                paginationSource = "\t\t\t.skip(" + repositoryMethod.getNameOfParametersPagination() + ".skip())\n"
                        + "\t\t\t.limit(" + repositoryMethod.getNameOfParametersPagination() + ".limit())\n";

            }
            /*
            Genera la ordenación.
            No se valida que sea un parametro del metodo.
             */

            String sortSource = "";

            Integer order = 1;
            if (repositoryMethod.getTypeOrder().equals(TypeOrder.DESC)) {
                order = -1;
            }


            sortSource = ".sort(sort)\n";

            String sentence = "";
            if (repositoryMethod.getCaseSensitive().equals(CaseSensitive.NO)) {
                sentence = "cursor = collection.find(new Document(\"" + field + "\", new Document(\"$regex\", \"^\"+" + valueParam + ")))\n"
                        + paginationSource
                        + sortSource
                        + "     .iterator();\n";
            } else {

                sentence = "cursor = collection.find(new Document(" + field + ", new Document(\"$regex\", \"^\" +" + valueParam + ").append(\"$options\", \"i\")))\n"
                        + paginationSource
                        + sortSource
                        + "     .iterator();\n";
            }

            String code
                    = ProcessorUtil.editorFold(repositoryMethod, param) + "\n\n"
                    + "    @Override\n"
                    + "    public " + repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "(" + param + ") {\n"
                    + "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n"
                    + "        try {\n"
                    + "             MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "             MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + "             MongoCursor<Document> cursor;\n"
                    + "             Document sort = new Document(\"" + field + "\"," + order + ");\n"
                    + "             " + sentence + "\n"
                    + "               try{\n"
                    + "                  while (cursor.hasNext()) {\n"
                    + "                        list.add(" + JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity()) + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, cursor.next()));\n"
                    + "                  }\n"
                    + "               } finally {\n"
                    + "                     cursor.close();\n"
                    + "               } \n"
                    + "         } catch (Exception e) {\n"
                    + "              MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "         }\n"
                    + "              " + returnValue + "\n"
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
