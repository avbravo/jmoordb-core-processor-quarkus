/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.methods;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avbravo
 */
public class WhereDescomposed {
         List<String> fieldList = new ArrayList<>();
            List<String> paramList = new ArrayList<>();
            List<String> comparatorList = new ArrayList<>();
            List<String> logicalList = new ArrayList<>();

    public WhereDescomposed() {
    }

            
            
            
    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public void setParamList(List<String> paramList) {
        this.paramList = paramList;
    }

    public List<String> getComparatorList() {
        return comparatorList;
    }

    public void setComparatorList(List<String> comparatorList) {
        this.comparatorList = comparatorList;
    }

    public List<String> getLogicalList() {
        return logicalList;
    }

    public void setLogicalList(List<String> logicalList) {
        this.logicalList = logicalList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WhereDescomposed{");
        sb.append("fieldList=").append(fieldList);
        sb.append(", paramList=").append(paramList);
        sb.append(", comparatorList=").append(comparatorList);
        sb.append(", logicalList=").append(logicalList);
        sb.append('}');
        return sb.toString();
    }
            
   public String logical(Integer pos)         {
      return logicalList.get(pos);
   }
   public String comparator(Integer pos)         {
      return comparatorList.get(pos);
   }
   public String field(Integer pos)         {
      return fieldList.get(pos);
   }
            
   public String param(Integer pos)         {
      return paramList.get(pos);
   }
            
}
