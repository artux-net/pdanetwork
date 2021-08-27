package net.artux.pdanetwork.configuration;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.service.MongoUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final MongoUserDetailService userDetailsService;

  private static final String[] ADMIN_LIST = {
          "/utility", "/swagger-ui/**"
  };

  private static final String[] WHITE_LIST = {
          "/", "/register", "/static/**", "/reset", "/reset/**", "/feed/**", "/enc/", "/enc/**",  "/enc/**/**"
  };

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(WHITE_LIST)
            .permitAll()
            .antMatchers(ADMIN_LIST).hasAnyAuthority("admin")
            .anyRequest().authenticated()
            .and().httpBasic()
            .and().sessionManagement().disable();
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