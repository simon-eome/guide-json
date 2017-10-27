/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.eome.guide.json;

import java.util.Objects;

/**
 * Tag model for JSON implementation of tag model.
 */
public class Tag implements de.eome.guide.api.Tag {
    private final Type type;
    private final String value;
    
    public Tag(Type type, String value) {
        this.type = type;
        this.value = value;
    }
    
    public static Tag ofString(String tagString) throws IllegalArgumentException {
        if (tagString == null || tagString.isEmpty()) {
            throw new IllegalArgumentException("Tag may not be empty or null.");
        }
        Type type;
        switch (tagString.substring(0, 1)) {
            case "@":
                type = Type.TYPED;
                break;
            case "#":
                type = Type.CUSTOM;
                break;
            default:
                throw new IllegalArgumentException("Invalid type marker, @ or # expected.");
        }
        return new Tag(type, tagString.substring(1));
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        switch (type) {
            case CUSTOM:
                builder.append("#");
                break;
            case TYPED:
                builder.append("@");
                break;
        }
        builder.append(value);
        return builder.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        return o != null ? toString().equals(o.toString()) : false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.type);
        hash = 47 * hash + Objects.hashCode(this.value);
        return hash;
    }
}
