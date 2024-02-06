package com.example.boardproject.config;

import com.example.boardproject.domain.UserAccount;
import com.example.boardproject.dto.UserAccountDto;
import com.example.boardproject.repository.UserAccountRepository;
import com.example.boardproject.service.UserAccountService;
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
    private UserAccountService userAccountService;

    @BeforeTestMethod
    public void securitySetUp() {

        given(userAccountService.searchUser(anyString()))
                .willReturn(Optional.of(createUserAccountDto()));
        given(userAccountService.saveUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .willReturn(createUserAccountDto());
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "tester1",
                "pw",
                "test@gmail.com",
                "testset",
                "test memo"
        );
    }
}
