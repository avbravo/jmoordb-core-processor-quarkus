package com.jmoordb.core.processor;

import com.jmoordb.core.annotation.DocumentEmbeddable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.*;

import com.jmoordb.core.processor.model.DocumentEmbeddableData;
import com.jmoordb.core.processor.analizer.DocumentEmbeddableAnalizer;
import com.jmoordb.core.processor.builder.DocumentEmbeddableSupplierSourceBuilder;
import com.jmoordb.core.processor.methods.DocumentEmbeddableField;
import com.jmoordb.core.processor.model.DocumentEmbeddableDataSupplier;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.io.IOException;
import java.io.Writer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(
        {"com.jmoordb.core.annotation.DocumentEmbeddable"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class DocummentEmbedableProcessor extends AbstractProcessor {

    private Messager messager;
    private DocumentEmbeddableDataSupplier documentEmbeddableDataSupplier = new DocumentEmbeddableDataSupplier();

    // <editor-fold defaultstate="collapsed" desc=" boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)">
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
       

            if (annotations.size() == 0) {
                return false;
            }
            /**
             * Lee los elementos que tengan la anotacion @DocumentEmbeddable
             */
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DocumentEmbeddable.class);

            List<String> uniqueIdCheckList = new ArrayList<>();

            for (Element element : elements) {
               DocumentEmbeddable documentEmbeddable = element.getAnnotation(DocumentEmbeddable.class);

                if (element.getKind() != ElementKind.CLASS) {

                    error("The annotation @DocumentEmbeddable can only be applied on class: ",
                            element);

                } else {
                    boolean error = false;
                    /**
                     * Obtener datos del documentEmbeddable para DocumentEmbeddableData
                     */

                    DocumentEmbeddableData documentEmbeddableData = documentEmbeddableDataSupplier.get(DocumentEmbeddableData::new, element);
                    String nameOfDocumentEmbeddable = "";



                    if (uniqueIdCheckList.contains(documentEmbeddableData.getCollection())) {
                        error("DocumentEmbeddable has should be uniquely defined", element);
                        error = true;
                    }

                    error = !checkIdValidity(documentEmbeddableData.getCollection(), element);
                    if (!error) {
                        uniqueIdCheckList.add(documentEmbeddableData.getCollection());
                        try {

                            builderClass(documentEmbeddable, documentEmbeddableData, element);

                        } catch (Exception e) {
                            error(e.getMessage(), null);
                        }
                    }
                }
            }
            MessagesUtil.box("Proceso de analisis finalizado");
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());

        }
        return false;
    }

    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="builderClass(DocumentEmbeddable documentEmbeddable, DocumentEmbeddableData documentEmbeddableData,Element element, TypeMirror typeDocumentEmbeddable)">
    private void builderClass(DocumentEmbeddable documentEmbeddable, DocumentEmbeddableData documentEmbeddableData, Element element)
            throws Exception {
        try {

            /**
             *
             */

            /**
             * List<DocumentEmbeddableField almacena la información de los atributos de los
             * documentEmbeddable
             */
            List<DocumentEmbeddableField> documentEmbeddableFieldList = new ArrayList<>();
            /**
             *
             * Procesa el contenido de la interface
             */
            DocumentEmbeddableAnalizer documentEmbeddableAnalizer = DocumentEmbeddableAnalizer.get(element, messager, documentEmbeddableData.getDatabase(), documentEmbeddableFieldList, documentEmbeddableData);

            /**
             * Imprimo información de los metodos
             */
//            if (documentEmbeddableFieldList.isEmpty()) {
//                MessagesUtil.test("No hay información de los métodos para recorrer");
//            } else {
//                MessagesUtil.test("....................................................");
//                MessagesUtil.test("Imprimiendo List<DocumentEmbeddableField>");
//                for (DocumentEmbeddableField documentEmbeddableField : documentEmbeddableFieldList) {
//                    System.out.println(" " + documentEmbeddableField.toString());
//                }
//                MessagesUtil.test("....................................................");
//            }
            /**
             * Construye la clase Supplier
             */
            DocumentEmbeddableSupplierSourceBuilder documentEmbeddablesupplierSourceBuilder = new DocumentEmbeddableSupplierSourceBuilder();

            documentEmbeddablesupplierSourceBuilder.init(documentEmbeddable, documentEmbeddableData, documentEmbeddableFieldList, documentEmbeddableData.getDatabase(), documentEmbeddableData.getCollection(),element);

            /**
             * SupplierServices
             */
            /**
             * Crea el archivo
             */
            generateJavaFile(documentEmbeddableData.getPackageOfDocumentEmbeddable() + "." + documentEmbeddableData.getDocumentEmbeddableName() + "Supplier", documentEmbeddablesupplierSourceBuilder.end());

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="generateJavaFile(String qfn, String end)">
    private void generateJavaFile(String qfn, String end) throws IOException {
        try {

            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(qfn);
            Writer writer = sourceFile.openWriter();
            writer.write(end);
            writer.close();

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="boolean checkIdValidity(String name, Element e)">
    /**
     * Checking if the class to be generated is a valid java identifier Also the
     * name should be not same as the target interface
     */
    private boolean checkIdValidity(String name, Element e) {
        boolean valid = true;
        try {

            for (int i = 0; i < name.length(); i++) {
                if (i == 0 ? !Character.isJavaIdentifierStart(name.charAt(i))
                        : !Character.isJavaIdentifierPart(name.charAt(i))) {
                    error("DocumentEmbeddable as should be valid java "
                            + "identifier for code generation: " + name, e);

                    valid = false;
                }
            }
            if (name.equals(ProcessorUtil.getTypeName(e))) {
                error("as should be different than the Interface name ", e);
            }
        } catch (Exception ex) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + ex.getLocalizedMessage());

        }
        // testMsg("valid = " + valid, false);
        return valid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="error(String msg, Element e)">
    private void error(String msg, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="init(ProcessingEnvironment processingEnvironment)">
    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        messager = processingEnvironment.getMessager();
    }
// </editor-fold>    
// <editor-fold defaultstate="collapsed" desc="printError(Element element, String message)">

    private void printError(Element element, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }
// </editor-fold>

}
