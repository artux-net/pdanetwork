package net.artux.pdanetwork.configuration;

import net.artux.pdanetwork.models.user.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
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
            "/api/v1/user/register",
            "/api/v1/user/reset/pass",
            "/mailing/**",
            "/confirmation/register",
            "/reset",
            "/reset/**",
            "/feed/**",
            "/enc/**",
            "/css/**",
            "/base/**",
            "/images/**",
            "/rules"
    };

    private final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    private String roleHierarchy;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(MODERATOR_LIST).hasAnyRole(Role.ADMIN.name(), Role.MODERATOR.name())
                        .requestMatchers(TESTER_LIST).hasAnyRole(Role.ADMIN.name(), Role.MODERATOR.name(), Role.TESTER.name())
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin().disable()
                .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        List<Role> roles = Arrays.stream(Role.values())
                .collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();
        String prefix = "ROLE_";

        for (int i = 1; i < roles.size(); i++) {
            builder.append(prefix)
                    .append(roles.get(i - 1).name());

            Role nextRole = roles.get(i);
            builder.append(" > ")
                    .append(prefix)
                    .append(nextRole.name())
                    .append("\n");

        }

        String hierarchy = builder.toString();
        logger.info("Role hierarchy: \n" + hierarchy);
        this.roleHierarchy = hierarchy;
        roleHierarchy.setHierarchy(this.roleHierarchy);
        return roleHierarchy;
    }

    public String getRoleHierarchy() {
        return roleHierarchy;
    }
}