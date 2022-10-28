/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.model;

import com.jmoordb.core.annotation.Entity;
import com.jmoordb.core.annotation.Id;

/**
 *
 * @author avbravo idautoincrementable = database_collection
 */
@Entity
public class Autosequence {

    @Id
    private String databasecollection;
    private Long secuence;

    public Autosequence() {
    }

    public Autosequence(String databasecollection, Long secuence) {
        this.databasecollection = databasecollection;
        this.secuence = secuence;
    }

    public String getDatabasecollection() {
        return databasecollection;
    }

    public void setDatabasecollection(String databasecollection) {
        this.databasecollection = databasecollection;
    }

    public Long getSecuence() {
        return secuence;
    }

    public void setSecuence(Long secuence) {
        this.secuence = secuence;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Autosequence{");
        sb.append("databasecollection=").append(databasecollection);
        sb.append(", secuence=").append(secuence);
        sb.append('}');
        return sb.toString();
    }
 
    public String toJson(Autosequence autosecuence) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("databasecollection:").append("").append(databasecollection);
        sb.append(", secuence:").append("").append(secuence);
        sb.append('}');
        return sb.toString();
    }

  

    
    

}
