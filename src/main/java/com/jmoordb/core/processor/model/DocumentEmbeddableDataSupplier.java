/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.model;

import com.jmoordb.core.annotation.DocumentEmbeddable;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.util.function.Supplier;
import javax.lang.model.element.Element;

/**I
 *
 * @author avbravo
 */
public class DocumentEmbeddableDataSupplier {
// <editor-fold defaultstate="collapsed" desc="DocumentEmbeddableData get(Supplier<? extends DocumentEmbeddableData> s, Element element)">

    public DocumentEmbeddableData get(Supplier<? extends DocumentEmbeddableData> s, Element element) {
        DocumentEmbeddableData documentEmbeddableData = s.get();
        try {

            DocumentEmbeddable documentEmbeddable = element.getAnnotation(DocumentEmbeddable.class);
    
            String database=   "{mongodb.database}";    
            if (documentEmbeddable.database() == null  || documentEmbeddable.database().equals("")) {
                database = "{mongodb.database}";
          
            } else {
     
                database = documentEmbeddable.database().replace("{", "").replace("}", "");
            }
            String collection ="";
    
           if (documentEmbeddable.collection() == null ||  documentEmbeddable.collection().trim().equals("")){
                    collection =JmoordbCoreUtil.letterToLower(ProcessorUtil.getTypeName(element));
                
            }else{
               collection = documentEmbeddable.collection();
           }
          
            documentEmbeddableData = new DocumentEmbeddableData.Builder()
                    .collection(collection)
                    .jakartaSource(documentEmbeddable.jakartaSource())
                    .database(database)                    
                    .packageOfDocumentEmbeddable(ProcessorUtil.getPackageName(element))
                     .documentEmbeddableName(ProcessorUtil.getTypeName(element))
                    .build();
            
            

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return documentEmbeddableData;
    }
    // </editor-fold>

}
