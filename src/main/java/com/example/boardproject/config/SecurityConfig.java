package com.example.boardproject.config;

import com.example.boardproject.dto.UserAccountDto;
import com.example.boardproject.dto.security.BoardPrincipal;
import com.example.boardproject.dto.security.KakaoOAuth2Response;
import com.example.boardproject.repository.UserAccountRepository;
import com.example.boardproject.service.UserAccountService;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserAccountService userAccountService) throws Exception {
        return http
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                .permitAll()
                                .mvcMatchers("/api/**").permitAll()
                                .mvcMatchers(
                                        HttpMethod.GET,
                                        "/",
                                        "/articles",
                                        "/articles/search-hashtag"
                                ).permitAll()
                                .anyRequest().authenticated()
                ).formLogin(Customizer.withDefaults())
                .logout(logoutConfig -> {
                    logoutConfig.logoutSuccessUrl("/");
                })
                .oauth2Login(oAuth2LoginConfig -> {
                    oAuth2LoginConfig.userInfoEndpoint(userInfoEndpointConfig -> {
                        userInfoEndpointConfig.userService(oAuth2UserService(userAccountService));
                    });
                })
                .csrf(csrf -> csrf.ignoringAntMatchers("/api/**"))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountService userAccountService) {
        return username -> userAccountService
                .searchUser(username)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(UserAccountService userAccountService) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String providerId = String.valueOf(kakaoResponse.id());
            String username = registrationId + "_" + providerId;
            String dummyPassword = this.passwordEncoder().encode("{bcrypt}" + UUID.randomUUID());

            return userAccountService.searchUser(username)
                    .map(BoardPrincipal::from)
                    .orElseGet(() ->
                            BoardPrincipal.from(
                                    userAccountService.saveUser(
                                            username,
                                            dummyPassword,
                                            kakaoResponse.email(),
                                            kakaoResponse.nickname(),
                                            null
                                    )
                            )
                    );
        };

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
