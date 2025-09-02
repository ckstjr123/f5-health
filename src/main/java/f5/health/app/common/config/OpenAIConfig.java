/*
package f5.health.app.common.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    public static final String OPEN_AI_MODEL = "gpt-4o";
    public static final Double SAMPLING_TEMPERATURE = 0.7;

    @Value("${openai.api.key}")
    private String openAiToken;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(openAiToken);
    }
}
*/
