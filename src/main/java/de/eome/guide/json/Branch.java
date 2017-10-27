package de.eome.guide.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JSON wrapper implementation of the branch model interface.
 */
public abstract class Branch extends Step implements de.eome.guide.api.Branch {
    protected Branch(String id) {
        super(id);
    }
    
    protected Branch(ObjectNode json) throws IllegalArgumentException {
        super(json);
    }
}
