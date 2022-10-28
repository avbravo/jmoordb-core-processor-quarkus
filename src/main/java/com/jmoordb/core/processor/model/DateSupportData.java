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
public class DateSupportData {


    private JakartaSource jakartaSource;
    private String packageOfDateSupport;
    private String dateSupportName;

    public DateSupportData() {
    }

    public DateSupportData(JakartaSource jakartaSource, String packageOfDateSupport, String dateSupportName) {
       
        this.jakartaSource = jakartaSource;
        this.packageOfDateSupport = packageOfDateSupport;
        this.dateSupportName = dateSupportName;
    }

    public JakartaSource getJakartaSource() {
        return jakartaSource;
    }

    public void setJakartaSource(JakartaSource jakartaSource) {
        this.jakartaSource = jakartaSource;
    }

    public String getDateSupportName() {
        return dateSupportName;
    }

    public void setDateSupportName(String dateSupportName) {
        this.dateSupportName = dateSupportName;
    }

    public String getPackageOfDateSupport() {
        return packageOfDateSupport;
    }

    public void setPackageOfDateSupport(String packageOfDateSupport) {
        this.packageOfDateSupport = packageOfDateSupport;
    }

   

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DateSupportData{");

        sb.append("\n\t jakartaSource=").append(jakartaSource);
        sb.append("\n\t, packageOfDateSupport=").append(packageOfDateSupport);
        sb.append("\n\t, dateSupportName=").append(dateSupportName).append("\n");
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {


        private String packageOfDateSupport;
        private String dateSupportName;
        JakartaSource jakartaSource;

        public Builder jakartaSource(JakartaSource jakartaSource) {
            this.jakartaSource = jakartaSource;
            return this;
        }

     
        public Builder dateSupportName(String dateSupportName) {
            this.dateSupportName = dateSupportName;
            return this;
        }

        public Builder packageOfDateSupport(String packageOfDateSupport) {
            this.packageOfDateSupport = packageOfDateSupport;
            return this;
        }

      

        public DateSupportData build() {
            return new DateSupportData(jakartaSource, packageOfDateSupport, dateSupportName);

        }
    }
}
