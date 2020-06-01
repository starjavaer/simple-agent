package com.ixingji.agent.guarder.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesUtils {

    public static Properties load(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream,
                StandardCharsets.UTF_8)
        ) {
            properties.load(inputStreamReader);
        }
        return properties;
    }

}
