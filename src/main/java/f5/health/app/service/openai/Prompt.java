package f5.health.app.service.openai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;

public interface Prompt {
    ChatCompletionRequest generate();
}