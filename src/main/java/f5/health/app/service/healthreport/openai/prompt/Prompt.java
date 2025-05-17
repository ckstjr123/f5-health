package f5.health.app.service.healthreport.openai.prompt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;

public interface Prompt {
    ChatCompletionRequest generate();
}