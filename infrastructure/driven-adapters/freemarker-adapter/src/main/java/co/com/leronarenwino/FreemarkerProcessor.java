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

import co.com.leronarenwino.config.FreemarkerConfigProvider;
import freemarker.template.Template;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

public class FreemarkerProcessor {

    public static String processTemplate(String templateContent, Map<String, Object> dataModel) throws Exception {
        Template template = new Template("template", new StringReader(templateContent), FreemarkerConfigProvider.getConfiguration());
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        return writer.toString();
    }

}