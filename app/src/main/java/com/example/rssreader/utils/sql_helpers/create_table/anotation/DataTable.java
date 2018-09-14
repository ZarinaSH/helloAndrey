package com.example.rssreader.utils.sql_helpers.create_table.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTable {
    /**
     *
     * @return The table name in the SQLite database.
     */
    String tableName() default "";

}
