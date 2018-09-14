package com.example.rssreader.utils.sql_helpers.create_table.parser;


import com.example.rssreader.utils.sql_helpers.create_table.anotation.DataField;
import com.example.rssreader.utils.sql_helpers.create_table.anotation.DataObject;
import com.example.rssreader.utils.sql_helpers.create_table.anotation.DataTable;
import com.example.rssreader.utils.sql_helpers.create_table.anotation.IntegerDefault;
import com.example.rssreader.utils.sql_helpers.create_table.anotation.StringDefault;
import com.example.rssreader.utils.sql_helpers.create_table.entity.DataFieldEntity;
import com.example.rssreader.utils.sql_helpers.create_table.entity.Type;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableParser {

    private final static String beginFieldEnumeration = " ( ";
    private final static String endFieldEnumeration = " ); ";

    /**
     * @param aClass class to be query created
     * @return SQLite create table query string
     */
    public static String generateCreateQuery(Class<? extends DataObject> aClass){
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TABLE ");
        String tableName = aClass.getAnnotation(DataTable.class).tableName();
        if (tableName.isEmpty()){
            tableName = aClass.getSimpleName();
        }
        queryBuilder.append(tableName);

        Field[] declaredFields = aClass.getDeclaredFields();

        List<DataFieldEntity> parsedEntity = parseFields(declaredFields);
        queryBuilder.append(beginFieldEnumeration);

        for (int i = 0; i < parsedEntity.size(); i++){
            DataFieldEntity entity = parsedEntity.get(i);
            queryBuilder.append(entity.toString());
            if (i != parsedEntity.size() - 1){
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(endFieldEnumeration);
        return queryBuilder.toString();
    }

    private static List<DataFieldEntity> parseFields(Field[] declaredFields){
        List<DataFieldEntity> result = new ArrayList<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(DataField.class)){
                DataField dataFiledAnnotation = declaredField.getAnnotation(DataField.class);
                String name = dataFiledAnnotation.name();
                Type type = dataFiledAnnotation.type();
                String defaultValue = "";
                boolean nullable = dataFiledAnnotation.isNull();
                boolean isPrimaryKey = dataFiledAnnotation.isPrimaryKey();

                if (dataFiledAnnotation.isSetDefault()) {
                    if (declaredField.isAnnotationPresent(IntegerDefault.class)){
                        defaultValue = String.valueOf(declaredField.getAnnotation(IntegerDefault.class).value());
                    }else if(declaredField.isAnnotationPresent(StringDefault.class)){
                        defaultValue = declaredField.getAnnotation(StringDefault.class).value();
                    }
                }
                if (name.isEmpty()){
                    name = declaredField.getName();
                }
                result.add(new DataFieldEntity(name, type, isPrimaryKey, nullable, defaultValue));
            }
        }
        return result;
    }

}
