package f5.health.app.vo.openai.response;

import com.theokanning.openai.completion.chat.ChatCompletionResult;
import lombok.Getter;

@Getter
public class PromptCompletion {

    private final String content;

    public PromptCompletion(ChatCompletionResult result) {
        this.content = result.getChoices().get(0).getMessage().getContent();
    }

    @Override
    public String toString() {
        return "PromptCompletion{" +
                "content='" + content + '\'' +
                '}';
    }
}
