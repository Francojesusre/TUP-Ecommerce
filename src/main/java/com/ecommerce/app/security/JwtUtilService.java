package com.ecommerce.app.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUtilService {
  private static final String JWT_SECRET_KEY = "TExBVkVfTVVZX1NFQ1JFVEE=";

  public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * (long) 8; // 8 Horas

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(extractAllClaims(token));
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
  }

  public String generateToken(UserDetails user) {
    Claims claims = Jwts.claims().setSubject(user.getUsername());
    var rol = user.getAuthorities().stream().collect(Collectors.toList()).get(0);
    claims.put("role", rol);
    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
            .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
            .compact();
  }


  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (Exception e) { }
    return false;
  }

  public String getToken (HttpServletRequest httpServletRequest) {
    final String bearerToken = httpServletRequest.getHeader("Authorization");
    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
    {return bearerToken.substring(7,bearerToken.length()); }
    return null;
  }


}
