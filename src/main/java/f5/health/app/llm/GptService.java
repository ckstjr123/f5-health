/*
package f5.health.app.llm;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static f5.health.app.common.config.OpenAIConfig.OPEN_AI_MODEL;
import static f5.health.app.common.config.OpenAIConfig.SAMPLING_TEMPERATURE;

@Service
@RequiredArgsConstructor
public class GptService {

    private final OpenAiService openAiService;

    public ChatCompletionResult call(int maxTokens, List<ChatMessage> messages) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(OPEN_AI_MODEL)
                .messages(messages)
                .temperature(SAMPLING_TEMPERATURE)
                .maxTokens(maxTokens)
                .build();
        return openAiService.createChatCompletion(request);
    }
}
*/
