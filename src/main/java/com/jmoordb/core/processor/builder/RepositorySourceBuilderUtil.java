package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.enumerations.JakartaSource;
import com.jmoordb.core.annotation.repository.Repository;
import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.MessagesUtil;

public class RepositorySourceBuilderUtil {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;


    
    
    // <editor-fold defaultstate="collapsed" desc="String numberOfParametersOfMethod(RepositoryMethod repositoryMethod)">
    /**
     *
     * @param repositoryMethod
     * @return Los parametros del metodo como una cadena
     */
    private Integer numberOfParametersOfMethod(RepositoryMethod repositoryMethod) {
        Integer number = 0;
        try {

            number = repositoryMethod.getParamTypeElement().size();

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return number;
    }
// </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="StringBuilder definePackage(String packageName)">
    /**
     *
     * @param packageName
     * @return inserta en package en la clase
     */
    public StringBuilder definePackage(String packageName) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";
            if (packageName != null) {
                code = "package " + packageName + ";\n";
            }
            builder.append(code);
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="StringBuilder defineClass(String startPart, String name, String extendPart)">
    /**
     *
     * @param startPart
     * @param name
     * @param extendPart
     * @return
     */
    public StringBuilder defineClass(String name, String extendPart) {
        className = name;
        StringBuilder builder = new StringBuilder();
        try {
            String code = "public class " + name;
            if (extendPart != null) {
                code += " " + extendPart;
            }
            code += "{\n";
            builder.append(code);
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="StringBuilder addApplicationScoped()">
    /**
     *
     * @param annotation
     * @return agrega anotaciones
     */
    public StringBuilder addApplicationScoped() {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";
            builder.append("@ApplicationScoped\n");
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="StringBuilder generateImport(Repository repository, RepositoryData repositoryData)">
    public StringBuilder generateImport(Repository repository, RepositoryData repositoryData) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";

            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"imports\">\n\n";
            if (repository.jakartaSource() == JakartaSource.JAVAEE_LEGACY) {
                /*
            Java EE
                 */
                code += "import javax.enterprise.context.ApplicationScoped;\n"
                        + "import javax.inject.Inject;\n";

            } else {
                /**
                 * Jakarta EE
                 */
                code += "import jakarta.enterprise.context.ApplicationScoped;\n"
                        + "import jakarta.inject.Inject;\n";


            }
            /**
             * Microprofile
             */
            code += "import org.eclipse.microprofile.config.Config;\n"
                    + "import org.eclipse.microprofile.config.inject.ConfigProperty;\n"
                    + "/**\n"
                    + "* MongoDB\n"
                    + "*/\n"
                    + "import com.mongodb.client.MongoDatabase;\n"
                    + "import static com.mongodb.client.model.Filters.eq;\n"
                    + "import com.mongodb.client.MongoClient;\n"
                    + "import com.mongodb.client.MongoCollection;\n"
                    + "import com.mongodb.client.MongoCursor;\n"
                    + "import org.bson.Document;\n"
                    + "import com.mongodb.client.model.FindOneAndUpdateOptions;\n"
                    + "import com.mongodb.client.model.ReturnDocument;\n"
                    + "import com.mongodb.client.result.InsertOneResult;\n"
                    + "import org.bson.BsonInt64;\n"
                    + "import org.bson.conversions.Bson;\n"
                    + "import org.bson.BsonDocument;\n"
                    + "import com.mongodb.client.model.Filters;\n"
                    + "import com.mongodb.client.result.UpdateResult;\n"
                    + "/**\n"
                    + "* Java\n"
                    + "*/\n"
                    + "import java.util.ArrayList;\n"
                    + "import java.util.List;\n"
                    + "import java.util.Set;\n"
                    + "import java.util.Optional;\n"
                    + "import java.util.function.Supplier;\n"
                    + "import com.jmoordb.core.util.MessagesUtil;\n"
                    + "import com.jmoordb.core.model.Pagination;\n"
                    + "import com.jmoordb.core.model.Sorted;\n"
                    + "import com.jmoordb.core.util.JmoordbCoreDateUtil;\n"
                    + "import java.util.HashSet;\n"
                    + "import " + repositoryData.getNameOfPackage() + repositoryData.getNameOfEntity() + ";\n\n\n"
                    + "// </editor-fold>\n";
            builder.append(code);
            
       

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="StringBuilder inject(Repository repository, RepositoryData repositoryData, String database, String collection)">
    public StringBuilder inject(Repository repository, RepositoryData repositoryData, String database, String collection) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";

            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"inject\">\n\n";

            code += "    @Inject\n"
                    + "    MongoClient mongoClient;\n"
                    + "/**\n"
                    + "* Microprofile Config\n"
                    + "*/\n"
                    + "    @Inject\n"
                    + "    Config config;\n"
                    + "    @Inject\n"
                    + "    @ConfigProperty(name = \"" + database + "\")\n"
                    + "    String mongodbDatabase;\n\n"
                    + "    String mongodbCollection = \"" + collection + "\";\n"
                    + "/**\n"
                    + "* AutogeneratedRepository\n"
                    + "*/\n"
                    + "    @Inject\n"
                    + "    " + repositoryData.getPackageOfRepository() + ".AutogeneratedRepository autogeneratedRepository;\n"
                    + "/**\n"
                    + "* Supplier\n"
                    + "*/\n"
                    + "    @Inject\n"
                    + "    " + repositoryData.getNameOfPackage() + repositoryData.getNameOfEntity() + "Supplier " + repositoryData.getNameOfEntityLower() + "Supplier;\n"
                    + "// </editor-fold>\n";
            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

// </editor-fold>
    

}
