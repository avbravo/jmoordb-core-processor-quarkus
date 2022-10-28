package com.jmoordb.core.processor;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import com.jmoordb.core.annotation.repository.Repository;

import static com.jmoordb.core.annotation.app.MyAnnotationTypeProcessor.mirror;
import com.jmoordb.core.processor.model.RepositoryData;
import com.jmoordb.core.processor.model.RepositoryDataSupplier;
import com.jmoordb.core.processor.builder.RepositorySourceBuilder;
import com.jmoordb.core.processor.analizer.RepositoryAnalizer;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;

@SupportedAnnotationTypes(
        {"com.jmoordb.core.annotation.repository.Repository"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RepositoryProcessor extends AbstractProcessor {

    private Messager messager;
    private RepositoryDataSupplier repositoryDataSupplier = new RepositoryDataSupplier();

    // <editor-fold defaultstate="collapsed" desc=" boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)">
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            MessagesUtil.box("Iniciando proceso de analisis de @Repository");

            if (annotations.size() == 0) {
                return false;
            }
            /**
             * Lee los elementos que tengan la anotacion @Repository
             */
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Repository.class);

            List<String> uniqueIdCheckList = new ArrayList<>();

            for (Element element : elements) {
                Repository repository = element.getAnnotation(Repository.class);

                /**
                 * Evalua la entidad definida en el
                 *
                 * @Repository(entity=MyEntity.class)
                 */
                TypeMirror typeEntity = mirror(repository::entity);
                if (typeEntity == null) {
//                     error("Error processing the repository entity",   element);

                }
                if (element.getKind() != ElementKind.INTERFACE) {

                    error("The annotation @Repository can only be applied on interfaces: ",
                            element);

                } else {
                    boolean error = false;
                    /**
                     * Obtener datos del repository para RepositoryData
                     */

                    RepositoryData repositoryData = repositoryDataSupplier.get(RepositoryData::new, element);

                    if (uniqueIdCheckList.contains(repositoryData.getNameOfEntity())) {
                        error("Repository has should be uniquely defined", element);
                        error = true;
                    }

                    error = !checkIdValidity(repositoryData.getNameOfEntity(), element);
                    if (!error) {
                        uniqueIdCheckList.add(repositoryData.getNameOfEntity());
                        try {

                            builderClass(repository, repositoryData, element, typeEntity);

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
// <editor-fold defaultstate="collapsed" desc="builderClass(Repository repository, RepositoryData repositoryData,Element element, TypeMirror typeEntity)">
    private void builderClass(Repository repository, RepositoryData repositoryData, Element element, TypeMirror typeEntity)
            throws Exception {
        try {

            String database = repository.database().trim();
            if (repository.database().equals("")) {
                database = "{mongodb.database}";
            } else {
                database = repository.database().replace("{", "").replace("}", "");
            }
                        String collection ="";
                        if(repository.collection().equals("")){
                           collection = repositoryData.getNameOfEntityLower();
                        }else{
                        collection = repository.collection().trim();
                        }

            /**
             * List<RepositoryMethod> almacena la información de los métodos de
             * los repositorios
             */
            List<RepositoryMethod> repositoryMethodList = new ArrayList<>();
            /**
             *
             * Procesa el contenido de la interface
             */
            RepositoryAnalizer repositoryAnalizer = RepositoryAnalizer.get(element, messager, database, typeEntity, repositoryMethodList);


            /**
             * Construye la clase
             */
            RepositorySourceBuilder repositorySourceBuilder = new RepositorySourceBuilder();

            repositorySourceBuilder.init(repository, repositoryData, repositoryMethodList, database, collection);


            generateJavaFile(repositoryData.getPackageOfRepository() + "." + repositoryData.getInterfaceName() + "Impl", repositorySourceBuilder.end());
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
                    error("Repository as should be valid java "
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

        // get messager for printing errors
        messager = processingEnvironment.getMessager();
    }
// </editor-fold>    
    
// <editor-fold defaultstate="collapsed" desc="printError(Element element, String message)">

    private void printError(Element element, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }
// </editor-fold>
}
