package f5.health.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import f5.health.app.service.GptService;
import f5.health.app.service.healthreport.dto.FeedbackRequestDto;
import f5.health.app.service.healthreport.dto.FeedbackResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // 시큐리티 필터 비활성화
class GptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GptService gptService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 피드백_생성_API_테스트() throws Exception {
        FeedbackRequestDto dto = new FeedbackRequestDto();
        dto.setWaterIntake(1200);
        dto.setAlcoholAmount(2);
        dto.setSmokingAmount(0);
        dto.setStepCount(8000);
        dto.setExerciseType("걷기");
        dto.setExerciseDuration(40);
        dto.setExerciseCalories(200);
        dto.setHeartRate(75);
        dto.setTotalCaloriesBurned(1800);
        dto.setSleepHours(7);
//        dto.setKcal(2200);
//        dto.setCarbohydrate(250.0);
//        dto.setProtein(90.0);
//        dto.setFat(70.0);

        when(gptService.getScoreAndFeedback(dto)).thenReturn(new FeedbackResponseDto(100, "건강한 하루였습니다!"));


        mockMvc.perform(post("/api/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("건강을 잘 유지하고 있습니다!"));
    }
}