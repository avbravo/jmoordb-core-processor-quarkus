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
public class DocumentEmbeddableData {

    private String collection;
    private String database;
    private JakartaSource jakartaSource;
    private String packageOfDocumentEmbeddable;
    private String documentEmbeddableName;

    public DocumentEmbeddableData() {
    }

    public DocumentEmbeddableData(String collection, String database, JakartaSource jakartaSource, String packageOfDocumentEmbeddable, String documentEmbeddableName) {
        this.collection = collection;
        this.database = database;
        this.jakartaSource = jakartaSource;
        this.packageOfDocumentEmbeddable = packageOfDocumentEmbeddable;
        this.documentEmbeddableName = documentEmbeddableName;
    }

    public JakartaSource getJakartaSource() {
        return jakartaSource;
    }

    public void setJakartaSource(JakartaSource jakartaSource) {
        this.jakartaSource = jakartaSource;
    }

    public String getDocumentEmbeddableName() {
        return documentEmbeddableName;
    }

    public void setDocumentEmbeddableName(String documentEmbeddableName) {
        this.documentEmbeddableName = documentEmbeddableName;
    }

    public String getPackageOfDocumentEmbeddable() {
        return packageOfDocumentEmbeddable;
    }

    public void setPackageOfDocumentEmbeddable(String packageOfDocumentEmbeddable) {
        this.packageOfDocumentEmbeddable = packageOfDocumentEmbeddable;
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
        sb.append("DocumentEmbeddableData{");
        sb.append("\n\tcollection=").append(collection);
        sb.append("\n\t, database=").append(database);
        sb.append("\n\t, jakartaSource=").append(jakartaSource);
        sb.append("\n\t, packageOfDocumentEmbeddable=").append(packageOfDocumentEmbeddable);
        sb.append("\n\t, documentEmbeddableName=").append(documentEmbeddableName).append("\n");
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {

        private String collection;
        private String database;
        private String packageOfDocumentEmbeddable;
        private String documentEmbeddableName;
        JakartaSource jakartaSource;

        public Builder jakartaSource(JakartaSource jakartaSource) {
            this.jakartaSource = jakartaSource;
            return this;
        }

        public Builder collection(String collection) {
            this.collection = collection;
            return this;
        }

        public Builder documentEmbeddableName(String documentEmbeddableName) {
            this.documentEmbeddableName = documentEmbeddableName;
            return this;
        }

        public Builder packageOfDocumentEmbeddable(String packageOfDocumentEmbeddable) {
            this.packageOfDocumentEmbeddable = packageOfDocumentEmbeddable;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public DocumentEmbeddableData build() {
            return new DocumentEmbeddableData(collection, database, jakartaSource, packageOfDocumentEmbeddable, documentEmbeddableName);

        }
    }
}
