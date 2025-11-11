package dev.study.springtestingpitfalls.user.service;

import dev.study.springtestingpitfalls.common.domain.exception.CertificationCodeNotMatchedException;
import dev.study.springtestingpitfalls.common.domain.exception.ResourceNotFoundException;
import dev.study.springtestingpitfalls.user.domain.UserStatus;
import dev.study.springtestingpitfalls.user.domain.UserCreate;
import dev.study.springtestingpitfalls.user.domain.UserUpdate;
import dev.study.springtestingpitfalls.user.infrastructure.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:application-test.yml")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "test@gmail.com";

        // when
        UserEntity result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("james");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        String email = "test2@gmail.com";

        // when

        // then
        assertThatThrownBy(() -> {
            userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given

        // when
        UserEntity result = userService.getById(1L);

        // then
        assertThat(result.getNickname()).isEqualTo("james");
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올_수_없다() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("test3@gamil.com")
                .address("Seoul")
                .nickname("talon")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // when
        UserEntity result = userService.create(userCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {
        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("talon2")
                .address("Daegu")
                .build();

        // when
        userService.update(
                1L,
                userUpdate
        );

        // then
        UserEntity result = userService.getById(1L);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNickname()).isEqualTo("talon2");
        assertThat(result.getAddress()).isEqualTo("Daegu");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        // given


        // when
        userService.login(1L);

        // then
        UserEntity result = userService.getById(1L);
        assertThat(result.getLastLoginAt()).isGreaterThan(0L);
//        assertThat(result.getLastLoginAt()).isEqualTo("T.T"); // FIXME
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        // given

        // when
        userService.verifyEmail(
                2L,
                "aaaaaaa-aaaa-aaaa-aaaaaaaaab"
        );

        // then
        UserEntity result = userService.getById(1L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(
                    2L,
                    "aaaaaaa-aaaa-aaaa-aaaaaaaaac"
            );
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
