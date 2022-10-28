package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.DocumentEmbeddable;
import com.jmoordb.core.annotation.enumerations.AnnotationType;
import com.jmoordb.core.annotation.enumerations.JakartaSource;
import com.jmoordb.core.processor.model.DocumentEmbeddableData;
import com.jmoordb.core.processor.methods.DocumentEmbeddableField;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.JmoordbCoreFileUtil;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import java.util.List;
import javax.lang.model.element.Element;

public class DocumentEmbeddableSupplierSourceBuilderUtil {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

    // <editor-fold defaultstate="collapsed" desc="String numberOfParametersOfMethod(RepositoryMethod documentEmbeddableMethod)">
    /**
     *
     * @param documentEmbeddableMethod
     * @return Los parametros del metodo como una cadena
     */
    private Integer numberOfParametersOfMethod(RepositoryMethod documentEmbeddableMethod) {
        Integer number = 0;
        try {

            number = documentEmbeddableMethod.getParamTypeElement().size();

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

    // <editor-fold defaultstate="collapsed" desc="StringBuilder generateImport(DocumentEmbeddable documentEmbeddable, DocumentEmbeddableData documentEmbeddableData)">
    public StringBuilder generateImport(DocumentEmbeddable documentEmbeddable, DocumentEmbeddableData documentEmbeddableData, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";

            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"imports\">\n\n";
            if (documentEmbeddable.jakartaSource() == JakartaSource.JAVAEE_LEGACY) {
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
                    + "import java.lang.annotation.Annotation;\n"
                    + "import java.util.ArrayList;\n"
                    + "import java.util.List;\n"
                    + "import java.util.Optional;\n"
                    + "import java.util.function.Supplier;\n"
                    + "/**\n"
                    + "* Jmoordb\n"
                    + "*/\n"
                    + "import com.jmoordb.core.util.MessagesUtil;\n"
                    + "import com.jmoordb.core.annotation.Referenced;\n"
                    + "import com.jmoordb.core.annotation.enumerations.TypePK;\n"
                    + "import com.jmoordb.core.annotation.enumerations.TypeReferenced;\n"
                    + "/**\n"
                    + "* MongoDB\n"
                    + "*/\n"
                    + "import org.bson.Document;\n"
                    + "import " + documentEmbeddableData.getPackageOfDocumentEmbeddable() + "." + documentEmbeddableData.getDocumentEmbeddableName() + ";\n"
                    + "import " + documentEmbeddableData.getPackageOfDocumentEmbeddable() + ".*;\n\n\n"
                    + "// </editor-fold>\n";
            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="StringBuilder inject(DocumentEmbeddable documentEmbeddable, DocumentEmbeddableData documentEmbeddableData, String database, String collectio">
    public StringBuilder inject(DocumentEmbeddable documentEmbeddable, DocumentEmbeddableData documentEmbeddableData, String database, String collection, List<DocumentEmbeddableField> documentEmbeddableFieldList, Element element, Boolean haveReferenced, Boolean haveEmbedded) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";

            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"inject\">\n\n";

            if (haveReferenced) {
                for (DocumentEmbeddableField ef : documentEmbeddableFieldList) {
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
                for (DocumentEmbeddableField ef : documentEmbeddableFieldList) {
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
// <editor-fold defaultstate="collapsed" desc=" Boolean haveEmbedded(List<DocumentEmbeddableField> documentEmbeddableFieldList)">
    /**
     * Verifica si tiene un Embedded definido
     *
     * @param documentEmbeddableFieldList
     * @return
     */
    public static Boolean haveEmbedded(List<DocumentEmbeddableField> documentEmbeddableFieldList) {
        Boolean result = Boolean.FALSE;
        try {
            for (DocumentEmbeddableField ef : documentEmbeddableFieldList) {
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
// <editor-fold defaultstate="collapsed" desc=" Boolean haveEmbedded(List<DocumentEmbeddableField> documentEmbeddableFieldList)">
    /**
     * Verifica si tiene un Embedded definido
     *
     * @param documentEmbeddableFieldList
     * @return
     */
    public static Boolean haveReferenced(List<DocumentEmbeddableField> documentEmbeddableFieldList) {
        Boolean result = Boolean.FALSE;
        try {
            for (DocumentEmbeddableField ef : documentEmbeddableFieldList) {
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
