package com.sample.auth.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
//Beanfactory를 상속한 스프링 컨테이너(ApplicationContext)에 스프링 Bean 등록
//@Service, @repository
@Component
//yml에 있는 jwt 가져오기
@ConfigurationProperties("jwt")
public class JwtProperties {
  private String issuer;
  private String key;
}
