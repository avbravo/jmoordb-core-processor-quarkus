package com.jmoordb.core.processor;

import com.jmoordb.core.processor.internal.MethodProcessorAux;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static com.jmoordb.core.annotation.app.MyAnnotationTypeProcessor.mirror;
import com.jmoordb.core.annotation.autosecuence.AutosecuenceRepository;
import com.jmoordb.core.processor.analizer.autosecuencerepository.AutosecuenceRepositoryAnalizer;
import com.jmoordb.core.processor.builder.AutosecuenceRepositorySourceBuilder;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

@SupportedAnnotationTypes(
        {"com.jmoordb.core.annotation.autosecuence.AutosecuenceRepository"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class AutosecuenceRepositoryProcessor extends AbstractProcessor {

    private Messager messager;

    // <editor-fold defaultstate="collapsed" desc=" boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)">
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
           

            if (annotations.size() == 0) {
                return false;
            }
            /**
             * Lee los elementos que tengan la anotacion @Repository
             */
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AutosecuenceRepository.class);

            List<String> uniqueIdCheckList = new ArrayList<>();

            for (Element element : elements) {
                AutosecuenceRepository autosecuenceRepository = element.getAnnotation(AutosecuenceRepository.class);
                
                

                TypeMirror typeEntity = mirror(autosecuenceRepository::entity);
                if (typeEntity == null) {
//                     error("Error processing the autosecuenceRepository entity",   element);

                }
                if (element.getKind() != ElementKind.INTERFACE) {

                    error("The annotation @AutosecuenceRepository can only be applied on interfaces: ",
                            element);

                } else {
                    boolean error = false;
                    /**
                     * Obtener el nombre de la entidad
                     */
//            

                    String nameOfEntity = ProcessorUtil.nameOfEntity(typeEntity);
                    String packageOfEntity = ProcessorUtil.packageOfTypeMirror(typeEntity);
                   

                    if (uniqueIdCheckList.contains(nameOfEntity)) {
                        error("AutosecuenceRepository has should be uniquely defined", element);
                        error = true;
                    }

                    error = !checkIdValidity(nameOfEntity, element);
                    if (!error) {
                        uniqueIdCheckList.add(nameOfEntity);
                        try {

                            builderClass(autosecuenceRepository, element, typeEntity);

                        } catch (Exception e) {
                            error(e.getMessage(), null);
                        }
                    }
                }
            }
        
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());

        }
        return false;
    }

    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="builderClass(AutosecuenceRepository autosecuenceRepository, Element element)">
    private void builderClass(AutosecuenceRepository autosecuenceRepository, Element element, TypeMirror typeEntity)
            throws Exception {
        try {
            //   testMsg("[generateClass(AutosecuenceRepository autosecuenceRepository, Element element)]", true);

            String pkg = getPackageName(element);
            //the target interface name
            String interfaceName = getTypeName(element);
            String nameOfEntity = ProcessorUtil.nameOfEntity(typeEntity);
            String packageOfEntity = ProcessorUtil.packageOfTypeMirror(typeEntity);
            // Valida el nombre de la base de datos
            String database = autosecuenceRepository.database().trim();
            
           
            if (autosecuenceRepository.database().equals("")) {
//                database = "{mongodb.database}";
                database = "{mongodb.jmoordb}";
            } else {
                database = autosecuenceRepository.database().replace("{", "").replace("}", "");
            }
            String collection = autosecuenceRepository.collection().trim();

            
            /**
             *
             * Procesa el contenido de la interface
             */
            AutosecuenceRepositoryAnalizer autosecuenceRepositoryAnalizer = AutosecuenceRepositoryAnalizer.get(element, messager, database, typeEntity);

            /**
             * Construye la clase
             */
            AutosecuenceRepositorySourceBuilder autosecuenceRepositorySourceBuilder = new AutosecuenceRepositorySourceBuilder();
            autosecuenceRepositorySourceBuilder.definePackage(pkg);

            /*
Import
             */
            autosecuenceRepositorySourceBuilder.addEditorFoldStart("imports");
            autosecuenceRepositorySourceBuilder.generateImport(autosecuenceRepository);

            autosecuenceRepositorySourceBuilder.addEditorFoldEnd();

            /**
             * Anotaciones
             */
            autosecuenceRepositorySourceBuilder.addAnnotations("@ApplicationScoped");
            /*
        Clase
             */
            autosecuenceRepositorySourceBuilder.defineClass("public class ", interfaceName + "Impl", " implements " + interfaceName);

            /**
             * Inject
             */
            autosecuenceRepositorySourceBuilder.addEditorFoldStart("inject");
            autosecuenceRepositorySourceBuilder.addInject("MongoClient mongoClient");

            autosecuenceRepositorySourceBuilder.addComment("Microprofile Config");
            autosecuenceRepositorySourceBuilder.addInject("Config config");

            autosecuenceRepositorySourceBuilder.addInjectConfigProperties(database, "String", "mongodbDatabase");
            autosecuenceRepositorySourceBuilder.addSentence("String mongodbCollection = \"" + collection + "\";");

            
            autosecuenceRepositorySourceBuilder.addEditorFoldEnd();
            
            
            
              /**
             * generate()
             */
               autosecuenceRepositorySourceBuilder.addEditorFoldStart("Long generate(String database, String collection)");
            autosecuenceRepositorySourceBuilder.generate();
            autosecuenceRepositorySourceBuilder.addEditorFoldEnd();

            /**
             * findAOneAndUpdate
             */
            autosecuenceRepositorySourceBuilder.addEditorFoldStart("Optional<Autosequence> findOneAndUpdate(String databasecollection)");
            autosecuenceRepositorySourceBuilder.findOneAndUpdate();
            autosecuenceRepositorySourceBuilder.addEditorFoldEnd();
            
            /**
             * get
             */

            autosecuenceRepositorySourceBuilder.addEditorFoldStart("Autosequence get(Supplier<? extends Autosequence> s, Document document)");
            autosecuenceRepositorySourceBuilder.get();
            autosecuenceRepositorySourceBuilder.addEditorFoldEnd();
            /**
             *save
             */

            autosecuenceRepositorySourceBuilder.addEditorFoldStart("Optional<Autosequence> save(Autosequence autosequence)");
            autosecuenceRepositorySourceBuilder.save();
            autosecuenceRepositorySourceBuilder.addEditorFoldEnd();
            
            /**
             * findById()
             */
               autosecuenceRepositorySourceBuilder.addEditorFoldStart("Optional<Autosequence> findById(String databasecollection)");
            autosecuenceRepositorySourceBuilder.findById();
            autosecuenceRepositorySourceBuilder.addEditorFoldEnd();
            
          
            
            
            
//        //nested builder class
            AutosecuenceRepositorySourceBuilder builder = null;
            String builderClassName = null;

            //adding class fields
            autosecuenceRepositorySourceBuilder.addFields(autosecuenceRepositoryAnalizer.getFields());
            if (builder != null) {
                builder.addFields(autosecuenceRepositoryAnalizer.getFields());
            }

            //adding constructor with mandatory fields
//            autosecuenceRepositorySourceBuilder.addConstructor(builder == null ? "public" : "private",
//                    autosecuenceRepositoryAnalizer.getMandatoryFields());
//            if (builder != null) {
//                builder.addConstructor("private", autosecuenceRepositoryAnalizer.getMandatoryFields());
//            }
            //generate methods
            for (Map.Entry<String, String> entry : autosecuenceRepositoryAnalizer.getFields().entrySet()) {
                String name = entry.getKey();
                String type = entry.getValue();
                boolean mandatory = autosecuenceRepositoryAnalizer.getMandatoryFields().contains(name);

                autosecuenceRepositorySourceBuilder.createGetterForField(name);

                //if no builder generation specified then crete setters for non mandatory fields
                if (builder == null && !mandatory) {
                    autosecuenceRepositorySourceBuilder.createSetterForField(name);
                }

                if (builder != null && !mandatory) {
                    builder.addMethod(new MethodProcessorAux()
                            .defineSignature("public", false, builderClassName)
                            .name(name)
                            .addParam(type, name)
                            .defineBody(" this." + name + " = " + name + ";"
                                    + AutosecuenceRepositorySourceBuilder.LINE_BREAK
                                    + " return this;"
                            )
                    );
                }
            }
            //testMsg("if (builder != null)", false);
            if (builder != null) {

                //generate create() method of the Builder class
                MethodProcessorAux createMethod = new MethodProcessorAux()
                        .defineSignature("public", true, builderClassName)
                        .name("create");

                String paramString = "(";
                int i = 0;
                for (String s : autosecuenceRepositoryAnalizer.getMandatoryFields()) {
                    createMethod.addParam(autosecuenceRepositoryAnalizer.getFields().get(s), s);
                    paramString += (i != 0 ? ", " : "") + s;
                    i++;
                }
                paramString += ");";

                createMethod.defineBody("return new " + builderClassName
                        + paramString);

                builder.addMethod(createMethod);

                //generate build() method of the builder class.
                //  testMsg("MethodProcessorAux buildMethod = new MethodProcessorAux()", false);
                MethodProcessorAux buildMethod = new MethodProcessorAux()
                        .defineSignature("public", false, autosecuenceRepository.entity().getName())
                        .name("build");
                StringBuilder buildBody = new StringBuilder();
                buildBody.append(autosecuenceRepository.entity())
                        .append(" a = new ")
                        .append(autosecuenceRepository.entity())
                        .append(paramString)
                        .append(AutosecuenceRepositorySourceBuilder.LINE_BREAK);
                for (String s : autosecuenceRepositoryAnalizer.getFields().keySet()) {
                    if (autosecuenceRepositoryAnalizer.getMandatoryFields().contains(s)) {
                        continue;
                    }
                    buildBody.append("a.")
                            .append(s)
                            .append(" = ")
                            .append(s)
                            .append(";")
                            .append(AutosecuenceRepositorySourceBuilder.LINE_BREAK);
                }
                buildBody.append("return a;")
                        .append(AutosecuenceRepositorySourceBuilder.LINE_BREAK);
                buildMethod.defineBody(buildBody.toString());

                builder.addMethod(buildMethod);
                autosecuenceRepositorySourceBuilder.addNestedClass(builder);

            }
            //finally generate class via Filer
//        generateClass(pkg + "." + autosecuenceRepository.entity(), implClass.end());
            //  testMsg("generateClass(pkg + \".\" + interfaceName + \"Impl\", implClass.end())", false);
            generateJavaFile(pkg + "." + interfaceName + "Impl", autosecuenceRepositorySourceBuilder.end());
        } catch (Exception e) {
            System.out.println("AutosecuenceRepository.generateClass() " + e.getLocalizedMessage());
        }
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String getPackageName(Element element)">

    private String getPackageName(Element element) {
        List<PackageElement> packageElements
                = ElementFilter.packagesIn(Arrays.asList(element.getEnclosingElement()));

        Optional<PackageElement> packageElement = packageElements.stream().findAny();
        return packageElement.isPresent()
                ? packageElement.get().getQualifiedName().toString() : null;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="generateClass(String qfn, String end)">
    private void generateJavaFile(String qfn, String end) throws IOException {
        try {

            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(qfn);
            Writer writer = sourceFile.openWriter();
            writer.write(end);
            writer.close();

        } catch (Exception e) {
            System.out.println("AutosecuenceRepository.generateClass() " + e.getLocalizedMessage());
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
                    error("AutosecuenceRepository as should be valid java "
                            + "identifier for code generation: " + name, e);

                    valid = false;
                }
            }
            if (name.equals(getTypeName(e))) {
                error("as should be different than the Interface name ", e);
            }
        } catch (Exception ex) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " error() " + ex.getLocalizedMessage());

        }
        // testMsg("valid = " + valid, false);
        return valid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String getTypeName(Element e)">
    /**
     * Get the simple name of the TypeMirror
     */
    private String getTypeName(Element e) {

        TypeMirror typeMirror = e.asType();
        String[] split = typeMirror.toString().split("\\.");
        try {

        } catch (Exception ex) {
            System.out.println("AutosecuenceRepository.getTypeName() " + ex.getLocalizedMessage());
        }
        return split.length > 0 ? split[split.length - 1] : null;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="error(String msg, Element e)">
    private void error(String msg, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
    // </editor-fold>

    private void checkMethod(ExecutableElement method) {
        // check for valid name
        String name = method.getSimpleName().toString();
     
//        if (!name.startsWith("set")) {
//            printError(method, "setter name must start with \"set\"");
//        } else if (name.length() == 3) {
//            printError(method, "the method name must contain more than just \"set\"");
//        } else if (Character.isLowerCase(name.charAt(3))) {
//            if (method.getParameters().size() != 1) {
//                printError(method, "character following \"set\" must be upper case");
//            }
//        }

        // check, if setter is public
//        if (!method.getModifiers().contains(Modifier.PUBLIC)) {
//            printError(method, "setter must be public");
//        }
//
//        // check, if method is static
//        if (method.getModifiers().contains(Modifier.STATIC)) {
//            printError(method, "setter must not be static");
//        }
    }

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        // get messager for printing errors
        messager = processingEnvironment.getMessager();
    }

    private void printError(Element element, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }
}
