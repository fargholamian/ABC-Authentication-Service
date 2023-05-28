package com.tradedoubler.authenticationservice.config;

import com.tradedoubler.authenticationservice.SecurityAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

  private SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;
  private AuthenticationFilter authenticationFilter;

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
      throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);
    return authenticationManagerBuilder.build();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // No security check for login and register endpoints
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**")
            .permitAll()
            .anyRequest()
            .authenticated()
        );

    // Providing a Security Authentication Entry Point to handle all authentication exceptions.
    http
        .exceptionHandling(conf ->
            conf.authenticationEntryPoint(securityAuthenticationEntryPoint)
        )
        .sessionManagement(conf ->
            conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

    // Security check for other endpoints e.g. get user info
    http
        .addFilterBefore(authenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        .headers(headersConfigurer -> headersConfigurer
            .cacheControl(HeadersConfigurer.CacheControlConfig::disable));

    http
        .requestCache(RequestCacheConfigurer::disable);


    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
