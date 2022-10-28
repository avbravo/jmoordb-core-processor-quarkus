package com.jmoordb.core.processor.builder;

import com.jmoordb.core.annotation.DateSupport;
import com.jmoordb.core.annotation.enumerations.JakartaSource;
import com.jmoordb.core.processor.model.DateSupportData;
import com.jmoordb.core.processor.methods.DateSupportField;
import com.jmoordb.core.processor.methods.RepositoryMethod;
import com.jmoordb.core.util.MessagesUtil;
import java.util.List;
import javax.lang.model.element.Element;

public class DateSupportSupplierSourceBuilderUtil {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB = "   ";
    private String className;

    // <editor-fold defaultstate="collapsed" desc="String numberOfParametersOfMethod(RepositoryMethod dateSupportMethod)">
    /**
     *
     * @param dateSupportMethod
     * @return Los parametros del metodo como una cadena
     */
    private Integer numberOfParametersOfMethod(RepositoryMethod dateSupportMethod) {
        Integer number = 0;
        try {

            number = dateSupportMethod.getParamTypeElement().size();

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
    public StringBuilder addProviderScoped() {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";
            builder.append("@Provider\n");
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="StringBuilder generateImport(DateSupport dateSupport, DateSupportData dateSupportData)">
    public StringBuilder generateImport(DateSupport dateSupport, DateSupportData dateSupportData, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";



            
            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"imports\">\n\n";
            if (dateSupport.jakartaSource() == JakartaSource.JAVAEE_LEGACY) {
                /*
            Java EE
                 */
                

                code += "import javax.ws.rs.WebApplicationException;\n"
                        + "import javax.ws.rs.ext.ParamConverter;\n";
      

            } else {
                /**
                 * Jakarta EE
                 */
                code += "import jakarta.ws.rs.ext.ParamConverter;\n"
                        + "import jakarta.ws.rs.ext.ParamConverter;\n";
           
                        
                        
            }
            /**
             * Microprofile
             */





            code += "import java.io.Serializable;\n"
                    + "/**\n"
                    + "* Java\n"
                    + "*/\n"
                    + "import java.util.Date;\n"
                    + "/**\n"
                    + "* Jmoordb\n"
                    + "*/\n"
                    + "import com.jmoordb.core.annotation.date.DateFormat;\n"
                    + "import com.jmoordb.core.annotation.date.DateTimeFormat;\n"
                    + "import java.text.ParseException;\n"
                    + "import java.text.SimpleDateFormat;\n\n\n"
                   
                    + "// </editor-fold>\n";
            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="StringBuilder generateImportProvider(DateSupport dateSupport, DateSupportData dateSupportData)">
    public StringBuilder generateImportProvider(DateSupport dateSupport, DateSupportData dateSupportData, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";



            
            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"imports\">\n\n";
            if (dateSupport.jakartaSource() == JakartaSource.JAVAEE_LEGACY) {
                /*
            Java EE
                 */
                

                code += "import javax.ws.rs.WebApplicationException;\n"
                        + "import javax.ws.rs.ext.ParamConverter;\n"
                        + "import javax.ws.rs.ext.ParamConverterProvider;\n"
                        + "import javax.ws.rs.ext.Provider;\n";

            } else {
                /**
                 * Jakarta EE
                 */
                code += "import jakarta.ws.rs.ext.ParamConverter;\n"
                        + "import jakarta.ws.rs.ext.ParamConverter;\n"
                        + "import jakarta.ws.rs.ext.ParamConverterProvider;\n"
                        + "import jakarta.ws.rs.ext.Provider;\n";
                        
                        
            }
            /**
             * Microprofile
             */





            code += "import java.io.Serializable;\n"
                    + "/**\n"
                    + "* Java\n"
                    + "*/\n"
                    + "import java.util.Date;\n"
                    + "import java.lang.annotation.Annotation;\n"
                    + "import java.lang.reflect.Type;\n"
                    + "/**\n"
                    + "* Jmoordb\n"
                    + "*/\n"
                    + "import com.jmoordb.core.annotation.date.DateFormat;\n"
                    + "import com.jmoordb.core.annotation.date.DateTimeFormat;\n"
                    + "import java.text.ParseException;\n"
                    + "import java.text.SimpleDateFormat;\n\n\n"
                   
                    + "// </editor-fold>\n";
            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="StringBuilder defineParameterConverter(DateSupport dateSupport, DateSupportData dateSupportData, List<DateSupportField> dateSupportFieldList, Element element)">
    public StringBuilder defineParameterConverter(DateSupport dateSupport, DateSupportData dateSupportData, List<DateSupportField> dateSupportFieldList, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";

            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"code\">\n\n";
            code += "     public static final String DEFAULT_FORMAT = DateTimeFormat.DEFAULT_DATE_TIME;\n";
            code += "     private DateTimeFormat customDateTimeFormat;\n";
            code += "     private DateFormat customDateFormat;\n\n";
            code += "     public void setCustomDateFormat(DateFormat customDateFormat) {\n";
            code += "        this.customDateFormat = customDateFormat;\n";
            code += "     }\n\n";
            code += "     public void setCustomDateTimeFormat(DateTimeFormat customDateTimeFormat) {\n";
            code += "       this.customDateTimeFormat = customDateTimeFormat;\n";
            code += "     }\n\n";
            code += "     @Override\n";
            code += "     public Date fromString(String string) {\n";
            code += "       String format = DEFAULT_FORMAT;\n";
            code += "       if (customDateFormat != null) {\n";
            code += "           format = customDateFormat.value();\n";
            code += "       } else if (customDateTimeFormat != null) {\n";
            code += "           format = customDateTimeFormat.value();\n";
            code += "       };\n\n";
            code += "       final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);\n\n";
            code += "       try {\n";
            code += "           return simpleDateFormat.parse(string);\n";
            code += "       } catch (ParseException ex) {\n";
            code += "           throw new WebApplicationException(ex);\n";
            code += "       }\n";
            code += "     }\n\n";
            code += "     @Override\n";
            code += "     public String toString(Date date) {\n";
            code += "         return new SimpleDateFormat(DEFAULT_FORMAT).format(date);\n";
            code += "     }\n\n";
                    
    
  
            code += "// </editor-fold>\n";
            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="StringBuilder defineParameterConverterProvider(DateSupport dateSupport, DateSupportData dateSupportData, List<DateSupportField> dateSupportFieldList, Element element)">
    public StringBuilder defineParameterConverterProvider(DateSupport dateSupport, DateSupportData dateSupportData, List<DateSupportField> dateSupportFieldList, Element element) {
        StringBuilder builder = new StringBuilder();
        try {
            String code = "";

            code += "// <editor-fold defaultstate=\"collapsed\" desc=\"code\">\n\n";
            code += "     @SuppressWarnings(\"unchecked\")\n";
            code += "     @Override\n";
            code += "     public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {\n";
            code += "       if (Date.class.equals(rawType)) {\n";
            code += "           final DateParameterConverter dateParameterConverter = new DateParameterConverter();\n\n";
            code += "           for (Annotation annotation : annotations) { \n";
            code += "               if (DateTimeFormat.class.equals(annotation.annotationType())) {\n";
            code += "                   dateParameterConverter. setCustomDateTimeFormat((DateTimeFormat) annotation);\n";
            code += "               } else if (DateFormat.class.equals(annotation.annotationType())) { \n";
            code += "                       dateParameterConverter.setCustomDateFormat((DateFormat) annotation);\n";
            code += "               } \n";
            code += "           }\n";
            code += "            return (ParamConverter<T>) dateParameterConverter;\n";
            code += "     }\n";
            code += "     return null;\n";
            code += "     \n";            
            code += "     }\n\n";
                    
    
  
            code += "// </editor-fold>\n";
            builder.append(code);

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return builder;
    }

// </editor-fold>

}
