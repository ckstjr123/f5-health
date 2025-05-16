package f5.health.app.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class OpenAIConfig {

    public static final String OPEN_AI_MODEL = "gpt-4o";
    public static final Double SAMPLING_TEMPERATURE = 0.7;

    @Value("${openai.api.key}")
    private String secretKey;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(secretKey);
    }
}
