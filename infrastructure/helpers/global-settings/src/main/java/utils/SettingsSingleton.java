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

package utils;

import java.util.Properties;

public class SettingsSingleton {

    private SettingsSingleton() {
    }

    public static final String FREEMARKER_LOCALE = "locale";
    public static final String FREEMARKER_TIME_ZONE = "time_zone";

    private static final String DEFAULT_LOCALE = "en_US";
    private static final String DEFAULT_TIME_ZONE = "UTC";

    private static String locale = DEFAULT_LOCALE;
    private static String timeZone = DEFAULT_TIME_ZONE;

    private static String theme = "Dark";

    public static String getTheme() {
        return theme;
    }

    public static void setTheme(String newTheme) {
        if (newTheme != null && (newTheme.equals("Dark") || newTheme.equals("Light"))) {
            theme = newTheme;
        }
    }

    public static Properties defaultFreemarkerProperties() {
        Properties properties = new Properties();
        properties.setProperty(FREEMARKER_LOCALE, DEFAULT_LOCALE);
        properties.setProperty(FREEMARKER_TIME_ZONE, DEFAULT_TIME_ZONE);
        return properties;
    }

    public static void setSettingsFromProperties(Properties properties) {
        locale = properties.getProperty(FREEMARKER_LOCALE, DEFAULT_LOCALE);
        timeZone = properties.getProperty(FREEMARKER_TIME_ZONE, DEFAULT_TIME_ZONE);
    }

    public static String getLocale() {
        return locale;
    }

    public static String getTimeZone() {
        return timeZone;
    }

}