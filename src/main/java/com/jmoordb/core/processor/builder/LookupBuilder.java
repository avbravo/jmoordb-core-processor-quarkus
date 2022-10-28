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

public class LookupBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;


    // <editor-fold defaultstate="collapsed" desc="StringBuilder lookup(RepositoryData repositoryData)">
    public static StringBuilder lookup(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
        StringBuilder builder = new StringBuilder();
        try {

            String returnValue = "return list;\n";
            if (repositoryMethod.getReturnType().equals(ReturnType.SET)) {

                returnValue = "Set<" + repositoryData.getNameOfEntity() + "> targetSet = new HashSet<>(list);\n"
                        + "        return targetSet;\n";
            } else {
                if (repositoryMethod.getReturnType().equals(ReturnType.STREAM)) {
                    returnValue = "return list.stream();\n";

                }
            }
            String param = ProcessorUtil.parametersOfMethod(repositoryMethod);

            String code
                    = ProcessorUtil.editorFold(repositoryMethod, param) + "\n\n"
                    + "    @Override\n"
                    + "    public " + repositoryMethod.getReturnTypeValue() + " " + repositoryMethod.getNameOfMethod() + "(" + param + ") {\n"
                    + "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n"
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + "               MongoCursor<Document> cursor;\n"
                    + "               Document sortQuery = new Document();\n"
                    + "               if (search.getSorted().getSort() == null || search.getSorted().getSort().isEmpty()) {\n"
                    + "               } else {\n"
                    + "                   sortQuery = search.getSorted().getSort();\n"
                    + "               }\n"
                    + "               if (search.getPagination() == null || search.getPagination().getPage() < 1) {\n"
                    + "                  cursor = collection.find(search.getFilter()).sort(sortQuery).iterator();\n"
                    + "               } else {\n"
                    + "                    cursor = collection.find(search.getFilter())\n"
                    + "                                        .skip(search.getPagination().skip())\n"
                    + "                                        .limit(search.getPagination().limit())\n"
                    + "                                        .sort(sortQuery).iterator();\n"
                    + "               }\n"
                    + "               try{\n"
                    + "                  while (cursor.hasNext()) {\n"
                    + "                       list.add(" + JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity()) + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, cursor.next()));\n"
                    + "                  }\n"
                    + "               } finally {\n"
                    + "                     cursor.close();\n"
                    + "               } \n"
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
