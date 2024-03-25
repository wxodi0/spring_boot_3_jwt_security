package com.sample.login.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")

//파라미터가 없는 디폴드 생성자를 생성해줌
//아무런 매개변수가 없는 생성자를 생성하되 다른 패키지에 소속된 클래스는 접근을 불허한다
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails { //UserDetails를 상속받아 인증 객체로 사용

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Builder
  public User(String email, String password, String auth) {
    this.email = email;
    this.password = password;
  }

  //권한 반환
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("User"));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }
  
  //return ture == 만료되지 않음 / 가능

  //계정 만료 여부
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  
  //계정 잠금 여부
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  //Credentials(비번) 만료 여부
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
  
  //계정 사용 여부
  @Override
  public boolean isEnabled() {
    return true;
  }
}
