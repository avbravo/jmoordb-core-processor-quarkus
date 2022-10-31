package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.enumerations.ReturnType;
import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import org.bson.Document;

public class FindBuilder {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

    // <editor-fold defaultstate="collapsed" desc="StringBuilder find(RepositoryData repositoryData, RepositoryMethod repositoryMethod)">
    /**
     * @Delete(where = "idoceano .eq. @idoceano .and. oceano .ne. @oceano .not.
     * fecha .gt. @fecha .or. activo .ne. @activo .and. km .gt. km")
     * @param repositoryData
     * @param repositoryMethod
     * @return
     */
    public static StringBuilder find(RepositoryData repositoryData, RepositoryMethod repositoryMethod) {
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
            switch (repositoryMethod.getReturnType()) {
                case SET:
                    returnValue = "Set<" + repositoryData.getNameOfEntity() + "> targetSet = new HashSet<>(list);\n"
                            + "       return targetSet;\n";
                    atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
                    break;
                case LIST:
                    returnValue = "return list;\n";
                    atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
                    break;

                case OPTIONAL:
                    returnValue = "return Optional.empty();\n";
                    atribute = "\n";
                    cursor = "\n";
                    process = "            " + repositoryData.getNameOfEntity() + " " + repositoryData.getNameOfEntityLower() + "_ = " + repositoryData.getNameOfEntityLower() + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, doc);\n"
                            + "            return Optional.of(" + repositoryData.getNameOfEntityLower() + "_);\n";
                    break;
                case STREAM:
                    returnValue = "return list.stream();\n";
                    atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
                    break;
            }

            String sentence = "";
            String paginationSource = "";
            Document sort = new Document();
            String sortSource = "";

            if (repositoryMethod.getHavePagination()) {
                paginationSource = "\t\t\t.skip(" + repositoryMethod.getNameOfParametersPagination() + ".skip())\n"
                        + "\t\t\t.limit(" + repositoryMethod.getNameOfParametersPagination() + ".limit())\n";
            }

            if (repositoryMethod.getHaveSorted()) {

                sortSource = "\t\t\t.sort(sorted.getSort())\n";
            }
            if (repositoryMethod.getNameOfMethod().startsWith("findAll")) {
                sentence += "\t\tcursor = collection.find()\n"
                        + "\t\t" + paginationSource
                        + "" + sortSource
                        + "\t\t.iterator();\n";
            } else {
                /**
                 * Genera el filtro
                 */
                String filter = ProcessorUtil.generateFilterForFindAndCountAndDelete(repositoryMethod);

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
    // <editor-fold defaultstate="collapsed" desc="StringBuilder findAllPaginationSortedOfCrud(RepositoryData repositoryData)">
    /**
     *
     * @param repositoryData
     * @return
     */
    public static StringBuilder findAllPaginationSortedOfCrud(RepositoryData repositoryData) {
        StringBuilder builder = new StringBuilder();
        try {
            String nameOfEntityUpper = JmoordbCoreUtil.letterToUpper(repositoryData.getNameOfEntity());
            String nameOfEntityLower = JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity());

            String returnValue = "return list;\n";
            String atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
            String cursor = "               MongoCursor<Document> cursor;\n";

            String process = "               try{\n"
                    + "                  while (cursor.hasNext()) {\n"
                    + "                        list.add(" + JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity()) + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, cursor.next()));\n"
                    + "                  }\n"
                    + "               } finally {\n"
                    + "                     cursor.close();\n"
                    + "               } \n";

            String sentence = "";
            String paginationSource = "";
            Document sort = new Document();
            String sortSource = "";

           
                paginationSource = "\t\t\t.skip(pagination.skip())\n"
                        + "\t\t\t.limit(pagination.limit())\n";
            

      

                sortSource = "\t\t\t.sort(sorted.getSort())\n";
      
           
                sentence += "\t\tcursor = collection.find()\n"
                        + "\t\t" + paginationSource
                        + "" + sortSource
                        + "\t\t.iterator();\n";
           

     
            String code
                    = ProcessorUtil.editorFold("public List<"+nameOfEntityUpper+"> findAllPaginationSorted(Pagination pagination, Sorted sorted)") + "\n\n"
                    + "    @Override\n"
                    + "    public List<" + nameOfEntityUpper + "> findAllPaginationSorted(Pagination pagination, Sorted sorted) {\n"
                    + atribute
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + cursor
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
    // <editor-fold defaultstate="collapsed" desc="StringBuilder findAllPaginationOfCrud(RepositoryData repositoryData)">
    /**
     *
     * @param repositoryData
     * @return
     */
    public static StringBuilder findAllPaginationOfCrud(RepositoryData repositoryData) {
        StringBuilder builder = new StringBuilder();
        try {
            String nameOfEntityUpper = JmoordbCoreUtil.letterToUpper(repositoryData.getNameOfEntity());
            String nameOfEntityLower = JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity());

            String returnValue = "return list;\n";
            String atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
            String cursor = "               MongoCursor<Document> cursor;\n";

            String process = "               try{\n"
                    + "                  while (cursor.hasNext()) {\n"
                    + "                        list.add(" + JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity()) + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, cursor.next()));\n"
                    + "                  }\n"
                    + "               } finally {\n"
                    + "                     cursor.close();\n"
                    + "               } \n";

            String sentence = "";
            String paginationSource = "";
            Document sort = new Document();
            String sortSource = "";

           
                paginationSource = "\t\t\t.skip(pagination.skip())\n"
                        + "\t\t\t.limit(pagination.limit())\n";
            

      

//                sortSource = "\t\t\t.sort(sorted.getSort())\n";
      
           
                sentence += "\t\tcursor = collection.find()\n"
                        + "\t\t" + paginationSource
                        + "" + sortSource
                        + "\t\t.iterator();\n";
           

     
            String code
                    = ProcessorUtil.editorFold("public List<"+nameOfEntityUpper+"> findAllPagination(Pagination pagination)") + "\n\n"
                    + "    @Override\n"
                    + "    public List<" + nameOfEntityUpper + "> findAllPagination(Pagination pagination) {\n"
                    + atribute
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + cursor
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
    // <editor-fold defaultstate="collapsed" desc="StringBuilder findAllSortedOfCrud(RepositoryData repositoryData)">
    /**
     *
     * @param repositoryData
     * @return
     */
    public static StringBuilder findAllSortedOfCrud(RepositoryData repositoryData) {
        StringBuilder builder = new StringBuilder();
        try {
            String nameOfEntityUpper = JmoordbCoreUtil.letterToUpper(repositoryData.getNameOfEntity());
            String nameOfEntityLower = JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity());

            String returnValue = "return list;\n";
            String atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
            String cursor = "               MongoCursor<Document> cursor;\n";

            String process = "               try{\n"
                    + "                  while (cursor.hasNext()) {\n"
                    + "                        list.add(" + JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity()) + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, cursor.next()));\n"
                    + "                  }\n"
                    + "               } finally {\n"
                    + "                     cursor.close();\n"
                    + "               } \n";

            String sentence = "";
            String paginationSource = "";
            Document sort = new Document();
            String sortSource = "";

           
//                paginationSource = "\t\t\t.skip(pagination.skip())\n"
//                        + "\t\t\t.limit(pagination.limit())\n";
//            

      

                sortSource = "\t\t\t.sort(sorted.getSort())\n";
      
           
                sentence += "\t\tcursor = collection.find()\n"
                        + "\t\t" + paginationSource
                        + "" + sortSource
                        + "\t\t.iterator();\n";
           

     
            String code
                    = ProcessorUtil.editorFold("public List<"+nameOfEntityUpper+"> findAllSorted(Sorted sorted)") + "\n\n"
                    + "    @Override\n"
                    + "    public List<" + nameOfEntityUpper + ">  findAllSorted(Sorted sorted) {\n"
                    + atribute
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + cursor
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
    
    // <editor-fold defaultstate="collapsed" desc="StringBuilder findAllOfCrud(RepositoryData repositoryData)">
    /**
     *
     * @param repositoryData
     * @return
     */
    public static StringBuilder findAllOfCrud(RepositoryData repositoryData) {
        StringBuilder builder = new StringBuilder();
        try {
            String nameOfEntityUpper = JmoordbCoreUtil.letterToUpper(repositoryData.getNameOfEntity());
            String nameOfEntityLower = JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity());

            String returnValue = "return list;\n";
            String atribute = "        List<" + repositoryData.getNameOfEntity() + "> list = new ArrayList<>();\n";
            String cursor = "               MongoCursor<Document> cursor;\n";

            String process = "               try{\n"
                    + "                  while (cursor.hasNext()) {\n"
                    + "                        list.add(" + JmoordbCoreUtil.letterToLower(repositoryData.getNameOfEntity()) + "Supplier.get(" + repositoryData.getNameOfEntity() + "::new, cursor.next()));\n"
                    + "                  }\n"
                    + "               } finally {\n"
                    + "                     cursor.close();\n"
                    + "               } \n";

            String sentence = "";
            String paginationSource = "";
            Document sort = new Document();
            String sortSource = "";

           
//                paginationSource = "\t\t\t.skip(pagination.skip())\n"
//                        + "\t\t\t.limit(pagination.limit())\n";
//            

      

//                sortSource = "\t\t\t.sort(sorted.getSort())\n";
      
           
                sentence += "\t\tcursor = collection.find()\n"
                        + "\t\t" + paginationSource
                        + "" + sortSource
                        + "\t\t.iterator();\n";
           

     
            String code
                    = ProcessorUtil.editorFold("public List<"+nameOfEntityUpper+"> findAll()") + "\n\n"
                    + "    @Override\n"
                    + "    public List<" + nameOfEntityUpper + "> findAll() {\n"
                    + atribute
                    + "        try {\n"
                    + "               MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);\n"
                    + "               MongoCollection<Document> collection = database.getCollection(mongodbCollection);\n"
                    + cursor
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
