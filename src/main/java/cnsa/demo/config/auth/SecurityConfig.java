package cnsa.demo.config.auth;

import cnsa.demo.service.CustomOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuthService customOAuthService;
    private static final String[] AUTH_WHITELIST = {
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((auth) -> auth.disable())
                .headers((httpSecurityHeadersConfigurer) -> httpSecurityHeadersConfigurer.frameOptions(
                        frameOptionsConfig -> frameOptionsConfig.disable()
                ))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AUTH_WHITELIST)
                        .permitAll()
                        .anyRequest().authenticated()

                )
                .logout(
                        (httpSecurityLogoutConfigurer) -> httpSecurityLogoutConfigurer.logoutSuccessUrl("/")
                )
                .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}
