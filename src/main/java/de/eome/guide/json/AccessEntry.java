package de.eome.guide.json;

public class AccessEntry implements de.eome.guide.api.AccessEntry {
    private final Type type;
    private final String id;
    
    public AccessEntry(String accessEntryString) throws IllegalArgumentException {
        String[] splittedString = accessEntryString.split(":");
        
        switch (splittedString[0]) {
            case "@user":
                type = Type.USER;
                break;
            case "@group":
                type = Type.GROUP;
                break;
            case "@all":
                type = Type.ALL;
                break;
            default:
                throw new IllegalArgumentException("Missing or invalid access entry type.");
        }
        
        if (type == Type.USER || type == Type.GROUP) {
            if (splittedString.length > 1) {
                id = accessEntryString.substring(splittedString[0].length());
            } else {
                throw new IllegalArgumentException("Access entries of type USER and GROUP require a target identifier, e.g., \"@group:administrators\".");
            }
        } else {
            id = null;
        }
    }
    
    private AccessEntry(Type type, String targetId) {
        this.type = type;
        this.id = targetId;
    }
    
    public static AccessEntry forAll() {
        return new AccessEntry(Type.ALL, null);
    }
    
    public static AccessEntry forGroup(String groupId) {
        return new AccessEntry(Type.GROUP, groupId);
    }
    
    public static AccessEntry forUser(String userId) {
        return new AccessEntry(Type.USER, userId);
    }
    
    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        switch (type) {
            case ALL:
                builder.append("@all");
                break;
            case GROUP:
                builder.append("@group").append(":").append(id);
                break;
            case USER:
                builder.append("@user").append(":").append(id);
                break;
        }
        return builder.toString();
    }
}
