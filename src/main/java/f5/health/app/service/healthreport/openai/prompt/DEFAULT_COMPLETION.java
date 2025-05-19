package f5.health.app.service.healthreport.openai.prompt;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import f5.health.app.entity.healthreport.PromptCompletion;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static f5.health.app.service.healthreport.HealthReportService.MINIMUM_SAVED_MONEY_REQUIRED;

public enum DEFAULT_COMPLETION {

    SAVED_MONEY_DEFAULT_COMPLETION(
            "\uD83D\uDCB0 총 절약 금액이 " +
                    NumberFormat.getNumberInstance(Locale.KOREA).format(MINIMUM_SAVED_MONEY_REQUIRED) +
                    "원을 넘으면, AI가 나에게 딱 맞는 건강 물품을 추천해 드려요."
    );

    private final PromptCompletion completion;

    DEFAULT_COMPLETION(String defaultMessage) {
        ChatCompletionChoice chatCompletionChoice = new ChatCompletionChoice();
        chatCompletionChoice.setMessage(new ChatMessage("default", defaultMessage));
        ChatCompletionResult defaultResult = new ChatCompletionResult();
        defaultResult.setChoices(List.of(chatCompletionChoice));
        this.completion = new PromptCompletion(defaultResult);
    }

    public PromptCompletion get() {
        return this.completion;
    }
}
