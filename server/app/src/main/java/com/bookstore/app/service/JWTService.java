package com.bookstore.app.service;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

  private String secretKey = "9t4k+N1v8VzW9sz6Z+xY0Q7j36LmVmriYqvfrHDLncA=";

    public String generateToken(String username) {
      Map<String, Object> claims = new HashMap<>();

      return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .signWith(getKey())
        .compact();
    }

    public Key getKey() {
      byte[] bytes = Decoders.BASE64.decode(secretKey);
      return Keys.hmacShaKeyFor(bytes);
    }
}
