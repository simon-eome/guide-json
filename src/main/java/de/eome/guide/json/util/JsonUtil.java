package de.eome.guide.json.util;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class JsonUtil {
    private static final JsonFactory factory = new JsonFactory();
    
    private JsonUtil() {};
    
    public static ArrayNode getOrCreateArray(ObjectNode parent, String fieldId) {
        ArrayNode node;
        if (parent.path(fieldId).isArray()) {
            node = (ArrayNode) parent.path(fieldId);
        } else {
            node = JsonNodeFactory.instance.arrayNode();
            parent.set(fieldId, node);
        }
        return node;
    }
    
    public static ObjectNode getOrCreateObject(ObjectNode parent, String fieldId) {
        ObjectNode node;
        if (parent.path(fieldId).isObject()) {
            node = (ObjectNode) parent.path(fieldId);
        } else {
            node = JsonNodeFactory.instance.objectNode();
            parent.set(fieldId, node);
        }
        return node;
    }
    
    public static boolean removeFromArray(ArrayNode array, String value) {
        List<Integer> indexesToRemove = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonNode entry = array.get(i);
            if (entry.asText().equals(value)) {
                indexesToRemove.add(i);
            }
        }
        indexesToRemove.forEach(i -> array.remove(i));
        return !indexesToRemove.isEmpty();
    }
    
    public static boolean removeFromArray(ArrayNode array, JsonNode object) {
        List<Integer> indexesToRemove = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonNode entry = array.get(i);
            if (entry == object) {
                indexesToRemove.add(i);
            }
        }
        indexesToRemove.forEach(i -> array.remove(i));
        return !indexesToRemove.isEmpty();
    }
    
    public static void validateStringArray(JsonNode node, String fieldName, boolean isOptional) throws IllegalArgumentException {
        if (node.isMissingNode()) {
            if (!isOptional) {
                throw new IllegalArgumentException("Missing required array [" + fieldName + "].");
            }
        } else {
            if (!node.isArray()) {
                throw new IllegalArgumentException("Invalid type of field [" + fieldName + "], array expected.");
            }
            for (JsonNode entry : (ArrayNode) node) {
                if (!entry.isTextual()) throw new IllegalArgumentException("Invalid entry for array [" + fieldName + "], string expected.");
            }
        }
    }
    
    public static void validateObjectArray(JsonNode node, String fieldName, boolean isOptional) throws IllegalArgumentException {
        if (node.isMissingNode()) {
            if (!isOptional) {
                throw new IllegalArgumentException("Missing required array [" + fieldName + "].");
            }
        } else {
            if (!node.isArray()) {
                throw new IllegalArgumentException("Invalid type of field [" + fieldName + "], array expected.");
            }
            for (JsonNode entry : (ArrayNode) node) {
                if (!entry.isObject()) throw new IllegalArgumentException("Invalid entry for array [" + fieldName + "], object expected.");
            }
        }
    }
    
    public static void validateTextNode(JsonNode node, String fieldName, boolean isOptional) {
        if (node == null || node.isMissingNode()) {
            if (!isOptional) {
                throw new IllegalArgumentException("Missing required text node [" + fieldName + "].");
            }
        } else {
            if (!node.isTextual()) {
                throw new IllegalArgumentException("Invalid type of node [" + fieldName + "], string expected.");
            }
        }
    }
    
    public static void validateObjectNode(JsonNode node, String fieldName, boolean isOptional) {
        if (node == null || node.isMissingNode()) {
            if (!isOptional) {
                throw new IllegalArgumentException("Missing required object node [" + fieldName + "].");
            }
        } else {
            if (!node.isObject()) {
                throw new IllegalArgumentException("Invalid type of node [" + fieldName + "], object expected.");
            }
        }
    }
    
    public static void validateArrayNode(JsonNode node, String fieldName, boolean isOptional) {
        if (node == null || node.isMissingNode()) {
            if (!isOptional) {
                throw new IllegalArgumentException("Missing required array node [" + fieldName + "].");
            }
        } else {
            if (!node.isArray()) {
                throw new IllegalArgumentException("Invalid type of node [" + fieldName + "], array expected.");
            }
        }
    }
    
    public static void validateStringMap(JsonNode node, String fieldName, boolean isOptional) {
        if (node == null || node.isMissingNode()) {
            if (!isOptional) {
                throw new IllegalArgumentException("Missing required object [" + fieldName + "].");
            }
        } else {
            if (!node.isObject()) {
                throw new IllegalArgumentException("Invalid type of field [" + fieldName + "], object expected.");
            }
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                if (!fields.next().getValue().isTextual()) {
                    throw new IllegalArgumentException("Invalid type of entry for map [" + fieldName + "] , string expected.");
                }
            }
        }
    }
    
    public static String exportJson(JsonNode jsonNode, boolean usePrettyPrint) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String output;
        try (StringWriter writer = new StringWriter()) {
            JsonGenerator gen = factory.createGenerator(writer);
            gen.useDefaultPrettyPrinter();
            mapper.writeTree(gen, jsonNode);
            output = writer.toString();
        }
        return output;
    }
    
    public static void writeJson(JsonNode jsonNode, File file, boolean usePrettyPrint) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = factory.createGenerator(file, JsonEncoding.UTF8);
        gen.useDefaultPrettyPrinter();
        mapper.writeTree(gen, jsonNode);
    }
    
    public static ObjectNode readJson(String jsonString) throws IllegalArgumentException {
        ObjectNode objectNode;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(jsonString);
            if (!node.isObject()) {
                throw new IllegalArgumentException("The given string does not encode a valid JSON object.");
            }
            objectNode = (ObjectNode) node;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Not a valid JSON object.", ex);
        }
        return objectNode;
    }
    
    public static ObjectNode readJson(File file) throws IllegalArgumentException {
        ObjectNode objectNode;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(file);
            if (!node.isObject()) {
                throw new IllegalArgumentException("The given file contains no valid JSON object.");
            }
            objectNode = (ObjectNode) node;
        } catch (IOException ex) {
            throw new IllegalArgumentException("File not readable or no a valid JSON object.", ex);
        }
        return objectNode;
    }
    
    public static ObjectNode mapToJsonObject(Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map, ObjectNode.class);
    }
    
    public static Map<String, Object> jsonObjectToMap(ObjectNode node) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(node, Map.class);
    }
    
    public static ArrayNode listToJsonArray(List<Object> list) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(list, ArrayNode.class);
    }
    
    public static List<Object> jsonArrayToList(ArrayNode node) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(node, List.class);
    }
}
