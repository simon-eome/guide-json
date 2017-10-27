package de.eome.guide.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.eome.guide.json.util.JsonUtil;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JSON wrapper implementation for the content descriptor model.
 */
public class Content implements de.eome.guide.api.Content, UpdateListener {
    private final ObjectNode json;
    private Media media;
    private final List<IconizedMessage> warnings;
    private final List<IconizedMessage> hints;
    private OffsetDateTime lastUpdate;
    
    public Content(String id) {
        json = JsonNodeFactory.instance.objectNode();
        json.put("id", id);
        lastUpdate = OffsetDateTime.now();
        json.put("lastUpdate", lastUpdate.toString());
        media = null;
        warnings = new ArrayList<>();
        hints = new ArrayList<>();
    }
    
    public Content(ObjectNode json) throws IllegalArgumentException {
        validate(json);
        this.json = json;
        warnings = new ArrayList<>();
        hints = new ArrayList<>();
        if (json.path("media").isObject()) {
            ObjectNode mediaNode = (ObjectNode) json.path("media");
            media = new Media(mediaNode);
        }
        if (json.path("warnings").isArray()) {
            ArrayNode warningsArray = (ArrayNode) json.path("warnings");
            warningsArray.forEach(entry -> {
                warnings.add(new IconizedMessage((ObjectNode) entry));
            });
        }
        if (json.path("hints").isArray()) {
            ArrayNode hintsArray = (ArrayNode) json.path("hints");
            hintsArray.forEach(entry -> {
                hints.add(new IconizedMessage((ObjectNode) entry));
            });
        }
    }
    
    private static void validate(ObjectNode json) throws IllegalArgumentException {
        JsonUtil.validateTextNode(json.path("id"), "id", false);
        JsonUtil.validateObjectArray(json.path("warnings"), "warnings", true);
        JsonUtil.validateObjectArray(json.path("hints"), "hints", true);
    }

    @Override
    public String getId() {
        return json.path("id").asText(null);
    }

    @Override
    public void setLanguageId(String languageId) {
        json.set("languageId", JsonNodeFactory.instance.textNode(languageId));
        updatePerformed();
    }

    @Override
    public String getLanguageId() {
        return json.path("languageId").asText(null);
    }

    @Override
    public void setTitle(String title) {
        json.set("title", JsonNodeFactory.instance.textNode(title));
        updatePerformed();
    }

    @Override
    public String getTitle() {
        return json.path("title").asText(null);
    }

    @Override
    public void setDescription(String description) {
        json.put("description", description);
        updatePerformed();
    }

    @Override
    public String getDescription() {
        return json.path("description").asText(null);
    }

    @Override
    public void setMedia(de.eome.guide.api.Media media) {
        if (!(media instanceof Media)) {
            throw new UnsupportedOperationException("Operation only supported for instances of class \"de.imc.ap.guide.json.Media\".");
        }
        Media typedMedia = (Media) media;
        typedMedia.setParent(this);
        this.media = typedMedia;
        json.set("media", typedMedia.asJson());
        updatePerformed();
    }

    @Override
    public Media getMedia() {
        return media;
    }
    
    @Override
    public void removeMedia() {
        if (media != null) {
            media = null;
            json.remove("media");
            updatePerformed();
        }
    }

    @Override
    public List<IconizedMessage> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    @Override
    public void addWarning(de.eome.guide.api.IconizedMessage warning) {
        if (!(warning instanceof IconizedMessage)) {
            throw new UnsupportedOperationException("Only available for warnings of class \"de.imc.ap.json.IconizedMessage\".");
        }
        IconizedMessage typedWarning = (IconizedMessage) warning;
        if (!warnings.contains(typedWarning)) {
            warnings.add(typedWarning);
            typedWarning.setParent(this);
            JsonUtil.getOrCreateArray(json, "warnings").add(typedWarning.asJson());
            updatePerformed();
        }
    }

    @Override
    public void removeWarning(de.eome.guide.api.IconizedMessage warning) {
        if (!(warning instanceof IconizedMessage)) {
            throw new UnsupportedOperationException("Only available for warnings of class \"de.imc.ap.json.IconizedMessage\".");
        }
        IconizedMessage typedWarning = (IconizedMessage) warning;
        if (warnings.contains(typedWarning)) {
            warnings.remove(typedWarning);
            typedWarning.setParent(null);
            JsonUtil.removeFromArray((ArrayNode) json.path("warnings"), typedWarning.asJson());
            updatePerformed();
        }
    }

    @Override
    public List<? extends de.eome.guide.api.IconizedMessage> getHints() {
        return Collections.unmodifiableList(hints);
    }

    @Override
    public void addHint(de.eome.guide.api.IconizedMessage hint) {
        if (!(hint instanceof IconizedMessage)) {
            throw new UnsupportedOperationException("Only available for hints of class \"de.imc.ap.json.IconizedMessage\".");
        }
        IconizedMessage typedHint = (IconizedMessage) hint;
        if (!hints.contains(typedHint)) {
            hints.add(typedHint);
            typedHint.setParent(this);
            JsonUtil.getOrCreateArray(json, "hints").add(typedHint.asJson());
            updatePerformed();
        }
    }

    @Override
    public void removeHint(de.eome.guide.api.IconizedMessage hint) {
        if (!(hint instanceof IconizedMessage)) {
            throw new UnsupportedOperationException("Only available for hints of class \"de.imc.ap.json.IconizedMessage\".");
        }
        IconizedMessage typedHint = (IconizedMessage) hint;
        if (hints.contains(typedHint)) {
            hints.remove(typedHint);
            typedHint.setParent(null);
            JsonUtil.removeFromArray((ArrayNode) json.path("hints"), typedHint.asJson());
            updatePerformed();
        }
    }
    
    @Override
    public OffsetDateTime getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public void updatePerformed() {
        lastUpdate = OffsetDateTime.now();
        json.put("lastUpdate", lastUpdate.toString());
    }

    /**
     * Returns the JSON object wrapped by this object.
     * @return JSON object.
     */
    public ObjectNode asJson() {
        return json;
    }

}
