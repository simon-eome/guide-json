package de.eome.guide.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.eome.guide.json.util.JsonUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON wrapper implementation for user requests.
 */
public class UserRequest extends Branch implements de.eome.guide.api.UserRequest {
    private final ObjectNode userRequestNode;
    private final Map<String, String> content;
    private final List<Option> options;
    
    public UserRequest(String id) {
        super(id);
        userRequestNode = JsonNodeFactory.instance.objectNode();
        json.set("userRequest", userRequestNode);
        content = new LinkedHashMap<>();
        options = new ArrayList<>();
    }
    
    public UserRequest(ObjectNode json) throws IllegalArgumentException {
        super(json);
        validate(json);
        this.userRequestNode = (ObjectNode) json.path("userRequest");
        this.content = new LinkedHashMap<>();
        if (json.path("content").isObject()) {
            ObjectNode contentObject = (ObjectNode) json.path("content");
            Iterator<Map.Entry<String, JsonNode>> fieldIterator = contentObject.fields();
            while (fieldIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldIterator.next();
                content.put(field.getKey(), field.getValue().asText());
            }
        }
        this.options = new ArrayList<>();
        if (json.path("options").isArray()) {
            ArrayNode optionsArray = (ArrayNode) json.path("options");
            optionsArray.forEach(entry -> {
                options.add(new Option((ObjectNode) entry));
            });
        }
    }
    
    private static void validate(ObjectNode json) throws IllegalArgumentException {
        JsonNode userRequest = json.path("userRequest");
        JsonUtil.validateObjectNode(userRequest, "userRequest", false);
        JsonUtil.validateStringMap(userRequest.path("content"), "userRequest.content", true);
        JsonUtil.validateObjectArray(userRequest.path("options"), "userRequest.options", true);
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
        JsonUtil.getOrCreateObject(userRequestNode, "content").put(languageId, contentId);
        updatePerformed();
    }

    @Override
    public void removeContentId(String languageId) {
        if (!content.containsKey(languageId)) return;
        content.remove(languageId);
        JsonUtil.getOrCreateObject(userRequestNode, "content").remove(languageId);
        if (content.isEmpty()) {
            json.remove("content");
        }
        updatePerformed();
    }

    @Override
    public List<Option> getOptions() {
        return Collections.unmodifiableList(options);
    }

    @Override
    public void addOption(de.eome.guide.api.Option option) {
        if (!(option instanceof Option)) {
            throw new UnsupportedOperationException("Unsupported implementation type, expects instance of \"de.imc.ap.guide.json.Option\".");
        }
        Option optionImpl = (Option) option;
        optionImpl.setParent(this);
        options.add(optionImpl);
        JsonUtil.getOrCreateArray(userRequestNode, "options").add(optionImpl.asJson());
        updatePerformed();
    }

    @Override
    public void removeOption(de.eome.guide.api.Option option) {
        if (!(option instanceof Option)) {
            throw new UnsupportedOperationException("Unsupported implementation type, expects instance of \"de.imc.ap.guide.json.Option\".");
        }
        Option optionImpl = (Option) option;
        if (options.contains(optionImpl)) {
            options.remove(optionImpl);
            optionImpl.setParent(null);
            JsonNode optionsArray = userRequestNode.get("options");
            if (!optionsArray.isMissingNode()) {
                JsonUtil.removeFromArray((ArrayNode) optionsArray, optionImpl.asJson());
            }
        }
        updatePerformed();
    }
}
