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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemplateValidator {

    private final TemplateProcessor templateProcessor;

    public TemplateValidator(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    public String processTemplate(String templateContent, Map<String, Object> dataModel) throws Exception {
        return templateProcessor.processTemplate(templateContent, dataModel);
    }

    public static List<String> validateFieldsPresent(String jsonOutput, String[] expectedFields) throws Exception {
        List<String> missing = new ArrayList<>();
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode jsonNode = mapper.readTree(jsonOutput);
        for (String field : expectedFields) {
            if (!field.isEmpty() && !jsonNode.has(field)) {
                missing.add(field);
            }
        }
        return missing;
    }

    public static Map<String, Object> parseJsonToDataModel(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<>() {
        });
    }

    public static String formatFlexibleJson(String input) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Object json = mapper.readValue(input, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e1) {
            try {
                if (input.trim().startsWith("{") && input.contains("\\\"") && !input.trim().startsWith("\"")) {
                    input = "\"" + input + "\"";
                }

                String unescaped = mapper.readValue(input, String.class);
                Object json = mapper.readValue(unescaped, Object.class);

                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            } catch (Exception e2) {
                throw new IllegalArgumentException("El JSON es inválido:\n\n" + e2.getMessage());
            }
        }
    }

}