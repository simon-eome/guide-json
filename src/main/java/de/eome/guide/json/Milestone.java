package de.eome.guide.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JSON wrapper implementation of the milestone interface model-
 */
public class Milestone extends Step implements de.eome.guide.api.Milestone {
    public Milestone(String id) {
        super(id);
    }
    
    public Milestone(ObjectNode json) throws IllegalArgumentException {
        super(json);
        validate(json);
    }
    
    private static void validate(ObjectNode json) throws IllegalArgumentException {
        // nothing;
    }
}
