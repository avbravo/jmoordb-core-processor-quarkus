package com.jmoordb.core.annotation.autosecuence;

import com.jmoordb.core.annotation.repository.*;
import com.jmoordb.core.annotation.enumerations.JakartaSource;
import com.jmoordb.core.annotation.enumerations.TypeReferenced;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutosecuenceRepository {
    Class<?> entity();
    JakartaSource jakartaSource() default JakartaSource.JAKARTA;
    String collection() default "autosecuence";
    /**
     * database_name --> Es un nombre de base de datos que indique el
     * desarrollador {mongodb.database} --> Es el parametro en
     * Microorofile-config.properties
     *
     * @return
     */
    String database() default "{mongodb.jmoordb}";

}
