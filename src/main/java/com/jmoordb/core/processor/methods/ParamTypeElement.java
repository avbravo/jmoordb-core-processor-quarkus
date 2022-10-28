/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.jmoordb.core.processor.methods;

import com.jmoordb.core.annotation.enumerations.ParamType;

/**
 *
 * @author avbravo
 * Se utiliza para almacenar los parametros de los métodos
 */
public class ParamTypeElement {


    private String type;
    private String name;
    private Boolean isIncludeTime ;
    private Boolean isExcludeTime;

    public ParamTypeElement() {
    }

    public ParamTypeElement( String type, String name, Boolean isIncludeTime, Boolean isExcludeTime) {

        this.type = type;
        this.name = name;
        this.isIncludeTime = isExcludeTime;
        this.isExcludeTime = isExcludeTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsIncludeTime() {
        return isIncludeTime;
    }

    public void setIsIncludeTime(Boolean isIncludeTime) {
        this.isIncludeTime = isIncludeTime;
    }

    public Boolean getIsExcludeTime() {
        return isExcludeTime;
    }

    public void setIsExcludeTime(Boolean isExcludeTime) {
        this.isExcludeTime = isExcludeTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParamTypeElement{");
        sb.append("type=").append(type);
        sb.append(", name=").append(name);
        sb.append(", isIncludeTime=").append(isIncludeTime);
        sb.append(", isExcludeTime=").append(isExcludeTime);
        sb.append('}');
        return sb.toString();
    }

    
    
    
   
  

    
    
    
    
    public static class Builder {


        private String name;
        private String type;
        
      private Boolean isIncludeTime ;
    private Boolean isExcludeTime;
        
        public Builder isIncludeTime(Boolean isIncludeTime){
            this.isIncludeTime= isIncludeTime;
            return this;
        }
        public Builder isExcludeTime(Boolean isExcludeTime){
            this.isExcludeTime= isExcludeTime;
            return this;
        }
        public Builder name(String name){
            this.name= name;
            return this;
        }
        public Builder type(String type){
            this.type= type;
            return this;
        }
        
        public  ParamTypeElement build(){
            return new ParamTypeElement( type, name,isIncludeTime, isExcludeTime);
        }

    }
}
