package com.sample.auth.provider;

import com.sample.auth.config.jwt.JwtProperties;
import com.sample.login.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class JwtProvider {

  private final JwtProperties jwtProperties;

  public String generateToken(User user, Duration expiredAt) {
    Date now = new Date();
    return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
  }

  //토큰 생성
  public String makeToken(Date expiry, User user) {
    Date now = new Date();

    return Jwts.builder()
      .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
      //내용 iss: wxodi0@gmail.com
      .setIssuer(jwtProperties.getIssuer())
      .setIssuedAt(now) //내용 iat: 현재시간
      .setExpiration(expiry) //내용 exp: expiry 맴버 변수값
      .setSubject(user.getEmail()) //내용 sub: 유저 이메일
      .claim("id", user.getId()) // 클레임 id: 유저 id
      //비밀값과 함께 해시값을 HS256 방식으로 암호화
      .signWith(SignatureAlgorithm.HS256, jwtProperties.getKey())
      .compact();
  }

  //토큰 검증
  //복호화를 진행하여 에러로 유효한지 검사한다.
  public boolean validToken(String token) {
    try{
      Jwts.parser()
        .setSigningKey(jwtProperties.getKey()) //복호화
        .parseClaimsJws(token);
      return true;
    } catch (Exception e) { //복호화에서 에러 발생
      return false;
    }
  }

  //토큰으로 사용자 정보(claims)를 받아 authentication을 반환한다.
  public Authentication getAuth(String token) {
    Claims claims = getClaims(token);
    Set<SimpleGrantedAuthority> authorities = Collections.singleton(
      new SimpleGrantedAuthority("ROLE_USER")
    );

    return new UsernamePasswordAuthenticationToken(
      new org.springframework.security.core.userdetails.User(
        claims.getSubject(),
        "",
        authorities
      ),
      token,
      authorities
    );
  }

  public Long getUserId(String token) {
    Claims claims = getClaims(token);
    return claims.get("id", Long.class);
  }

  public Claims getClaims(String token) {
    return Jwts.parser()
      .setSigningKey(jwtProperties.getKey())
      //parseClaimsJwt가 아닌 jws를 사용하는 이유
      //사용 하고 있는 것은 서버의 key로 서명한 것을 토큰화 한 JWS인 것을 인지하고 있어야 한다.
      //그렇기에 UnsupportedJwtException 발생하지 않게 하기 위해서
      .parseClaimsJws(token)
      .getBody();
  }
}
