/*
 * This file is part of FreeMarker JSON/XML Toolkit.
 *
 * FreeMarker JSON/XML Toolkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FreeMarker JSON/XML Toolkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FreeMarker JSON/XML Toolkit. If not, see <https://www.gnu.org/licenses/>.
 */

package co.com.leronarenwino;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemplateValidator {

    private final TemplateProcessor templateProcessor;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public TemplateValidator(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    public String processTemplate(String templateContent, Map<String, Object> dataModel) throws Exception {
        return templateProcessor.processTemplate(templateContent, dataModel);
    }

    public static List<String> validateFieldsPresentWithTypes(String jsonOutput, String[] expectedFields) throws Exception {
        List<String> missing = new ArrayList<>(expectedFields.length);
        JsonNode jsonNode = MAPPER.readTree(jsonOutput);
        for (String field : expectedFields) {
            if (field.isEmpty()) continue;
            String[] parts = field.split(":");
            String fieldPath = parts[0];
            String expectedType = parts.length > 1 ? parts[1].toLowerCase() : null;
            JsonNode valueNode = getNestedField(jsonNode, fieldPath);
            if (valueNode == null || valueNode.isMissingNode()) {
                missing.add(field);
            } else if (expectedType != null && !matchesType(valueNode, expectedType)) {
                missing.add(field + " (type mismatch)");
            }
        }
        return missing;
    }

    private static JsonNode getNestedField(JsonNode node, String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        JsonNode current = node;
        for (String part : parts) {
            if (current == null || !current.has(part)) {
                return null;
            }
            current = current.get(part);
        }
        return current;
    }

    private static boolean matchesType(JsonNode node, String type) {
        return switch (type) {
            case "string" -> node.isTextual();
            case "number" -> node.isNumber();
            case "boolean" -> node.isBoolean();
            case "object" -> node.isObject();
            case "array" -> node.isArray();
            case "null" -> node.isNull();
            default -> false;
        };
    }

    public static Map<String, Object> parseJsonToDataModel(String json) throws Exception {
        return MAPPER.readValue(json, new TypeReference<>() {
        });
    }

    public static String formatFlexibleJson(String input) {
        try {
            Object json = MAPPER.readValue(input, Object.class);
            String pretty = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            return pretty.replace("\r\n", "\n");
        } catch (Exception e1) {
            try {
                String toParse = input;
                if (toParse.trim().startsWith("{") && toParse.contains("\\\"") && !toParse.trim().startsWith("\"")) {
                    toParse = "\"" + toParse + "\"";
                }
                String unescaped = MAPPER.readValue(toParse, String.class);
                Object json = MAPPER.readValue(unescaped, Object.class);
                String pretty = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                return pretty.replace("\r\n", "\n");
            } catch (Exception e2) {
                throw new IllegalArgumentException("El JSON es inválido:\n\n" + e2.getMessage());
            }
        }
    }
}