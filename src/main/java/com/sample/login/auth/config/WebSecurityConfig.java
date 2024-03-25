package com.sample.login.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

//di를 자동으로 해줌 <- lombok 기능
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

  private final UserDetailsService userService;
  
  //시큐리티 비활성화
  @Bean
  public WebSecurityCustomizer configure() {
    return (web) -> web.ignoring()
      .requestMatchers(toH2Console())
      .requestMatchers("/static/**");
  }

  //특정 HTTP 요청에 대한 웹 기반 보안 구성
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests((authorizeRequests) ->
        authorizeRequests
          .requestMatchers(toH2Console())
          .permitAll()
          .requestMatchers("/", "/login/**")
          .permitAll()
          .anyRequest().authenticated()
      )

      //헤더 설정
      .headers((headerConfig) ->
        headerConfig.frameOptions(
          HeadersConfigurer.FrameOptionsConfig::disable
        )
      )

      //scrf 비활성화
      .csrf(AbstractHttpConfigurer::disable)

      //로그인 폼 설정
      .formLogin((login) ->
        login.loginPage("/login")
          .defaultSuccessUrl("/articles")
      )

      //로그아웃 폼 설정
      .logout((logout) ->
        logout.logoutSuccessUrl("/login")
          .invalidateHttpSession(true)
      )
      .build();
  }
  
  //인증 관리자 관련 설정
  @Bean
  public AuthenticationManager authenticationManager(
    HttpSecurity http,
    BCryptPasswordEncoder bCryptPasswordEncoder
  ) throws Exception {
    return http
      .getSharedObject(AuthenticationManagerBuilder.class)
      .userDetailsService(userService)
      .passwordEncoder(bCryptPasswordEncoder)
      .and()
      .build();
  }

  //패스워드 인코더로 사용할 빈 등록
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
