package dev.study.springtestingpitfalls.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.study.springtestingpitfalls.model.dto.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JavaMailSender javaMailSender;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_회원_가입을_할_수_있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
        // given
        UserCreateDto createDto = UserCreateDto.builder()
                .email("test@gmail.com")
                .nickname("james")
                .address("Seoul")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // when & then
        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("james"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
