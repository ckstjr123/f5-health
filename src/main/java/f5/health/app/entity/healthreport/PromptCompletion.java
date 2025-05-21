package f5.health.app.entity.healthreport;

import com.theokanning.openai.completion.chat.ChatCompletionResult;
import io.jsonwebtoken.lang.Assert;
import lombok.Getter;

@Getter
public class PromptCompletion {

    private final String content;

    public PromptCompletion(ChatCompletionResult result) {
        Assert.notNull(result);
        String original = result.getChoices().get(0).getMessage().getContent();
        int endIndex = original.lastIndexOf(".") + 1;
        this.content = original.substring(0, endIndex);
    }

    @Override
    public String toString() {
        return "PromptCompletion{" +
                "content='" + content + '\'' +
                '}';
    }
}
