package f5.health.app.vo.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "생활 습관 점수 변동치")
public class HealthLifestyleScoreList {

    @Schema(description = "날짜 범위로 조회된 점수 리스트", nullable = true)
    private final List<HealthLifestyleScore> scores;

    private HealthLifestyleScoreList(List<HealthLifestyleScore> scores) {
        this.scores = scores;
    }

    public static HealthLifestyleScoreList from(List<HealthLifestyleScore> scores) {
        return new HealthLifestyleScoreList(scores);
    }
}
