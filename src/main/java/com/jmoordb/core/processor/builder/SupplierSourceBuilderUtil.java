package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.Entity;
import com.jmoordb.core.annotation.enumerations.AnnotationType;
import com.jmoordb.core.annotation.enumerations.JakartaSource;
import com.jmoordb.core.processor.model.EntityData;
import com.jmoordb.core.processor.methods.EntityField;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.JmoordbCoreFileUtil;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import java.util.List;
import javax.lang.model.element.Element;

public class SupplierSourceBuilderUtil {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

    // <editor-fold defaultstate="collapsed" desc="String numberOfParametersOfMethod(RepositoryMethod entityMethod)">
    /**
     *
     * @param entityMethod
     * @return Los parametros del metodo como una cadena
     */
    private Integer numberOfParametersOfMethod(RepositoryMethod entityMethod) {
        Integer number = 0;
        try {

            number = entityMethod.getParamTypeElement().size();

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
    public StringBuilder addRequestScoped() {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";
            builder.append("@RequestScoped\n");
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="StringBuilder generateImport(Entity entity, EntityData entityData)">
    public StringBuilder generateImport(Entity entity, EntityData entityData, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";

            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"imports\">\n\n";
            if (entity.jakartaSource() == JakartaSource.JAVAEE_LEGACY) {
                /*
            Java EE
                 */
                code += "import javax.enterprise.context.RequestScoped;\n"
                        + "import javax.inject.Inject;\n";

            } else {
                /**
                 * Jakarta EE
                 */
                code += "import jakarta.enterprise.context.RequestScoped;\n"
                        + "import jakarta.inject.Inject;\n";
                        
            }
            /**
             * Microprofile
             */

            code += "import java.io.Serializable;\n"
                    + "/**\n"
                    + "* Java\n"
                    + "*/\n"
                    + "import java.time.LocalDateTime;\n"
                    + "import java.lang.annotation.Annotation;\n"
                    + "import java.util.ArrayList;\n"
                    + "import java.util.List;\n"
                    + "import java.util.Optional;\n"
                    + "import java.util.function.Supplier;\n"
                    + "/**\n"
                    + "* Jmoordb\n"
                    + "*/\n"
                    + "import com.jmoordb.core.util.MessagesUtil;\n"
                    + "import com.jmoordb.core.util.JmoordbCoreDateUtil;\n"
                    + "import com.jmoordb.core.annotation.Referenced;\n"
                    + "import com.jmoordb.core.annotation.enumerations.TypePK;\n"
                    + "import com.jmoordb.core.annotation.enumerations.TypeReferenced;\n"
                    + "/**\n"
                    + "* MongoDB\n"
                    + "*/\n"
                    + "import org.bson.Document;\n"
                    + "import " + entityData.getPackageOfEntity() + "." + entityData.getEntityName() + ";\n"
                    + "import " + entityData.getPackageOfEntity() + ".*;\n\n\n"
                    + "// </editor-fold>\n";
            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="StringBuilder inject(Entity entity, EntityData entityData, String database, String collectio">
    public StringBuilder inject(Entity entity, EntityData entityData, String database, String collection, List<EntityField> entityFieldList, Element element, Boolean haveReferenced, Boolean haveEmbedded) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";

            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"inject\">\n\n";

            if (haveReferenced) {
                for (EntityField ef : entityFieldList) {
                    if (ef.getAnnotationType().equals(AnnotationType.REFERENCED)) {

                        String packagePath = JmoordbCoreFileUtil.packageOfFileInProject(element, JmoordbCoreUtil.letterToUpper(ef.getNameOfMethod()) + "Repository.java");
                        code += "    @Inject\n"
                                + "   " + packagePath + JmoordbCoreUtil.letterToUpper(ef.getNameOfMethod()) + "Repository " + ef.getNameOfMethod() + "Repository ;\n";
                        code += "    @Inject\n"
                                + "   " + JmoordbCoreUtil.letterToUpper(ef.getNameOfMethod()) + "Supplier " + ef.getNameOfMethod() + "Supplier ;\n";
                    }
                }
            }
            if (haveEmbedded) {
                for (EntityField ef : entityFieldList) {
                    if (ef.getAnnotationType().equals(AnnotationType.EMBEDDED)) {      
                        code += "    @Inject\n"
                                + "   " + JmoordbCoreUtil.letterToUpper(ef.getNameOfMethod()) + "Supplier " + ef.getNameOfMethod() + "Supplier ;\n";
                    }
                }
            }
            code += "// </editor-fold>\n";
            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

// </editor-fold>
// <editor-fold defaultstate="collapsed" desc=" Boolean haveEmbedded(List<EntityField> entityFieldList)">
    /**
     * Verifica si tiene un Embedded definido
     *
     * @param entityFieldList
     * @return
     */
    public static Boolean haveEmbedded(List<EntityField> entityFieldList) {
        Boolean result = Boolean.FALSE;
        try {
            for (EntityField ef : entityFieldList) {
                if (ef.getAnnotationType().equals(AnnotationType.EMBEDDED)) {
                    result = Boolean.TRUE;
                    break;
                }
            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;

    }

// </editor-fold>
// <editor-fold defaultstate="collapsed" desc=" Boolean haveEmbedded(List<EntityField> entityFieldList)">
    /**
     * Verifica si tiene un Embedded definido
     *
     * @param entityFieldList
     * @return
     */
    public static Boolean haveReferenced(List<EntityField> entityFieldList) {
        Boolean result = Boolean.FALSE;
        try {
            for (EntityField ef : entityFieldList) {
                if (ef.getAnnotationType().equals(AnnotationType.REFERENCED)) {
                    result = Boolean.TRUE;
                    break;
                }
            }
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;

    }

// </editor-fold>
}
