package de.eome.guide.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.eome.guide.json.util.JsonUtil;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JSON wrapper implementation for an option of a user request.
 */
public class Option implements de.eome.guide.api.Option, UpdateListener {
    private final ObjectNode json;
    private final Map<String, String> content;
    private UserRequest parent;
    
    public Option() {
        json = JsonNodeFactory.instance.objectNode();
        content = new LinkedHashMap<>();
    }
    
    public Option(ObjectNode json) throws IllegalArgumentException {
        validate(json);
        this.json = json;
        this.content = new LinkedHashMap<>();
        if (json.path("content").isObject()) {
            ObjectNode contentObject = (ObjectNode) json.path("content");
            Iterator<Map.Entry<String, JsonNode>> fieldIterator = contentObject.fields();
            while (fieldIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldIterator.next();
                content.put(field.getKey(), field.getValue().asText());
            }
        }
    }
    
    public void setParent(UserRequest userRequest) {
        parent = userRequest;
    }
    
    public UserRequest getParent() {
        return parent;
    }
    
    @Override
    public void updatePerformed() {
        if (parent != null) parent.updatePerformed();
    }
    
    private static void validate(ObjectNode json) throws IllegalArgumentException {
        JsonUtil.validateStringMap(json.path("content"), "content", true);
        JsonUtil.validateTextNode(json.path("next"), "next", true);
    }

    @Override
    public String getContentId(String languageId) {
        return content.get(languageId);
    }

    @Override
    public Map<String, String> getContentIds() {
        return Collections.unmodifiableMap(content);
    }

    @Override
    public void setContentId(String languageId, String contentId) {
        content.put(languageId, contentId);
        JsonUtil.getOrCreateObject(json, "content").set(languageId, JsonNodeFactory.instance.textNode(contentId));
        updatePerformed();
    }

    @Override
    public void removeContentId(String languageId) {
        if (!content.containsKey(languageId)) return;
        content.remove(languageId);
        JsonUtil.getOrCreateObject(json, "content").remove(languageId);
        updatePerformed();
    }

    @Override
    public void setNext(String stepId) {
        json.put("next", stepId);
        updatePerformed();
    }

    @Override
    public void removeNext() {
        json.remove("next");
        updatePerformed();
    }

    @Override
    public String getNext() {
        return json.path("next").asText(null);
    }
    
    public ObjectNode asJson() {
        return json;
    }
}
