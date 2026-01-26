package qa.challenge.api.utils;

import qa.challenge.api.base.BaseTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigProp extends BaseTest {
    private static final String PATH_PROP = "/src/test/resources/config.properties";
    private static final String PATH_FULL = System.getProperty("user.dir") + PATH_PROP;
    private static final Properties properties = new Properties();

    private static Properties loadProp() {
        try {
            properties.load(new FileInputStream(PATH_FULL));
        } catch (IOException ex) {
            logger.error("Não foi encontrado o arquivo 'config.properties' em 'src/test/resources'. " +
                    "Crie o arquivo e informe a BASE_URL (https://serverest.dev/). e realize o teste novamente.");
            throw new RuntimeException(ex);
        }
        return properties;
    }

    public static String getSecret(String key) {
        return loadProp().getProperty(key);
    }
}