/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.model;

import com.jmoordb.core.annotation.DateSupport;
import com.jmoordb.core.util.JmoordbCoreUtil;
import com.jmoordb.core.util.MessagesUtil;
import com.jmoordb.core.util.ProcessorUtil;
import java.util.function.Supplier;
import javax.lang.model.element.Element;

/**I
 *
 * @author avbravo
 */
public class DateSupportDataSupplier {
// <editor-fold defaultstate="collapsed" desc="DateSupportData get(Supplier<? extends DateSupportData> s, Element element)">

    public DateSupportData get(Supplier<? extends DateSupportData> s, Element element) {
        DateSupportData dateSupportData = s.get();
        try {

            DateSupport dateSupport = element.getAnnotation(DateSupport.class);
    
           
          
            dateSupportData = new DateSupportData.Builder()
                    .jakartaSource(dateSupport.jakartaSource())
                    .packageOfDateSupport(ProcessorUtil.getPackageName(element))
                     .dateSupportName(ProcessorUtil.getTypeName(element))
                    .build();
            
            

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return dateSupportData;
    }
    // </editor-fold>

}
