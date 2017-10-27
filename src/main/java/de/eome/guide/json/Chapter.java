package de.eome.guide.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.eome.guide.json.util.JsonUtil;

/**
 * JSON wrapper implementation for chapter model interface.
 */
public class Chapter extends Step implements de.eome.guide.api.Chapter {
    
    public Chapter(String id, String target) {
        super(id);
        json.put("target", target);
    }
    
    protected Chapter(ObjectNode json) throws IllegalArgumentException {
        super(json);
        validate(json);
    }
    
    private static void validate(ObjectNode json) throws IllegalArgumentException {
        JsonUtil.validateTextNode(json.path("target"), "target", false);
    }

    @Override
    public void setTarget(String guideId) {
        json.put("target", guideId);
        updatePerformed();
    }

    @Override
    public String getTarget() {
        return json.path("target").asText(null);
    }
}
