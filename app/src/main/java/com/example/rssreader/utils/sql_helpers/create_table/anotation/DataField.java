package com.example.rssreader.utils.sql_helpers.create_table.anotation;

import com.example.rssreader.utils.sql_helpers.create_table.entity.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataField {

    /**
     * The primary key of this table
     */
    boolean isPrimaryKey() default false;

    /**
     * Name of the column in the database.
     */
    String name() default "";

    /**
     * Column type. At early stages only two types are supported: Integer and Text
     */
    Type type();

    /**
     * This value works in conjunction with the annotation: IntegerDefault or StringDefault.
     * When the value of this annotation is true, will be taken from the value of the annotations ...Default and set as the default value
     */
    boolean isSetDefault() default false;

    /**
     * The ability of the column to take null values. The default is true
     */
    boolean isNull() default true;

}
