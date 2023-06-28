package uk.gov.hmcts.reform.laubackend.cases.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.security.Security;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
            "/swagger-ui.html",
            "/webjars/springfox-swagger-ui/**",
            "/swagger-resources/**",
            "/health",
            "/health/liveness",
            "/health/readiness",
            "/v2/api-docs/**",
            "/info",
            "/favicon.ico",
            "/"
        );
    }

    @Bean
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) //NOSONAR not used in secure contexts
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .exceptionHandling(exc -> exc.authenticationEntryPoint(
                (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
            )
            .authorizeHttpRequests(
                authz -> authz.requestMatchers("/**").permitAll()
                    .requestMatchers("/lau/cases/s**")
                    .authenticated()
            );

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        return http.build();
    }
}
