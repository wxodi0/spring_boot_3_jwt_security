package com.sample.login.controller;

import com.sample.login.dto.LoginDto;
import com.sample.login.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class UserController {

  private final UserService userservice;

  @PostMapping("/User")
  public Long singUp(@RequestBody LoginDto dto) {
    return userservice.save(dto);
  }

  @GetMapping("/logout")
  public String logout(
    HttpServletRequest req,
    HttpServletResponse res
  ) {
    new SecurityContextLogoutHandler().logout(
      req,
      res,
      SecurityContextHolder.getContext().getAuthentication()
    );

    return "로그아웃 성공";
  }

}
