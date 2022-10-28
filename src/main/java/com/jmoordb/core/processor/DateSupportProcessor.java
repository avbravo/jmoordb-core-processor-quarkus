package com.jmoordb.core.processor;

import com.jmoordb.core.annotation.DateSupport;
import com.jmoordb.core.processor.analizer.DateSupportAnalizer;
import com.jmoordb.core.processor.builder.DateSupportSupplierSourceBuilder;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.*;

import com.jmoordb.core.processor.methods.DateSupportField;
import com.jmoordb.core.processor.model.DateSupportData;
import com.jmoordb.core.processor.model.DateSupportDataSupplier;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.io.IOException;
import java.io.Writer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(
        {"com.jmoordb.core.annotation.DateSupport"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class DateSupportProcessor extends AbstractProcessor {

    private Messager messager;
    private DateSupportDataSupplier dateSupportDataSupplier = new DateSupportDataSupplier();

    // <editor-fold defaultstate="collapsed" desc=" boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)">
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            MessagesUtil.box("Iniciando proceso de analisis de @DateSupport");

            if (annotations.size() == 0) {
                return false;
            }
            /**
             * Lee los elementos que tengan la anotacion @DateSupport
             */
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DateSupport.class);

            List<String> uniqueIdCheckList = new ArrayList<>();

            for (Element element : elements) {
               DateSupport dateSupport = element.getAnnotation(DateSupport.class);

                if (element.getKind() != ElementKind.CLASS) {

                    error("The annotation @DateSupport can only be applied on class: ",
                            element);

                } else {
                    boolean error = false;
                    /**
                     * Obtener datos del dateSupport paraDateSupportData
                     */

                   DateSupportData dateSupportData = dateSupportDataSupplier.get(DateSupportData::new, element);
                    String nameOfDateSupport = "";



                   
               
                    if (!error) {
               
                        try {

                            builderClass(dateSupport, dateSupportData, element);

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
// <editor-fold defaultstate="collapsed" desc="builderClass(DateSupport dateSupport,DateSupportData dateSupportData,Element element, TypeMirror typeDateSupport)">
    private void builderClass(DateSupport dateSupport,DateSupportData dateSupportData, Element element)
            throws Exception {
        try {

         
            /**
             * List<DateSupportField almacena la información de los atributos de los
             * dateSupport
             */
            List<DateSupportField> dateSupportFieldList = new ArrayList<>();
            /**
             *
             * Procesa el contenido de la interface
             */
           DateSupportAnalizer dateSupportAnalizer =DateSupportAnalizer.get(element, messager,  dateSupportFieldList, dateSupportData);


            /**
             * Construye la clase Supplier
             */
            DateSupportSupplierSourceBuilder supplierSourceBuilder = new DateSupportSupplierSourceBuilder();

            supplierSourceBuilder.init(dateSupport, dateSupportData, dateSupportFieldList, element);

            /**
             * SupplierServices
             */
            /**
             * Crea el archivo
             */
//            generateJavaFile(dateSupportData.getPackageOfDateSupport() + "." + dateSupportData.getDateSupportName() + "Converter", supplierSourceBuilder.end());
            generateJavaFile(dateSupportData.getPackageOfDateSupport() + "." + "DateParameterConverter", supplierSourceBuilder.end());
            generateJavaFile(dateSupportData.getPackageOfDateSupport() + "." + "DateParameterConverterProvider", supplierSourceBuilder.endProvider());

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
                    error("DateSupport as should be valid java "
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
