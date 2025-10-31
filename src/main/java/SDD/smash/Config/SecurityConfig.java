package SDD.smash.Config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Value("${front_url}")
    private String[] frontUrl;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated() //기본 거부 정책 적용
                );


        /**
         * csrf 보호 해제
         * */
        http
                .csrf((csrf) -> csrf.disable());


        /**
         * cors 관련 설정
         * */
        http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration config = new CorsConfiguration();

                                List<String> allowed = Arrays.asList(frontUrl);
                                config.setAllowedOrigins(allowed);
                                config.setAllowedMethods(singletonList("*")); // 허용할 메소드 Get ect on
                                config.setAllowCredentials(true);
                                config.setAllowedHeaders(singletonList("*"));
                                config.setMaxAge(3600L);

                                config.setExposedHeaders(singletonList("Authorization"));

                                return config;
                            }
                        }));
        http
                .formLogin((formLogin) -> formLogin.disable());

        return http.build();
    }
}
