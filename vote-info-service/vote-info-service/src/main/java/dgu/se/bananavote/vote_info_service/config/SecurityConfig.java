package dgu.se.bananavote.vote_info_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청 허용
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
                .formLogin(formLogin -> formLogin.disable()); // 폼 로그인 비활성화
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 Origin (와일드카드 사용 가능)
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 필요한 Origin 설정
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name())); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*")); // 허용할 요청 헤더
        configuration.setExposedHeaders(List.of("Authorization")); // 응답 헤더 노출
        configuration.setAllowCredentials(true); // 인증 정보 포함 허용

        // 모든 경로에 대해 CORS 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
