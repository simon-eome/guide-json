package de.eome.guide.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class Action extends Step implements de.eome.guide.api.Action {

    public Action(String id) {
        super(id);
    }
    
    public Action(ObjectNode actionNode) {
        super(actionNode);
        validate(actionNode);
    }
    
    private static void validate(ObjectNode json) throws IllegalArgumentException {
        // nothing
    }
}
