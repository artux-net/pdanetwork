package net.artux.pdanetwork.configuration;

import net.artux.pdanetwork.models.user.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfiguration {

    private static final String[] MODERATOR_LIST = {
            "/utility/**"
    };

    private static final String[] TESTER_LIST = {
            "/utility/help",
            "/v3/api-docs/*",
            "/swagger-ui/**",
            "/actuator/**",
            "/webjars/**"
    };

    private static final String[] WHITE_LIST = {
            "/mailing/**",
            "/user/register",
            "/reset",
            "/reset/**",
            "/feed/**",
            "/enc/**",
            "/css/**",
            "/base/**",
            "/images/**",
            "/rules"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(MODERATOR_LIST).hasRole(Role.MODERATOR.name())
                        .requestMatchers(TESTER_LIST).hasRole(Role.TESTER.name())
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}