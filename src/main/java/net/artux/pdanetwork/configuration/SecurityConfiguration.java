package net.artux.pdanetwork.configuration;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.enums.Role;
import net.artux.pdanetwork.service.user.UserDetailService;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailService userDetailsService;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(WHITE_LIST)
                .permitAll()
                .antMatchers(MODERATOR_LIST).hasAuthority(Role.MODERATOR.name())
                .antMatchers(TESTER_LIST).hasAuthority(Role.TESTER.name())
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().disable()
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder)
            throws Exception {
        builder.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}