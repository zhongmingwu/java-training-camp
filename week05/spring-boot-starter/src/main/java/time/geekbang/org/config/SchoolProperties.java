package time.geekbang.org.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "school")
public class SchoolProperties {
    private List<Integer> studentIds;
    private List<String> studentNames;
    private List<Integer> klassIds;
    private List<String> klassNames;
    private List<Map<String, Integer>> studentOfKlass;
}
