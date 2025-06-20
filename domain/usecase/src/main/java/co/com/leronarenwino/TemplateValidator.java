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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String formatJson(String input) throws Exception {
        String unescaped = input.replace("\\\"", "\"");

        Pattern pattern = Pattern.compile("(\"[^\"]+\":)\"(\\{.*?})\"");
        Matcher matcher = pattern.matcher(unescaped);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            String embedded = matcher.group(2);
            String escapedJson = embedded.replace("\"", "\\\"");
            matcher.appendReplacement(sb, key + "\"" + escapedJson + "\"");
        }

        matcher.appendTail(sb);

        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(sb.toString(), Object.class);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}