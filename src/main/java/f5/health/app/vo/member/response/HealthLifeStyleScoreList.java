package f5.health.app.vo.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Schema(description = "생활 습관 점수 변동치")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HealthLifeStyleScoreList {

    @Schema(description = "날짜 범위로 조회된 점수 리스트", nullable = true)
    private final List<HealthLifeScore> scores;

    public static HealthLifeStyleScoreList from(List<HealthLifeScore> scores) {
        return new HealthLifeStyleScoreList(scores);
    }
}
