/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.model;

import static com.jmoordb.core.annotation.app.MyAnnotationTypeProcessor.mirror;
import com.jmoordb.core.annotation.enumerations.AutogeneratedActive;
import com.jmoordb.core.annotation.enumerations.TypePK;
import com.jmoordb.core.annotation.repository.Repository;
import com.jmoordb.core.util.JmoordbCoreFileUtil;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.util.function.Supplier;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

/**
 *
 * @author avbravo
 */
public class RepositoryDataSupplier {
// <editor-fold defaultstate="collapsed" desc="RepositoryData get(Supplier<? extends RepositoryData> s, Element element)">

    public RepositoryData get(Supplier<? extends RepositoryData> s, Element element) {
        RepositoryData repositoryData = s.get();
        try {

          
            
            Repository repository = element.getAnnotation(Repository.class);
            
           
            
            TypeMirror typeEntity = mirror(repository::entity);

/**
 * Lee la entidad para obterner el nombre del campo @Id el tipo y si es autoincrementable
 */
             IdData idData = new IdData();
    JmoordbCoreFileUtil.readIdAnnotationOfEntityFile(element, ProcessorUtil.nameOfEntity(typeEntity)+".java",idData);
              
              /**
               * 
               */
            Boolean isAutogenerated =Boolean.FALSE;
            if(idData.getAutogeneratedActive().equals(AutogeneratedActive.ON)){
                isAutogenerated = Boolean.TRUE;
            }
            
            repositoryData = new RepositoryData.Builder()
                    .nameOfEntity(ProcessorUtil.nameOfEntity(typeEntity))
                    .nameOfEntityLower(JmoordbCoreUtil.letterToLower(ProcessorUtil.nameOfEntity(typeEntity)))
                    .nameOfPackage(ProcessorUtil.packageOfTypeMirror(typeEntity))
                    .fieldPK(idData.getFieldName())
                    .autogeneratedActive(idData.getAutogeneratedActive())
                    .typePK(idData.getFieldType().equals("String")?TypePK.STRING :TypePK.LONG)
                    .typeParameter(idData.getFieldType())
                    .packageOfRepository(ProcessorUtil.getPackageName(element))
                    .interfaceName(ProcessorUtil.getTypeName(element))
                    .isAutogenerated(isAutogenerated)
                    .build();
            


        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return repositoryData;
    }
    // </editor-fold>


    
    
}