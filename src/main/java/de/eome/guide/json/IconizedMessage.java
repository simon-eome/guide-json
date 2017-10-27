package de.eome.guide.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.eome.guide.json.util.JsonUtil;
import java.util.Objects;

public class IconizedMessage implements de.eome.guide.api.IconizedMessage, UpdateListener {
    private final ObjectNode json;
    private Content parent;
    
    public IconizedMessage(String text) {
        json = JsonNodeFactory.instance.objectNode();
        json.put("text", text);
    }
    
    public IconizedMessage(ObjectNode json) throws IllegalArgumentException {
        validate(json);
        this.json = json;
    }
    
    private static void validate(ObjectNode json) throws IllegalArgumentException {
        JsonUtil.validateTextNode(json.path("text"), "text", false);
        JsonUtil.validateTextNode(json.path("icon"), "icon", true);
    }
    
    public void setParent(Content parent) {
        this.parent = parent;
    }
    
    public Content getParent() {
        return parent;
    }
    
    @Override
    public void updatePerformed() {
        if (parent != null) parent.updatePerformed();
    }

    @Override
    public void setText(String text) {
        json.put("text", text);
        updatePerformed();
    }

    @Override
    public String getText() {
        return json.path("text").asText(null);
    }

    @Override
    public void setIcon(String path) {
        json.put("icon", path);
        updatePerformed();
    }

    @Override
    public String getIcon() {
        return json.path("icon").asText(null);
    }
    
    @Override
    public String toString() {
        return json.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        return other != null ? toString().equals(other.toString()) : false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(getText());
        hash = 53 * hash + Objects.hashCode(getIcon());
        return hash;
    }
    
    /**
     * Returns the JSON wrapped by this object.
     * @return JSON representing the iconized message.
     */
    public ObjectNode asJson() {
        return json;
    }
}
