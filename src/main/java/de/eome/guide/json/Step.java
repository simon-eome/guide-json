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
 * JSON implementation for the step model.
 */
public abstract class Step implements de.eome.guide.api.Step, UpdateListener {
    protected final ObjectNode json;
    private final Map<String, String> content;
    private Guide parent;
    
    public Step(String id) {
        json = JsonNodeFactory.instance.objectNode();
        json.put("id", id);
        json.put("type", getType());
        content = new LinkedHashMap<>();
    }
    
    public void setParent(Guide guide) {
        this.parent = guide;
    }
    
    public Guide getParent() {
        return parent;
    }
    
    @Override
    public void updatePerformed() {
        if (parent != null) parent.updatePerformed();
    }
    
    protected Step(ObjectNode json) throws IllegalArgumentException {
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
    
    private static void validate(ObjectNode json) throws IllegalArgumentException {
        JsonUtil.validateTextNode(json.path("id"), "id", false);
        JsonUtil.validateTextNode(json.path("type"), "type", false);
        JsonUtil.validateStringMap(json.path("content"), "content", true);
        JsonUtil.validateTextNode(json.path("next"), "next", true);
    }
    
    public static Step fromJson(ObjectNode stepNode) throws IllegalArgumentException {
        if (stepNode.path("type").isMissingNode()) {
            throw new IllegalArgumentException("Missing or invalid field [type], string expected.");
        }
        Step step;
        String type = stepNode.path("type").asText("<unknown>");
        switch (type) {
            case "action":
                step = new Action(stepNode);
                break;
            case "milestone":
                step = new Milestone(stepNode);
                break;
            case "chapter":
                step = new Chapter(stepNode);
                break;
            case "branch":
                JsonNode userRequestNode = stepNode.get("userRequest");
                if (userRequestNode.isMissingNode() || !userRequestNode.isObject()) {
                    throw new IllegalArgumentException("Wrong or missing userReques field, expects object.");
                }
               step = new UserRequest(stepNode);
                break;
                
            default:
                throw new IllegalArgumentException("Invalid step type: " + type);
        }
        return step;
    }
    
    @Override
    public String getId() {
        return json.path("id").asText(null);
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
        if (content.isEmpty()) {
            json.remove("content");
        }
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
    
    /**
     * Returns the json object wrapped by this object.
     * @return Json object representing a step.
     */
    public ObjectNode asJson() {
        return json;
    }
}
