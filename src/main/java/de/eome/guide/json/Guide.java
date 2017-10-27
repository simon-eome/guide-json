package de.eome.guide.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.eome.guide.json.util.JsonUtil;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Guide model wrapping a JSON object.
 */
public class Guide implements de.eome.guide.api.Guide, UpdateListener {
    private final ObjectNode json;
    private final Map<String, String> content;
    private final List<AccessEntry> accessEntries;
    private final List<Tag> tags;
    private final List<Step> steps;
    private OffsetDateTime lastUpdate;
    
    public Guide(String id) {
        json = JsonNodeFactory.instance.objectNode();
        json.put("id", id);
        lastUpdate = OffsetDateTime.now();
        json.put("lastUpdate", lastUpdate.toString());
        
        content = new LinkedHashMap<>();
        accessEntries = new ArrayList<>();
        tags = new ArrayList<>();
        steps = new ArrayList<>();
        
    }
    
    public Guide(ObjectNode json) throws IllegalArgumentException {
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
        
        accessEntries = new ArrayList<>();
        if (json.path("access").isArray()) {
            ArrayNode accessArray = (ArrayNode) json.path("access");
            accessArray.forEach(entry -> accessEntries.add(new AccessEntry(entry.asText())));
        }
        
        tags = new ArrayList<>();
        if (json.path("tags").isArray()) {
            ArrayNode tagsArray = (ArrayNode) json.path("tags");
            tagsArray.forEach(entry -> tags.add(Tag.ofString(entry.asText())));
        }
        
        steps = new ArrayList<>();
        if (json.path("steps").isArray()) {
            ArrayNode stepsArray = (ArrayNode) json.path("steps");
            stepsArray.forEach(entry -> steps.add(Step.fromJson((ObjectNode) entry)));
        }
    }
    
    private static void validate(JsonNode json) throws IllegalArgumentException {
        JsonUtil.validateTextNode(json.path("id"), "id", false);
        JsonUtil.validateStringMap(json.path("content"), "content", true);
        JsonUtil.validateStringArray(json.path("access"), "access", true);
        JsonUtil.validateStringArray(json.path("tags"), "tags", true);
        JsonUtil.validateObjectArray(json.path("steps"), "steps", true);
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
        if (content.isEmpty()) {
            json.remove("content");
        } else {
            JsonUtil.getOrCreateObject(json, "content").remove(languageId);
        }
        updatePerformed();
    }

    @Override
    public List<AccessEntry> getAllAccessEntries() {
        return Collections.unmodifiableList(accessEntries);
    }

    @Override
    public boolean hasUserAccess(String userId) {
        return accessEntries.stream().anyMatch(entry -> entry.getType() == AccessEntry.Type.USER && entry.getId().equals(userId));
    }

    @Override
    public boolean hasGroupAccess(String groupId) {
        return accessEntries.stream().anyMatch(entry -> entry.getType() == AccessEntry.Type.GROUP && entry.getId().equals(groupId));
    }

    @Override
    public boolean hasAllAccess() {
        return accessEntries.stream().anyMatch(entry -> entry.getType() == AccessEntry.Type.ALL);
    }

    @Override
    public void grantUserAccess(String userId) {
        if (!hasUserAccess(userId)) {
            AccessEntry newEntry = AccessEntry.forUser(userId);
            accessEntries.add(newEntry);
            JsonUtil.getOrCreateArray(json, "access").add(newEntry.toString());
            updatePerformed();
        }
    }
    
    @Override
    public void grantGroupAccess(String groupId) {
        if (!hasGroupAccess(groupId)) {
            AccessEntry newEntry = AccessEntry.forGroup(groupId);
            accessEntries.add(newEntry);
            JsonUtil.getOrCreateArray(json, "access").add(newEntry.toString());
            updatePerformed();
        }
    }

    @Override
    public void grantAllAccess() {
        if (!hasAllAccess()) {
            AccessEntry newEntry = AccessEntry.forAll();
            accessEntries.add(newEntry);
            JsonUtil.getOrCreateArray(json, "access").add(newEntry.toString());
            updatePerformed();
        }
    }

    @Override
    public void revokeUserAccess(String userId) {
        AccessEntry accessEntry = accessEntries.stream().filter(entry -> entry.getType() == AccessEntry.Type.USER && entry.getId().equals(userId)).findAny().orElse(null);
        if (accessEntry != null) {
            accessEntries.remove(accessEntry);
            JsonUtil.removeFromArray((ArrayNode) json.path("access"), accessEntry.toString());
            if (accessEntries.isEmpty()) json.remove("access");
            updatePerformed();
        }
    }

    @Override
    public void revokeGroupAccess(String groupId) {
        AccessEntry accessEntry = accessEntries.stream().filter(entry -> entry.getType() == AccessEntry.Type.GROUP && entry.getId().equals(groupId)).findAny().orElse(null);
        if (accessEntry != null) {
            accessEntries.remove(accessEntry);
            JsonUtil.removeFromArray((ArrayNode) json.path("access"), accessEntry.toString());
            if (accessEntries.isEmpty()) json.remove("access");
            updatePerformed();
        }
    }

    @Override
    public void revokeAllAccess() {
        AccessEntry accessEntry = accessEntries.stream().filter(entry -> entry.getType() == AccessEntry.Type.ALL).findAny().orElse(null);
        if (accessEntry != null) {
            accessEntries.remove(accessEntry);
            JsonUtil.removeFromArray((ArrayNode) json.path("access"), accessEntry.toString());
            if (accessEntries.isEmpty()) json.remove("access");
            updatePerformed();
        }
    }

    @Override
    public List<Tag> getAllTags() {
        return Collections.unmodifiableList(tags);
    }

    @Override
    public List<String> getCustomTags() {
        return tags.stream()
            .filter(tag -> tag.getType() == de.eome.guide.api.Tag.Type.CUSTOM)
            .map(tag -> tag.getValue())
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getTypedTags() {
        return tags.stream()
            .filter(tag -> tag.getType() == de.eome.guide.api.Tag.Type.TYPED)
            .map(tag -> tag.getValue())
            .collect(Collectors.toList());
    }

    @Override
    public void addCustomTag(String value) {
        Tag newTag = new Tag(de.eome.guide.api.Tag.Type.CUSTOM, value);
        if (!tags.contains(newTag)) {
            tags.add(newTag);
            JsonUtil.getOrCreateArray(json, "tags").add(newTag.toString());
            updatePerformed();
        }
    }

    @Override
    public void removeCustomTag(String value) {
        Tag tag = new Tag(de.eome.guide.api.Tag.Type.CUSTOM, value);
        if (tags.contains(tag)) {
            tags.remove(tag);
            JsonUtil.removeFromArray((ArrayNode) json.path("tags"), tag.toString());
            if (tags.isEmpty()) json.remove("tags");
            updatePerformed();
        }
    }

    @Override
    public void addTypedTag(String value) {
        Tag newTag = new Tag(de.eome.guide.api.Tag.Type.TYPED, value);
        if (!tags.contains(newTag)) {
            tags.add(newTag);
            JsonUtil.getOrCreateArray(json, "tags").add(newTag.toString());
            updatePerformed();
        }
    }

    @Override
    public void removeTypedTag(String value) {
        Tag tag = new Tag(de.eome.guide.api.Tag.Type.TYPED, value);
        if (tags.contains(tag)) {
            tags.remove(tag);
            JsonUtil.removeFromArray((ArrayNode) json.path("tags"), tag.toString());
            if (tags.isEmpty()) json.remove("tags");
            updatePerformed();
        }
    }

    @Override
    public List<Step> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    @Override
    public Step getStep(String stepId) {
        return steps.stream().filter(step -> step.getId().equals(stepId)).findAny().orElse(null);
    }

    @Override
    public int indexOfStep(String stepId) {
        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).getId().equals(stepId)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void addStep(int index, de.eome.guide.api.Step step) {
        if (!(step instanceof Step)) {
            throw new UnsupportedOperationException("Only instances of class\"de.imc.ap.guide.json.Step\" are supported.");
        }
        Step typedStep = (Step) step;
        typedStep.setParent(this);
        steps.add(index, typedStep);
        JsonUtil.getOrCreateArray(json, "steps").insert(index, typedStep.asJson());
        updatePerformed();
    }

    @Override
    public void addStep(de.eome.guide.api.Step step) {
        addStep(steps.size(), step);
    }

    @Override
    public Step removeStep(String stepId) {
        Step step = getStep(stepId);
        if (step != null) {
            steps.remove(step);
            step.setParent(null);
            JsonUtil.removeFromArray((ArrayNode) json.path("steps"), step.asJson());
            if (steps.isEmpty()) json.remove("steps");
        }
        updatePerformed();
        return step;
    }
    
    @Override
    public OffsetDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Returns the JSON node wrapped by this object.
     * @return JSON node.
     */
    public ObjectNode asJson() {
        return json;
    }

    @Override
    public void updatePerformed() {
        lastUpdate = OffsetDateTime.now();
        json.put("lastUpdate", lastUpdate.toString());
    }
}
