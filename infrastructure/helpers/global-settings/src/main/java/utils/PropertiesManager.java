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

package utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesManager {

    private static final Logger logger = Logger.getLogger(PropertiesManager.class.getName());

    private PropertiesManager() {
        throw new IllegalStateException("Utility class");
    }

    public static Properties loadProperties(String filePath, Properties defaultProperties) {
        Properties properties = new Properties();

        try {
            File file = new File(filePath);
            if (!file.exists() && defaultProperties != null) {
                saveProperties(filePath, defaultProperties);
            }
            try (FileReader reader = new FileReader(filePath)) {
                properties.load(reader);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Reader error in PropertiesManager class");
        }

        return properties;
    }

    public static void saveProperties(String filePath, Properties properties) {
        try (FileWriter writer = new FileWriter(filePath)) {
            properties.store(writer, "Properties file");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Properties store error in PropertiesManager class", e);
        }
    }
}