/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.model;

import com.jmoordb.core.annotation.enumerations.AutogeneratedActive;
import com.jmoordb.core.annotation.enumerations.JakartaSource;

/**
 *
 * @author avbravo
 */
public class IdData {

    String value;
    AutogeneratedActive autogeneratedActive;
    String fieldType;
    String fieldName;
    String collection;
    String database;
    JakartaSource jakartaSource;

    public IdData() {
    }

    public IdData(String value, AutogeneratedActive autogeneratedActive, String fieldType, String fieldName, String collection, String database, JakartaSource jakartaSource) {
        this.value = value;
        this.autogeneratedActive = autogeneratedActive;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.collection = collection;
        this.database = database;
        this.jakartaSource = jakartaSource;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AutogeneratedActive getAutogeneratedActive() {
        return autogeneratedActive;
    }

    public void setAutogeneratedActive(AutogeneratedActive autogeneratedActive) {
        this.autogeneratedActive = autogeneratedActive;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public JakartaSource getJakartaSource() {
        return jakartaSource;
    }

    public void setJakartaSource(JakartaSource jakartaSource) {
        this.jakartaSource = jakartaSource;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IdData{");
        sb.append("\nvalue=").append(value);
        sb.append("\n, autogeneratedActive=").append(autogeneratedActive);
        sb.append("\n, fieldType=").append(fieldType);
        sb.append("\n, fieldName=").append(fieldName);
        sb.append("\n, collection=").append(collection);
        sb.append("\n, database=").append(database);
        sb.append("\n, jakartaSource=").append(jakartaSource).append("\n");
        sb.append('}');
        return sb.toString();
    }




    public static class Builder {

        String value;
        AutogeneratedActive autogeneratedActive;
        String fieldType;
        String fieldName;
        String collection;
        String database;
        JakartaSource jakartaSource;

        public Builder collection(String collection) {
            this.collection = collection;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder jakartaSource(JakartaSource jakartaSource) {
            this.jakartaSource = jakartaSource;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder fieldType(String fieldType) {
            this.fieldType = fieldType;
            return this;
        }

        public Builder fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder autogeneratedActive(AutogeneratedActive autogeneratedActive) {
            this.autogeneratedActive = autogeneratedActive;
            return this;
        }

        public IdData build() {
            return new IdData(value, autogeneratedActive, fieldType, fieldName, collection, database, jakartaSource);

        }
    }

}
