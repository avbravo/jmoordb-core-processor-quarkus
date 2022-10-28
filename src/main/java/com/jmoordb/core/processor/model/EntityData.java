/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.model;

import com.jmoordb.core.annotation.enumerations.JakartaSource;

/**
 *
 * @author avbravo Se utiliza para gestionar los datos leidos del repositorio
 */
public class EntityData {

    private String collection;
    private String database;
    private JakartaSource jakartaSource;
    private String packageOfEntity;
    private String entityName;

    public EntityData() {
    }

    public EntityData(String collection, String database, JakartaSource jakartaSource, String packageOfEntity, String entityName) {
        this.collection = collection;
        this.database = database;
        this.jakartaSource = jakartaSource;
        this.packageOfEntity = packageOfEntity;
        this.entityName = entityName;
    }

    public JakartaSource getJakartaSource() {
        return jakartaSource;
    }

    public void setJakartaSource(JakartaSource jakartaSource) {
        this.jakartaSource = jakartaSource;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getPackageOfEntity() {
        return packageOfEntity;
    }

    public void setPackageOfEntity(String packageOfEntity) {
        this.packageOfEntity = packageOfEntity;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EntityData{");
        sb.append("\n\tcollection=").append(collection);
        sb.append("\n\t, database=").append(database);
        sb.append("\n\t, jakartaSource=").append(jakartaSource);
        sb.append("\n\t, packageOfEntity=").append(packageOfEntity);
        sb.append("\n\t, entityName=").append(entityName).append("\n");
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {

        private String collection;
        private String database;
        private String packageOfEntity;
        private String entityName;
        JakartaSource jakartaSource;

        public Builder jakartaSource(JakartaSource jakartaSource) {
            this.jakartaSource = jakartaSource;
            return this;
        }

        public Builder collection(String collection) {
            this.collection = collection;
            return this;
        }

        public Builder entityName(String entityName) {
            this.entityName = entityName;
            return this;
        }

        public Builder packageOfEntity(String packageOfEntity) {
            this.packageOfEntity = packageOfEntity;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public EntityData build() {
            return new EntityData(collection, database, jakartaSource, packageOfEntity, entityName);

        }
    }
}
