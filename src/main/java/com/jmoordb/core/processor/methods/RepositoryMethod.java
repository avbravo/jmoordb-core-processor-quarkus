/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.methods;

import com.jmoordb.core.annotation.enumerations.CaseSensitive;
import com.jmoordb.core.annotation.enumerations.TypeOrder;
import com.jmoordb.core.annotation.enumerations.AnnotationType;
import com.jmoordb.core.annotation.enumerations.ReturnType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avbravo
 */
public class RepositoryMethod {

    private AnnotationType annotationType;
    private ReturnType returnType;
    private String returnTypeValue;
    private String nameOfMethod;
    private CaseSensitive caseSensitive;
    private String where = "";
    private List<String> tokenWhere = new ArrayList<>();
    private List<ParamTypeElement> paramTypeElement;
    private TypeOrder typeOrder;
    private Boolean havePagination = Boolean.FALSE;
    private Boolean haveSorted = Boolean.FALSE;
    private String nameOfParametersPagination;
    private String nameOfParametersSorted;
    private WhereDescomposed whereDescomposed;
    private List<String> lexemas = new ArrayList<>();
    private List<String> worldAndToken = new ArrayList<>();
    private List<String> includeTimeFields = new ArrayList<>();
    private List<String> excludeTimeFields = new ArrayList<>();
    
       

    public RepositoryMethod() {
    }

    public RepositoryMethod(AnnotationType annotationType, ReturnType returnType, String returnTypeValue, String nameOfMethod, CaseSensitive caseSensitive, List<ParamTypeElement> paramTypeElement,
            TypeOrder typeOrder,
            Boolean havePagination, Boolean haveSorted, String nameOfParametersPagination, String nameOfParametersSorted, WhereDescomposed whereDescomposed, List<String> lexemas, List<String> worldAndToken,
             List<String> includeTimeFields,List<String> excludeTimeFields ) {
        this.annotationType = annotationType;
        this.returnType = returnType;
        this.returnTypeValue = returnTypeValue;
        this.nameOfMethod = nameOfMethod;
        this.caseSensitive = caseSensitive;
        this.paramTypeElement = paramTypeElement;
        this.typeOrder = typeOrder;
        this.havePagination = havePagination;
        this.haveSorted = haveSorted;
        this.whereDescomposed = whereDescomposed;
        this.lexemas = lexemas;
        this.worldAndToken = worldAndToken;
        this.includeTimeFields = includeTimeFields;
        this.excludeTimeFields = excludeTimeFields;

    }

    public List<String> getIncludeTimeFields() {
        return includeTimeFields;
    }

    public void setIncludeTimeFields(List<String> includeTimeFields) {
        this.includeTimeFields = includeTimeFields;
    }

    public List<String> getExcludeTimeFields() {
        return excludeTimeFields;
    }

    public void setExcludeTimeFields(List<String> excludeTimeFields) {
        this.excludeTimeFields = excludeTimeFields;
    }

    
    
    
    public WhereDescomposed getWhereDescomposed() {
        return whereDescomposed;
    }

    public void setWhereDescomposed(WhereDescomposed whereDescomposed) {
        this.whereDescomposed = whereDescomposed;
    }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public Boolean getHavePagination() {
        return havePagination;
    }

    public void setHavePagination(Boolean havePagination) {
        this.havePagination = havePagination;
    }

    public Boolean getHaveSorted() {
        return haveSorted;
    }

    public void setHaveSorted(Boolean haveSorted) {
        this.haveSorted = haveSorted;
    }

    public void setAnnotationType(AnnotationType annotationType) {
        this.annotationType = annotationType;
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public String getNameOfMethod() {
        return nameOfMethod;
    }

    public void setNameOfMethod(String nameOfMethod) {
        this.nameOfMethod = nameOfMethod;
    }

    public CaseSensitive getCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(CaseSensitive caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public List<String> getTokenWhere() {
        return tokenWhere;
    }

    public void setTokenWhere(List<String> tokenWhere) {
        this.tokenWhere = tokenWhere;
    }

    public List<ParamTypeElement> getParamTypeElement() {
        return paramTypeElement;
    }

    public void setParamTypeElement(List<ParamTypeElement> paramTypeElement) {
        this.paramTypeElement = paramTypeElement;
    }

    public TypeOrder getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(TypeOrder typeOrder) {
        this.typeOrder = typeOrder;
    }

    public String getReturnTypeValue() {
        return returnTypeValue;
    }

    public void setReturnTypeValue(String returnTypeValue) {
        this.returnTypeValue = returnTypeValue;
    }

    public String getNameOfParametersPagination() {
        return nameOfParametersPagination;
    }

    public void setNameOfParametersPagination(String nameOfParametersPagination) {
        this.nameOfParametersPagination = nameOfParametersPagination;
    }

    public String getNameOfParametersSorted() {
        return nameOfParametersSorted;
    }

    public void setNameOfParametersSorted(String nameOfParametersSorted) {
        this.nameOfParametersSorted = nameOfParametersSorted;
    }

    public List<String> getLexemas() {
        return lexemas;
    }

    public void setLexemas(List<String> lexemas) {
        this.lexemas = lexemas;
    }

    public List<String> getWorldAndToken() {
        return worldAndToken;
    }

    public void setWorldAndToken(List<String> worldAndToken) {
        this.worldAndToken = worldAndToken;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RepositoryMethod{");
        sb.append("annotationType=").append(annotationType);
        sb.append(", returnType=").append(returnType);
        sb.append(", returnTypeValue=").append(returnTypeValue);
        sb.append(", nameOfMethod=").append(nameOfMethod);
        sb.append(", caseSensitive=").append(caseSensitive);
        sb.append(", where=").append(where);
        sb.append(", tokenWhere=").append(tokenWhere);
        sb.append(", paramTypeElement=").append(paramTypeElement);
        sb.append(", typeOrder=").append(typeOrder);
        sb.append(", havePagination=").append(havePagination);
        sb.append(", haveSorted=").append(haveSorted);
        sb.append(", nameOfParametersPagination=").append(nameOfParametersPagination);
        sb.append(", nameOfParametersSorted=").append(nameOfParametersSorted);
        sb.append(", whereDescomposed=").append(whereDescomposed);
        sb.append(", lexemas=").append(lexemas);
        sb.append(", worldAndToken=").append(worldAndToken);
        sb.append(", includeTimeFields=").append(includeTimeFields);
        sb.append(", excludeTimeFields=").append(excludeTimeFields);
        sb.append('}');
        return sb.toString();
    }

    
    
    

    public static class Builder {

        private AnnotationType annotationType;
        private ReturnType returnType;
        private String nameOfMethod;
        private CaseSensitive caseSensitive;
        private String where = "";
        private List<String> tokenWhere = new ArrayList<>();
        private List<ParamTypeElement> paramTypeElement;
        private TypeOrder typeOrder;
        private String returnTypeValue;
        private Boolean havePagination = Boolean.FALSE;
        private Boolean haveSorted = Boolean.FALSE;
        private String nameOfParametersPagination;
        private String nameOfParametersSorted;
        private WhereDescomposed whereDescomposed;

        private List<String> lexemas = new ArrayList<>();
        private List<String> worldAndToken = new ArrayList<>();
        
            private List<String> includeTimeFields = new ArrayList<>();
    private List<String> excludeTimeFields = new ArrayList<>();
    
    

        public Builder includeTimeFields(List<String> includeTimeFields) {
            this.includeTimeFields = includeTimeFields;
            return this;
        }
        public Builder excludeTimeFields(List<String> excludeTimeFields) {
            this.excludeTimeFields = excludeTimeFields;
            return this;
        }
        
        public Builder lexemas(List<String> lexemas) {
            this.lexemas = lexemas;
            return this;
        }

        public Builder worldAndToken(List<String> worldAndToken) {
            this.worldAndToken = worldAndToken;
            return this;
        }

        public Builder annotationType(AnnotationType annotationType) {
            this.annotationType = annotationType;
            return this;
        }

        public Builder nameOfParametersPagination(String nameOfParametersPagination) {
            this.nameOfParametersPagination = nameOfParametersPagination;
            return this;
        }

        public Builder nameOfParametersSorted(String nameOfParametersSorted) {
            this.nameOfParametersSorted = nameOfParametersSorted;
            return this;
        }

        public Builder returnType(ReturnType returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder whereDescomposed(WhereDescomposed whereDescomposed) {
            this.whereDescomposed = whereDescomposed;
            return this;
        }

        public Builder havePagination(Boolean havePagination) {
            this.havePagination = havePagination;
            return this;
        }

        public Builder haveSorted(Boolean haveSorted) {
            this.haveSorted = haveSorted;
            return this;
        }

        public Builder returnTypeValue(String returnTypeValue) {
            this.returnTypeValue = returnTypeValue;
            return this;
        }

        public Builder nameOfMethod(String nameOfMethod) {
            this.nameOfMethod = nameOfMethod;
            return this;
        }

        public Builder caseSensitive(CaseSensitive caseSensitive) {
            this.caseSensitive = caseSensitive;
            return this;
        }

        public Builder where(String where) {
            this.where = where;
            return this;
        }

        public Builder tokenWhere(List<String> tokenWhere) {
            this.tokenWhere = tokenWhere;
            return this;
        }

        public Builder paramTypeElement(List<ParamTypeElement> paramTypeElement) {
            this.paramTypeElement = paramTypeElement;
            return this;
        }

        public Builder typeOrder(TypeOrder typeOrder) {
            this.typeOrder = typeOrder;
            return this;
        }

        public RepositoryMethod build() {
            return new RepositoryMethod(annotationType, returnType, returnTypeValue, nameOfMethod, caseSensitive, paramTypeElement, typeOrder, havePagination, haveSorted, nameOfParametersPagination, nameOfParametersSorted, whereDescomposed,lexemas,worldAndToken
            ,includeTimeFields,excludeTimeFields);

        }

    }
}
