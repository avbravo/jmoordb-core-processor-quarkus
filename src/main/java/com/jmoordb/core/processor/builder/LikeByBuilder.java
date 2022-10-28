package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.enumerations.CaseSensitive;
import com.jmoordb.core.annotation.enumerations.TypeOrder;
import com.jmoordb.core.annotation.enumerations.ReturnType;
import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import org.bson.Document;

public class LikeByBuilder {

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
    public static StringBuilder likeBy(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
        StringBuilder builder = new StringBuilder();
        try {

            String returnValue = "return list;\n";
            String atribute = "";
            String cursor = "               MongoCursor<Document> cursor;\n";

            String process = "               try{\n"
                    + "                  while (cursor.hasNext()) {\n"
                    + "                        list.add(" + JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity()) + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, cursor.next()));\n"
                    + "                  }\n"
                    + "               } finally {\n"
                    + "                     cursor.close();\n"
                    + "               } \n";

            if (repositoryMethod.getReturnType().equals(ReturnType.SET)) {
                returnValue = "Set<" + repositoryData.getNameOfEntity() + "> targetSet = new HashSet<>(list);\n"
                        + "       return targetSet;\n";
                atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
            } else {
                if (repositoryMethod.getReturnType().equals(ReturnType.LIST)) {
                    returnValue = "return list;\n";
                    atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
                } else {
                    if (repositoryMethod.getReturnType().equals(ReturnType.OPTIONAL)) {
                        returnValue = "return Optional.empty();\n";
                        atribute = "\n";
                        cursor = "\n";
                        process = "            " + repositoryData.getNameOfEntity() + " " + repositoryData.getNameOfEntityLower() + " = " + repositoryData.getNameOfEntityLower() + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, doc);\n"
                                + "            return Optional.of(" + repositoryData.getNameOfEntityLower() + ");\n";
                    } else {
                        if (repositoryMethod.getReturnType().equals(ReturnType.STREAM)) {
                            returnValue = "return list.stream();\n";
                            atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
                        }
                    }

                }
            }

            String sentence = "";
            String paginationSource = "";
    
            String sortSource = "";
            String field = JmoordbCoreUtil.letterToLower(repositoryMethod.getWorldAndToken().get(0));
            String parametro= repositoryMethod.getParamTypeElement().get(0).getName();
            Integer order = 1;
            if (repositoryMethod.getTypeOrder().equals(TypeOrder.DESC)) {
                order = -1;
            }


            if (repositoryMethod.getHavePagination()) {
                paginationSource = "\t\t\t.skip(" + repositoryMethod.getNameOfParametersPagination() + ".skip())\n"
                        + "\t\t\t.limit(" + repositoryMethod.getNameOfParametersPagination() + ".limit())\n";
            }

            sortSource = ".sort(sort)\n";

            /**
             * Genera el filtro
             */
            String filter = "";
            if (repositoryMethod.getCaseSensitive().equals(CaseSensitive.NO)) {
                filter = "\t\tDocument filter = new Document(\"" + field + "\", new Document(\"$regex\", \"^\"+" + parametro + "));\n";
            } else {
                filter = "\t\tDocument filter = new Document(\"" + field + "\", new Document(\"$regex\", \"^\"+" + parametro + ").append(\"$options\", \"i\"));\n";
            }

            sentence += filter + "\n";
            if (!repositoryMethod.getReturnType().equals(ReturnType.OPTIONAL)) {
                sentence += "\t\tcursor = collection.find( filter )\n"
                        + "\t\t" + paginationSource
                        + "\t\t" + sortSource
                        + "\t.iterator();\n";
            } else {
                sentence += "\t\tDocument doc = collection.find( filter )\n"
                        + "\t\t" + paginationSource
                        + "\t\t" + sortSource
                        + "\t.first();\n";
            }

            String param = ProcessorUtil.parametersOfMethod(repositoryMethod);
            String code
                    = ProcessorUtil.editorFold(repositoryMethod, param) + "\n\n"
                    + "    @Override\n"
                    + "    public " + repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "(" + param + ") {\n"
                    + atribute
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + cursor
                    + "             Document sort = new Document(\"" + field + "\"," + order + ");\n"
                    + "               " + sentence + "\n"
                    + process
                    + "         } catch (Exception e) {\n"
                    + "              MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + \" \" + e.getLocalizedMessage());\n"
                    + "         }\n"
                    + "         " + returnValue + "\n"
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
