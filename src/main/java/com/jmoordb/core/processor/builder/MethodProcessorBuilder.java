package com.jmoordb.core.processor.builder;

import java.util.SortedMap;

public class MethodProcessorBuilder {
    private StringBuilder builder = new StringBuilder();
    private boolean firstParam = true;
    private boolean forInterface;

    public MethodProcessorBuilder forInterface() {
        forInterface = true;
        return this;
    }

    public MethodProcessorBuilder defineSignature(String accessModifier, boolean asStatic, String returnType) {
        builder.append(forInterface ? "" : accessModifier)
                .append(asStatic? " static ": " ")
                .append(returnType)
                .append(" ");
        return this;
    }

    public MethodProcessorBuilder name(String name) {
        builder.append(name)
                .append("(");
        return this;
    }

    public MethodProcessorBuilder addParam(String type, String identifier) {
        if (!firstParam) {
            builder.append(", ");
        } else {
            firstParam = false;
        }
        builder.append(type)
                .append(" ")
                .append(identifier);

        return this;
    }

    public MethodProcessorBuilder defineBody(String body) {
        if (forInterface) {
            throw new IllegalArgumentException("Interface cannot define a body");
        }
        builder.append(") {")
                .append(RepositorySourceBuilder.LINE_BREAK)
                .append(body)
                .append(RepositorySourceBuilder.LINE_BREAK)
                .append("}")
                .append(RepositorySourceBuilder.LINE_BREAK);
        return this;
    }

    public String end() {
        return forInterface ? ");" : builder.toString();
    }
}