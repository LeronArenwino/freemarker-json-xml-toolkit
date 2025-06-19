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

import co.com.leronarenwino.config.FreemarkerConfigProvider;
import freemarker.template.Template;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

public class FreemarkerProcessor implements TemplateProcessor{

    @Override
    public String processTemplate(String templateContent, Map<String, Object> dataModel) throws Exception {
        Template template = new Template("template", new StringReader(templateContent), FreemarkerConfigProvider.getConfiguration());
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        return writer.toString();
    }

}