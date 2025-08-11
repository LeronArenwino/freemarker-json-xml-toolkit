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

    public static final String APP_THEME = "theme";
    private static final String DEFAULT_THEME = "Dark";

    private static String theme = DEFAULT_THEME;

    public static final String RSYNTAX_THEME = "rsyntax_theme";
    private static final String DEFAULT_RSYNTAX_THEME = "idea.xml";

    private static String rsyntaxTheme = DEFAULT_RSYNTAX_THEME;

    public static final String EXPECTED_FIELDS_VISIBLE = "expected_fields_visible";
    private static final boolean DEFAULT_EXPECTED_FIELDS_VISIBLE = true;
    private static boolean expectedFieldsVisible = DEFAULT_EXPECTED_FIELDS_VISIBLE;

    public static Properties defaultAppProperties() {
        Properties properties = new Properties();
        properties.setProperty(FREEMARKER_LOCALE, DEFAULT_LOCALE);
        properties.setProperty(FREEMARKER_TIME_ZONE, DEFAULT_TIME_ZONE);
        properties.setProperty(APP_THEME, DEFAULT_THEME);
        properties.setProperty(RSYNTAX_THEME, DEFAULT_RSYNTAX_THEME);
        properties.setProperty(EXPECTED_FIELDS_VISIBLE, String.valueOf(DEFAULT_EXPECTED_FIELDS_VISIBLE));
        return properties;
    }

    public static String getRSyntaxTheme() {
        return rsyntaxTheme;
    }

    public static void setRSyntaxTheme(String theme) {
        if (theme != null && !theme.isEmpty()) {
            rsyntaxTheme = theme;
        }
    }

    public static String getTheme() {
        return theme;
    }

    public static void setTheme(String newTheme) {
        if (newTheme != null && (newTheme.equals("Dark") || newTheme.equals("Light"))) {
            theme = newTheme;
        }
    }

    public static void setSettingsFromProperties(Properties properties) {
        locale = properties.getProperty(FREEMARKER_LOCALE, DEFAULT_LOCALE);
        timeZone = properties.getProperty(FREEMARKER_TIME_ZONE, DEFAULT_TIME_ZONE);
        theme = properties.getProperty(APP_THEME, DEFAULT_THEME);
        rsyntaxTheme = properties.getProperty(RSYNTAX_THEME, DEFAULT_RSYNTAX_THEME);
        expectedFieldsVisible = Boolean.parseBoolean(properties.getProperty(EXPECTED_FIELDS_VISIBLE, String.valueOf(DEFAULT_EXPECTED_FIELDS_VISIBLE)));
    }

    public static String getLocale() {
        return locale;
    }

    public static String getTimeZone() {
        return timeZone;
    }

    public static boolean isExpectedFieldsVisible() {
        return expectedFieldsVisible;
    }

    public static void setExpectedFieldsVisible(boolean visible) {
        expectedFieldsVisible = visible;
    }

}