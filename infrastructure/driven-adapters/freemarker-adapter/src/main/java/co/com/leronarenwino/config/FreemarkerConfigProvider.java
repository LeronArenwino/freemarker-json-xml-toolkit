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

package co.com.leronarenwino.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import utils.SettingsSingleton;

import java.util.Locale;
import java.util.TimeZone;

public class FreemarkerConfigProvider {
    private static Configuration cfg = createConfiguration();

    public static synchronized void reloadConfiguration() {
        cfg = createConfiguration();
    }

    private static Configuration createConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_34);
        configuration.setDefaultEncoding("UTF-8");

        // Load settings from SettingsSingleton (which reads from properties)
        String localeStr = SettingsSingleton.getLocale();
        String timeZoneStr = SettingsSingleton.getTimeZone();

        // Set locale
        if (localeStr != null && !localeStr.isEmpty()) {
            String[] parts = localeStr.split("_");
            if (parts.length == 2) {
                configuration.setLocale(new Locale(parts[0], parts[1]));
            } else {
                configuration.setLocale(Locale.forLanguageTag(localeStr));
            }
        }

        // Set time zone
        if (timeZoneStr != null && !timeZoneStr.isEmpty()) {
            configuration.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
        }

        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);

        return configuration;
    }

    public static Configuration getConfiguration() {
        return cfg;
    }
}