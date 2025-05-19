package f5.health.app.service.savedmoney;

import f5.health.app.entity.Member;
import f5.health.app.repository.MemberRepository;
import f5.health.app.service.healthreport.openai.GptService;
import f5.health.app.service.healthreport.openai.prompt.HealthItemsRecommendPrompt;
import f5.health.app.service.healthreport.vo.request.healthkit.applekit.Workouts;
import f5.health.app.vo.openai.response.PromptCompletion;
import f5.health.app.vo.savedmoney.HealthItemRecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HealthItemRecommendService {

    private final MemberRepository memberRepository;
    private final GptService gptService;

    @Transactional
    public HealthItemRecommendationResponse recommend(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 운동 기록은 현재 없다고 가정하고 빈 Workouts 객체 사용
        Workouts workouts = Workouts.empty();

        // GPT 프롬프트 생성
        HealthItemsRecommendPrompt prompt = new HealthItemsRecommendPrompt(member, workouts);
        PromptCompletion gptResponse = gptService.call(prompt);

        // 추천 결과 저장
        member.updateHealthItemsRecommend(gptResponse);

        return HealthItemRecommendationResponse.of(
                member.getTotalSavedMoney(),
                member.getHealthItemsRecommend()
        );

    }
}
