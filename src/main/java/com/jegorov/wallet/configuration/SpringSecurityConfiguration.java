package com.jegorov.wallet.configuration;

import com.jegorov.wallet.engine.provider.WalletAuthenticationProvider;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final WalletAuthenticationProvider authProvider;

  public SpringSecurityConfiguration(
      WalletAuthenticationProvider authProvider) {
    this.authProvider = authProvider;
  }

  @Autowired
  public void configAuthentication(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(authProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.
        cors().and().
        csrf().disable().
        formLogin().disable().
        httpBasic().
        and().authorizeRequests().antMatchers("/login").permitAll().anyRequest().authenticated();
  }


  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("HEAD",
        "GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowCredentials(true);
    configuration
        .setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
