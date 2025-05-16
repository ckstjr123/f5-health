package f5.health.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import f5.health.app.service.openai.GptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

//        when(gptService.call(new HealthFeedbackPrompt())).thenReturn(new PromptCompletion());

//
//        mockMvc.perform(post("/test/health/feedback")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString()))
//                .andExpect(status().isOk())
//                .andExpect(content().string("건강을 잘 유지하고 있습니다!"));
    }
}