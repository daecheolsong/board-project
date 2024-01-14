package com.example.boardproject.config;

import com.example.boardproject.domain.UserAccount;
import com.example.boardproject.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
@Import(SecurityConfig.class)
public class TestSecurityConfig {
    @MockBean
    private UserAccountRepository userAccountRepository;

    @BeforeTestMethod
    public void securitySetUp() {
        given(userAccountRepository.findByUserId(anyString()))
                .willReturn(Optional.of(
                        UserAccount.of(
                                "tester1",
                                "pw",
                                "test@gmail.com",
                                "testset",
                                "test memo"
                        )
                ));
    }
}
