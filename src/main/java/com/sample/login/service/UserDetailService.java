package com.sample.login.service;
import com.sample.login.domain.User;
import com.sample.login.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailService implements UserDetailsService {
  private UserRepository userRepository;

  //로그인 할때 이메일로 사용자 정보를 가져오는 api
  @Override
  public UserDetails loadUserByUsername(String email) {
    return userRepository.findByEmail(email).orElseThrow(() ->
      new IllegalArgumentException("사용자를 찾을 수 없습니다.")
    );
  }
}
