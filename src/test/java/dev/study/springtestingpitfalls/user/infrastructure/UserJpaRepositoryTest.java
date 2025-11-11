package dev.study.springtestingpitfalls.user.infrastructure;

import dev.study.springtestingpitfalls.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
public class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given

        // when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(
                1,
                UserStatus.ACTIVE
        );

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // given

        // when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(
                1,
                UserStatus.INACTIVE
        );

        // then
        assertThat(result.isPresent()).isFalse();
        assertThat(result).isEmpty();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given

        // when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus(
                "test@gmail.com",
                UserStatus.ACTIVE
        );

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // given

        // when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus(
                "test@gamil.com",
                UserStatus.INACTIVE
        );

        // then
        assertThat(result.isPresent()).isFalse();
        assertThat(result).isEmpty();
    }
}
