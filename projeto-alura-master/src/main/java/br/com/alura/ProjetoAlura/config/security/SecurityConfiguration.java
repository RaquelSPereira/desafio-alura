package br.com.alura.ProjetoAlura.config.security;

import br.com.alura.ProjetoAlura.adapters.auth.UserAuthenticationFilterAdapter;
import br.com.alura.ProjetoAlura.util.exceptions.CustomAccessDeniedHandler;
import br.com.alura.ProjetoAlura.util.exceptions.CustomAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ObjectMapper objectMapper;

    @Autowired
    private UserAuthenticationFilterAdapter userAuthenticationFilter;

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/login",
            "/user/newStudent"
    };

    public static final String [] ENDPOINTS_INSTRUCTOR = {
            "/course/new",
            "/user/newInstructor",
            "/course/inactive/**"
    };

    public static final String [] ENDPOINTS_ANY_ROLE = {
            "/registration/report"
    };

    public static final String [] ENDPOINTS_STUDENT = {
            "/registration/new"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers((headers) ->
                        headers
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                    .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
                                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
                                )
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .requestMatchers(ENDPOINTS_INSTRUCTOR).hasRole("INSTRUCTOR")
                        .requestMatchers(ENDPOINTS_STUDENT).hasRole("STUDENT")
                        .requestMatchers(ENDPOINTS_ANY_ROLE).hasAnyRole("STUDENT", "INSTRUCTOR")
                );

        http.addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
