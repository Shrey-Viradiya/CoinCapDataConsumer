package org.sv.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.sv.data.config.ConfigObject;

import java.io.IOException;
import java.io.InputStream;

public class App 
{
    public static ConfigObject readConfigFromResource(String resourceName) throws IOException {
        try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found");
            }

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            return objectMapper.readValue(inputStream, ConfigObject.class);
        }
    }
    public static void main( String[] args ) throws IOException {
        ConfigObject applicationConfiguration = readConfigFromResource("configuration.yaml");
        System.out.println(applicationConfiguration.host());
    }
}
