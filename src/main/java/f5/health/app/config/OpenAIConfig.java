package f5.health.app.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String secretKey;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(secretKey);
    }
}

