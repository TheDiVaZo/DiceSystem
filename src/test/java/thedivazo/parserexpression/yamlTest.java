package thedivazo.parserexpression;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class yamlTest {

    @Test
    void test1() throws InvalidConfigurationException {
        String configFile = "\n" +
                "field1:\n" +
                "- filed2:fff\n" +
                "- field3:ggg\n";
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.loadFromString(configFile);
        for (Map<?, ?> field1 : configuration.getMapList("field1")) {
            for (Map.Entry<?, ?> entry : field1.entrySet()) {
                System.out.println(entry.getKey()+": "+entry.getValue());
            }
            System.out.println("---------------------------");
        }

    }
}
