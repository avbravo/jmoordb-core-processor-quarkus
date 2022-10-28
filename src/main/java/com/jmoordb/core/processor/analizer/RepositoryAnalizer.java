package com.jmoordb.core.processor.analizer;

import com.jmoordb.core.annotation.date.ExcludeTime;
import com.jmoordb.core.annotation.date.IncludeTime;
import com.jmoordb.core.annotation.repository.Count;
import com.jmoordb.core.annotation.repository.Delete;
import com.jmoordb.core.annotation.enumerations.CaseSensitive;
import com.jmoordb.core.annotation.enumerations.TypeOrder;
import com.jmoordb.core.annotation.enumerations.AnnotationType;
import com.jmoordb.core.annotation.enumerations.ReturnType;
import com.jmoordb.core.annotation.repository.CountBy;
import com.jmoordb.core.annotation.repository.CountLikeBy;
import com.jmoordb.core.annotation.repository.DeleteBy;
import com.jmoordb.core.annotation.repository.Find;
import com.jmoordb.core.annotation.repository.LikeBy;
import com.jmoordb.core.annotation.repository.Ping;
import com.jmoordb.core.annotation.repository.Query;

import com.jmoordb.core.annotation.repository.Save;
import com.jmoordb.core.annotation.repository.Update;
import com.jmoordb.core.util.ProcessorUtil;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.processing.Messager;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import com.jmoordb.core.annotation.repository.Lookup;
import com.jmoordb.core.annotation.repository.Regex;
import com.jmoordb.core.annotation.repository.RegexCount;
import com.jmoordb.core.processor.methods.ParamTypeElement;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.processor.methods.WhereDescomposed;

/**
 * Converts getters to field
 */
public class RepositoryAnalizer {
// <editor-fold defaultstate="collapsed" desc="fields()">

    private Messager messager;
    private StringBuilder builderMethods = new StringBuilder();

//    private final LinkedHashMap<String, String> fields;
//    private final List<String> mandatoryFields;
//    public RepositoryAnalizer(LinkedHashMap<String, String> fields, List<String> mandatoryFields) {
//
//        this.fields = fields;
//        this.mandatoryFields = mandatoryFields;
//    }
    // </editor-fold>
    public RepositoryAnalizer() {
    }

// <editor-fold defaultstate="collapsed" desc="set/get">
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="RepositoryAnalizer get(Element element, Messager messager, String database, TypeMirror typeEntity, List<RepositoryMethod> repositoryMethodList)">
    /**
     * Procesa los metodos definidos en la interface
     *
     * @param element
     * @return
     */
    public static RepositoryAnalizer get(Element element, Messager messager, String database, TypeMirror typeEntity, List<RepositoryMethod> repositoryMethodList) {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        List<String> mandatoryFields = new ArrayList<>();

        for (ExecutableElement executableElement : ElementFilter.methodsIn(element.getEnclosedElements())) {
            //metodo que almacena la información del repositorio

            if (executableElement.getKind() != ElementKind.METHOD) {
                continue;
            }
            /**
             * Obtiene el nombre del metodo
             */

            String methodName = ProcessorUtil.nameOfMethod(executableElement);
            //Parametros
            List<ParamTypeElement> paramTypeElementList = new ArrayList<>();
            List<String> tokenWhere = new ArrayList<>();

            List<String> lexemas = new ArrayList<>();
            List<String> worldAndToken = new ArrayList<>();
            /**
             * Definición inicial
             */
            RepositoryMethod repositoryMethod = new RepositoryMethod.Builder()
                    .nameOfMethod(methodName)
                    .caseSensitive(CaseSensitive.NONE)
                    .annotationType(AnnotationType.NONE)
                    .paramTypeElement(paramTypeElementList)
                    .returnType(ReturnType.NONE)
                    .returnTypeValue("")
                    .typeOrder(TypeOrder.NONE)
                    .where("")
                    .tokenWhere(tokenWhere)
                    .whereDescomposed(new WhereDescomposed())
                    .havePagination(Boolean.FALSE)
                    .haveSorted(Boolean.FALSE)
                    .nameOfParametersPagination("")
                    .nameOfParametersSorted("")
                    .lexemas(lexemas)
                    .worldAndToken(worldAndToken)
                    .build();

            if (methodName.equals("findByPKOfEntity")) {
                messager.printMessage(Diagnostic.Kind.ERROR, "The method name findByPKOfEntity() is reserved for internal framework use, please rename your method.", element);
            }
            /**
             * Obtengo el valor de retorno convierte a ReturnType.
             */
            TypeMirror returnTypeOfMethod = executableElement.getReturnType();

            repositoryMethod.setReturnTypeValue(returnTypeOfMethod.toString());
            repositoryMethod.setReturnType(ProcessorUtil.convertToReturnType(returnTypeOfMethod.toString(), typeEntity));

            List<? extends VariableElement> parameters = executableElement.getParameters();
            if (parameters.size() <= 0) {

            } else {
                /**
                 * Se cargan los parámetros del metodo.
                 */
                Boolean isIncludeTime = Boolean.FALSE;
                Boolean isExcludeTime = Boolean.FALSE;

                for (int i = 0; i < parameters.size(); i++) {

                    VariableElement param = parameters.get(i);

                    TypeMirror secondArgumentType = param.asType();
                    System.out.println("Quitar ++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("Quitar " + param.getSimpleName().toString() + " [[TypeMirror ]]]" + secondArgumentType);

                    if (parameters.get(i).getAnnotation(IncludeTime.class) != null) {
                        System.out.println("Quitar------> es Include");
                        isIncludeTime = Boolean.TRUE;
                    } else {

                        if (parameters.get(i).getAnnotation(ExcludeTime.class) != null) {
                            System.out.println("Quitar ------> es Exclude");
                            isExcludeTime = Boolean.TRUE;
                        } else {
                            isIncludeTime = Boolean.FALSE;
                            isExcludeTime = Boolean.FALSE;
                        }
                    }
                   
                    ParamTypeElement paramTypeElement = new ParamTypeElement.Builder()
                            .type(param.asType().toString())
                            .name(param.getSimpleName().toString())
                            .isExcludeTime(isExcludeTime)
                            .isIncludeTime(isIncludeTime)
                            .build();

                    paramTypeElementList.add(paramTypeElement);

                }
                repositoryMethod.setParamTypeElement(paramTypeElementList);

            }

            /*
             *
             * Verifica si es una anotación valida
             *
             */
            if (!ProcessorUtil.isValidAnnotationOfRepository(executableElement)) {
                // No tiene anotaciones validas
                messager.printMessage(Diagnostic.Kind.ERROR, "Methods " + methodName + " without declaring valid annotation for a Repository interface", element);

                return new RepositoryAnalizer();
            }

            /**
             * Verifico si el metodo tiene anotación Query.classs E invoco el
             * analizador de la anotación
             */
            Query query = executableElement.getAnnotation(Query.class);
            if (query != null) {

                if (!QueryAnalizer.analizer(query, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, QueryAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.QUERY);

            }
            /**
             * @Find Verifico si el metodo tiene anotación Find.classs E invoco
             * el analizador de la anotación
             */
            Find find = executableElement.getAnnotation(Find.class);
            if (find != null) {

                if (!FindAnalizer.analizer(find, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, FindAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.FIND);

            }
            /**
             * @LikeBy
             */
            LikeBy likeBy = executableElement.getAnnotation(LikeBy.class);
            if (likeBy != null) {

                if (!LikeByAnalizer.analizer(likeBy, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, LikeByAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.LIKEBY);

            }
            /**
             * @CountLikeBy
             */
            CountLikeBy countLikeBy = executableElement.getAnnotation(CountLikeBy.class);
            if (countLikeBy != null) {

                if (!CountLikeByAnalizer.analizer(countLikeBy, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, CountLikeByAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.COUNTLIKEBY);

            }
            /**
             * Verifico si el metodo tiene anotación DeleteBy.classs E invoco el
             * analizador de la anotación
             */
            DeleteBy deleteBy = executableElement.getAnnotation(DeleteBy.class);
            if (deleteBy != null) {

                if (!DeleteByAnalizer.analizer(deleteBy, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, DeleteByAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.DELETEBY);

            }

            /**
             * Lee la anotación @QueryJSOM
             */
            Lookup lookup = executableElement.getAnnotation(Lookup.class);
            if (lookup != null) {

                /**
                 * Imprimo el valor de la anotación Query
                 */
                if (!LookupAnalizer.analizer(lookup, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, LookupAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.LOOKUP);
            }

            /**
             * Lee la anotación @QueryJSOM
             */
            Regex regex = executableElement.getAnnotation(Regex.class);
            if (regex != null) {

                if (!RegexAnalizer.analizer(regex, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, RegexAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.REGEX);
            }

            /**
             * Lee la anotación CountRegex
             */
            RegexCount regexCount = executableElement.getAnnotation(RegexCount.class);
            if (regexCount != null) {

                if (!RegexCountAnalizer.analizer(regexCount, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, RegexCountAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.REGEXCOUNT);
            }

            /**
             * Count
             */
            Count count = executableElement.getAnnotation(Count.class);
            if (count != null) {

                if (!CountAnalizer.analizer(count, element, executableElement, typeEntity)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, CountAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.COUNT);
            }
            /**
             * CountBy
             */
            CountBy countBy = executableElement.getAnnotation(CountBy.class);
            if (countBy != null) {

                if (!CountByAnalizer.analizer(countBy, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, CountByAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.COUNTBY);

            }

            /**
             * Lee la anotacion @Ping
             */
            Ping ping = executableElement.getAnnotation(Ping.class);
            if (ping != null) {
                if (!PingAnalizer.analizer(ping, element, executableElement, typeEntity)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, PingAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.PING);
            }

            /**
             * Lee la anotacion @Update
             */
            Update update = executableElement.getAnnotation(Update.class);
            if (update != null) {
                if (!UpdateAnalizer.analizer(update, element, executableElement, typeEntity)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, UpdateAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.UPDATE);
            }
            /**
             * Lee la anotacion @Save
             */
            Save save = executableElement.getAnnotation(Save.class);
            if (save != null) {
                if (!SaveAnalizer.analizer(save, element, executableElement, typeEntity)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, SaveAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.SAVE);
            }
            /**
             * Lee la anotacion @Delete
             */
            Delete delete = executableElement.getAnnotation(Delete.class);
            if (delete != null) {
                if (!DeleteAnalizer.analizer(delete, element, executableElement, typeEntity, repositoryMethod)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, DeleteAnalizer.getMessage(), element);
                }
                repositoryMethod.setAnnotationType(AnnotationType.DELETE);
            }
            /**
             * Almacena la informacion del repository
             */
            repositoryMethodList.add(repositoryMethod);

            /**
             * Falta aqui verificar cuando un metodo no tiene anotaciones enviar
             * un mensaje de error
             *
             */
        }

//        return new RepositoryAnalizer(fields, mandatoryFields);
        return new RepositoryAnalizer();
    }
// </editor-fold>

}
