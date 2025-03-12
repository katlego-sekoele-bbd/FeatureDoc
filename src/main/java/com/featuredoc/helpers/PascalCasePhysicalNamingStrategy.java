package com.featuredoc.helpers;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PascalCasePhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return applyPascalCase(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return applyPascalCase(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return applyPascalCase(name);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return applyPascalCase(name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return applyPascalCase(name);
    }

    private Identifier applyPascalCase(Identifier name) {
        if (name == null) {
            return null;
        }
        String text = name.getText();
        String pascalCaseText = convertToPascalCase(text);
        return Identifier.toIdentifier(pascalCaseText);
    }

    private String convertToPascalCase(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }
        String camelCaseText = Character.toUpperCase(text.charAt(0)) + text.substring(1);
        String quotes = "\"%s\"";
        return String.format(quotes, camelCaseText);
    }
}

