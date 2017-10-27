package de.eome.guide.json.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.eome.guide.json.Content;
import de.eome.guide.json.Guide;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for handling guides.
 */
public final class GuideUtil {
    private static final JsonFactory factory = new JsonFactory();
    
    /**
     * Reads a guide from a JSON string.
     * @param jsonString JSON string representing a guide.
     * @return Guide represented by the JSON object.
     * @throws IllegalArgumentException The given string is either no valid JSON object or does not encode a guide.
     */
    public static Guide readGuide(String jsonString) throws IllegalArgumentException {
        ObjectNode objectNode = JsonUtil.readJson(jsonString);
        return new Guide(objectNode);
    }
    
    /**
     * Reads a guide from a JSON file.
     * @param file JSON file.
     * @return Guide represented by the JSON file.
     * @throws IllegalArgumentException Failed to read file or no valid JSON object.
     */
    public static Guide readGuide(File file) throws IllegalArgumentException {
        ObjectNode objectNode = JsonUtil.readJson(file);
        return new Guide(objectNode);
    }
    
    /**
     * Exports a guide as JSON string.
     * @param guide Guide to serialize as JSON.
     * @param usePrettyPrint If set to <code>true</code>, whitepaces and identation is used.
     * @return JSON string encoding the guide.
     * @throws IOException Failed to serialize guide.
     */
    public static String exportGuide(Guide guide, boolean usePrettyPrint) throws IOException {
        return JsonUtil.exportJson(guide.asJson(), usePrettyPrint);
    }
    
    /**
     * Writes a guide as JSON string directly in a file.
     * @param guide Guide to write.
     * @param usePrettyPrint If set to <code>true</code>, whitepaces and identation is used.
     * @param file File to write the JSON string in.
     * @throws IOException Failed to write the given file.
     */
    public static void writeGruide(Guide guide, File file, boolean usePrettyPrint) throws IOException {
        JsonUtil.writeJson(guide.asJson(), file, usePrettyPrint);
    }
    
    public static Content readContent(String contentString) throws IllegalArgumentException {
        ObjectNode object = JsonUtil.readJson(contentString);
        return new Content(object);
    }
    
    public static Content readContent(File file) throws IllegalArgumentException {
        ObjectNode object = JsonUtil.readJson(file);
        return new Content(object);
    }
    
    public static String exportContent(Content content, boolean usePrettyPrint) throws IOException {
       return JsonUtil.exportJson(content.asJson(), usePrettyPrint);
    }
    
    public static void writeContent(Content content, File file, boolean usePrettyPrint) throws IOException {
        JsonUtil.writeJson(content.asJson(), file, usePrettyPrint);
    }
}
