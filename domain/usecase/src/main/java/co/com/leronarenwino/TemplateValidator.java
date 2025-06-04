/*
 * This file is part of Template Tool.
 *
 * Template Tool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Template Tool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Template Tool. If not, see <https://www.gnu.org/licenses/>.
 */

package co.com.leronarenwino;

import freemarker.template.Configuration;
import freemarker.template.Template;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TemplateValidator {

    private static final Set<String> REQUIRED_FIELDS = new HashSet<>();

    static {
        REQUIRED_FIELDS.add("anotherHeader");
    }

    public static String processTemplate(String templateContent, Map<String, Object> dataModel) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDefaultEncoding("UTF-8");
        Template template = new Template("template", new StringReader(templateContent), cfg);
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        return writer.toString();
    }

    public static boolean validateJson(String jsonOutput) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonOutput);
            for (String field : REQUIRED_FIELDS) {
                if (!jsonNode.has(field)) {
                    System.out.println("Missing required field: " + field);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Invalid JSON: " + e.getMessage());
            return false;
        }
    }

    public static String generateJsonOutput(String templateContent, Map<String, Object> dataModel) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDefaultEncoding("UTF-8");
        Template template = new Template("template", new StringReader(templateContent), cfg);
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        return writer.toString();
    }

    public static String generatePrettyJsonOutput(String templateContent, Map<String, Object> dataModel) throws Exception {
        String jsonOutput = generateJsonOutput(templateContent, dataModel);
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Object json = mapper.readValue(jsonOutput, Object.class);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    public static String formatFreemarkerTemplate(String template) {
        // Remove escaped double quotes only
        String unescaped = template.replace("\\\"", "\"");

        // Find the last assignment (>) to split assignments and JSON
        int lastAssign = unescaped.lastIndexOf('>');
        if (lastAssign != -1 && lastAssign + 1 < unescaped.length()) {
            String assignments = unescaped.substring(0, lastAssign + 1);
            String jsonPart = unescaped.substring(lastAssign + 1).trim();

            // Format assignments: each on its own line, no extra characters
            String[] assignLines = assignments.split("><");
            StringBuilder prettyAssigns = new StringBuilder();
            for (int i = 0; i < assignLines.length; i++) {
                String line = assignLines[i];
                // Remove trailing '>' if present (to avoid double '>')
                if (line.endsWith(">")) {
                    line = line.substring(0, line.length() - 1);
                }
                if (i > 0) prettyAssigns.append("<");
                prettyAssigns.append(line).append(">");
                if (i < assignLines.length - 1) prettyAssigns.append("\n");
            }

            // Pretty-print the JSON part
            String prettyJson = jsonPart;
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                Object json = mapper.readValue(jsonPart, Object.class);
                prettyJson = "\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json) + "\n";
            } catch (Exception e) {
                // If not valid JSON, leave as is
            }

            // Combine formatted assignments and pretty JSON, no extra newlines
            return prettyAssigns + prettyJson;
        }
        return unescaped;
    }

    public static Map<String, Object> parseJsonToDataModel(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
    }

    public static void testing(String[] args) {

        // Example FreeMarker template content
        String templateContent = "<#assign headers = headers>{\"anotherHeader\": \"${headers!\"error\"?truncate(50, '')}\"}";

        Map<String, Object> dataModel = new HashMap<>();

        Map<String, String> headers = new HashMap<>();
        headers.put("notAnotherHeader", "abc123");

        dataModel.put("headers", headers);

        // Validate template
        boolean isValid = validateJson(templateContent);
    }

}