/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.util;

import com.jmoordb.core.annotation.Referenced;
import com.jmoordb.core.annotation.enumerations.TypePK;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.bson.Document;

/**
 *
 * @author avbravo
 */
public class JmoordbCoreReferenceUtil {
    // <editor-fold defaultstate="collapsed" desc="String getIdValue(Document document, Referenced referenced)">

    /**
     *
     * @param document
     * @param referenced
     * @return Obtiene el valor del id referenciado
     */
//    public static String getIdValue(Document document, Referenced referenced) {
//        String result = "";
//        try {
//
//            String data = "";
//            if (document.get(referenced.from()) != null) {
//                data = document.get(referenced.from()).toString();
//                data = data.replace("Document{{", "");
//                data = data.replace("}}", "");
//                data = data.replace(referenced.foreignField(), "");
//                data = data.replace("=", "");
//                result = data.trim();
//            } else {
//                // Cuando se pasa desde un List<Document> de llaves primarias
//                data = document.toJson();
//                data = data.replace("{", "");
//                data = data.replace("}", "");
//                data = data.replace("\"", "");
//                data = data.replace(referenced.foreignField(), "");
//                data = data.replace(":", "");
//                result = data.trim();
//
//            }
//        } catch (Exception e) {
//            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
//        }
//        return result;
//    }
//    // </editor-fold>
//    
//    
//    // <editor-fold defaultstate="collapsed" desc="String getListValue(Document document, Referenced referenced)">
//
//    /**
//     *
//     * @param document
//     * @param referenced
//     * @return List<Document> con las referencias de un @Referenced List<>
//     */
//    public static List<Document> getListValue(Document document, Referenced referenced) {
//        List<Document> result = new ArrayList<>();
//        try {
//
//            String data = document.get(referenced.from()).toString();
//            data = data.replace("Document{{", "");
//
//            data = data.replace("}}", "");
//
//            data = data.replace(referenced.foreignField(), "");
//
//            data = data.replace("=", "");
//
//            data = data.replace("[", "");
//
//            data = data.trim();
//            data = data.replace("[", "");
//
//            data = data.replace("]", "");
//
//            data = data.trim();
//            StringTokenizer st = new StringTokenizer(data, ",");
//            while (st.hasMoreTokens()) {
//                Document doc = new Document();
//                if (referenced.typePK().equals(TypePK.STRING)) {
//                    doc.append(referenced.foreignField(), st.nextToken().trim());
//                } else {
//                    Integer value = Integer.parseInt(st.nextToken().trim());
//                    doc.append(referenced.foreignField(), value);
//                }
//                result.add(doc);
//
//            }
//
//        } catch (Exception e) {
//            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
//        }
//        return result;
//    }
//    // </editor-fold>

}
