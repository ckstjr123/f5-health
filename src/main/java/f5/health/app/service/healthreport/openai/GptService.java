package f5.health.app.service.healthreport.openai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import f5.health.app.service.healthreport.openai.prompt.Prompt;
import f5.health.app.vo.openai.response.PromptCompletion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GptService {

    private final OpenAiService openAiService;

    public PromptCompletion call(Prompt prompt) {
        ChatCompletionRequest request = prompt.generate();
        ChatCompletionResult result = openAiService.createChatCompletion(request);
        return new PromptCompletion(result);
    }
}
