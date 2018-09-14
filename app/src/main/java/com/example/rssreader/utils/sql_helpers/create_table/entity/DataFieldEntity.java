package com.example.rssreader.utils.sql_helpers.create_table.entity;


public class DataFieldEntity {

    private final String name;

    private final Type type;

    private boolean isPrimaryKey;

    private final boolean nullable;

    private final String defaultVal;

    public DataFieldEntity(String name, Type type, boolean isPrimaryKey, boolean nullable, String defaultVal) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.defaultVal = defaultVal == null ? "" : defaultVal;
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(name)
                .append(" ")
                .append(type)
                .append(" ")
                .append(isPrimaryKey ? " PRIMARY KEY " : "")
                .append(defaultVal.isEmpty() ? "" : " DEFAULT ".concat(defaultVal))
                .append(nullable ? "" : "NOT NULL ");


        return stringBuilder.toString();
    }
}
