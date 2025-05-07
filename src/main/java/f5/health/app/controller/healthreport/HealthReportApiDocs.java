package f5.health.app.controller.healthreport;

import f5.health.app.service.healthreport.vo.request.MealsRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "건강 일지", description = "리포트 작성 및 조회 API")
public interface HealthReportApiDocs {

    @Operation(summary = "리포트 등록", description = "HealthKit, 식단, 음주/흡연량 등 데이터 처리",
            parameters = {
                    @Parameter(name = "mealsRequest", description = "식단 요청 VO", content = @Content(schema = @Schema(implementation = MealsRequest.class)))
            }
    )

    void submit(MealsRequest mealsRequest);

}
