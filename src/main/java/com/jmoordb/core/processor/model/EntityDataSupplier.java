/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.model;

import com.jmoordb.core.annotation.Entity;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.util.function.Supplier;
import javax.lang.model.element.Element;

/**I
 *
 * @author avbravo
 */
public class EntityDataSupplier {
// <editor-fold defaultstate="collapsed" desc="EntityData get(Supplier<? extends EntityData> s, Element element)">

    public EntityData get(Supplier<? extends EntityData> s, Element element) {
        EntityData entityData = s.get();
        try {

            Entity entity = element.getAnnotation(Entity.class);
    
            String database=   "{mongodb.database}";    
            if (entity.database() == null  || entity.database().equals("")) {
                database = "{mongodb.database}";
          
            } else {
     
                database = entity.database().replace("{", "").replace("}", "");
            }
            String collection ="";
    
           if (entity.collection() == null ||  entity.collection().trim().equals("")){
                    collection =JmoordbCoreUtil.letterToLower(ProcessorUtil.getTypeName(element));
                
            }else{
               collection = entity.collection();
           }
          
            entityData = new EntityData.Builder()
                    .collection(collection)
                    .jakartaSource(entity.jakartaSource())
                    .database(database)                    
                    .packageOfEntity(ProcessorUtil.getPackageName(element))
                     .entityName(ProcessorUtil.getTypeName(element))
                    .build();
            
            

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return entityData;
    }
    // </editor-fold>

}
