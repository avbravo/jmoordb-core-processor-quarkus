/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.methods;

import com.jmoordb.core.annotation.Column;
import com.jmoordb.core.annotation.Embedded;
import com.jmoordb.core.annotation.Id;
import com.jmoordb.core.annotation.Referenced;
import com.jmoordb.core.annotation.enumerations.AnnotationType;
import com.jmoordb.core.annotation.enumerations.ReturnType;
import com.jmoordb.core.annotation.enumerations.TypeReferenced;
import java.util.List;

/**
 *
 * @author avbravo
 */
public class DateSupportField {

    private String returnTypeValue;
    private String nameOfMethod;
    private AnnotationType annotationType;
    private ReturnType returnType;
    private Id id;
    private Column column;
    private Embedded embedded;
    private Referenced referenced;
    private TypeReferenced typeReferenced;

    public DateSupportField(String returnTypeValue, String nameOfMethod, AnnotationType annotationType, ReturnType returnType, Id id, Column column, Embedded embedded, Referenced referenced, TypeReferenced typeReferenced) {
        this.returnTypeValue = returnTypeValue;
        this.nameOfMethod = nameOfMethod;
        this.annotationType = annotationType;
        this.returnType = returnType;
        this.id = id;
        this.column = column;
        this.embedded = embedded;
        this.referenced = referenced;
        this.typeReferenced = typeReferenced;
    }

    public TypeReferenced getTypeReferenced() {
        return typeReferenced;
    }

    public void setTypeReferenced(TypeReferenced typeReferenced) {
        this.typeReferenced = typeReferenced;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public Referenced getReferenced() {
        return referenced;
    }

    public void setReferenced(Referenced referenced) {
        this.referenced = referenced;
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(AnnotationType annotationType) {
        this.annotationType = annotationType;
    }

    public String getReturnTypeValue() {
        return returnTypeValue;
    }

    public void setReturnTypeValue(String returnTypeValue) {
        this.returnTypeValue = returnTypeValue;
    }

    public String getNameOfMethod() {
        return nameOfMethod;
    }

    public void setNameOfMethod(String nameOfMethod) {
        this.nameOfMethod = nameOfMethod;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EntityField{");
        sb.append("\n\treturnTypeValue=").append(returnTypeValue);
        sb.append("\n\t, nameOfMethod=").append(nameOfMethod);
        sb.append("\n\t, annotationType=").append(annotationType);
        sb.append("\n\t, returnType=").append(returnType);
        sb.append("\n\t, id=").append(id);
        sb.append("\n\t, column=").append(column);
        sb.append("\n\t, embedded=").append(embedded);
        sb.append("\n\t, referenced=").append(referenced);
        sb.append("\n\t, typeReferenced=").append(typeReferenced);
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {

        private String returnTypeValue;
        private String nameOfMethod;
        private AnnotationType annotationType;
        private ReturnType returnType;

        private Id id;
        private Column column;
        private Embedded embedded;
        private Referenced referenced;
        private TypeReferenced typeReferenced;

        public Builder id(Id id) {
            this.id = id;
            return this;
        }
        public Builder typeReferenced(TypeReferenced typeReferenced) {
            this.typeReferenced= typeReferenced;
            return this;
        }

        public Builder column(Column column) {
            this.column = column;
            return this;
        }

        public Builder embedded(Embedded embedded) {
            this.embedded = embedded;
            return this;
        }

        public Builder referenced(Referenced referenced) {
            this.referenced = referenced;
            return this;
        }

        public Builder returnTypeValue(String returnTypeValue) {
            this.returnTypeValue = returnTypeValue;
            return this;
        }

        public Builder returnType(ReturnType returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder nameOfMethod(String nameOfMethod) {
            this.nameOfMethod = nameOfMethod;
            return this;
        }

        public Builder annotationType(AnnotationType annotationType) {
            this.annotationType = annotationType;
            return this;
        }

        public DateSupportField build() {
            return new DateSupportField(returnTypeValue, nameOfMethod, annotationType, returnType, id, column, embedded, referenced, typeReferenced);

        }

    }
}
