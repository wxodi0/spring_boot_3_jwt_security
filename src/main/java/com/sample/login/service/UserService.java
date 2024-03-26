package com.sample.login.service;

import com.sample.login.domain.User;
import com.sample.login.dto.LoginDto;
import com.sample.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public Long save(LoginDto dto) {

    if(userRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
    }

    return userRepository.save(User.builder()
      .email(dto.getEmail())
      .password(bCryptPasswordEncoder.encode(dto.getPassword()))
      .build()
    ).getId();
  }
}
