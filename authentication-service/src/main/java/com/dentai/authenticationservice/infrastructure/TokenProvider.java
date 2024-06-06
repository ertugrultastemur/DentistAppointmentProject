package com.dentai.authenticationservice.infrastructure;

import com.dentai.authenticationservice.client.UserManagementServiceClient;
import com.dentai.authenticationservice.dto.TokenRequest;
import com.dentai.authenticationservice.dto.UserDto;
import com.dentai.authenticationservice.dto.ValidationResponseDto;
import com.dentai.authenticationservice.exceptions.TokenNotValidException;
import com.dentai.authenticationservice.exceptions.UnsignedJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class TokenProvider {

    Logger logger = org.slf4j.LoggerFactory.getLogger(TokenProvider.class);
    
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1 hours
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2;  // 2 hours
    private final Key key;

    private final UserManagementServiceClient userManagementServiceClient;


    public TokenProvider(@Value("${jwt.secret}") String secretKey, UserManagementServiceClient userManagementServiceClient) {
        logger.trace("TokenProvider: constructor started with secret key | {}", secretKey);
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.userManagementServiceClient = userManagementServiceClient;
        logger.trace("TokenProvider: constructor finished.");
    }

    public UsernamePasswordAuthenticationToken refreshTokenAuthentication(TokenRequest tokenRequest , UserDto userDto) {
        logger.trace("TokenProvider refreshToken: started.");

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(tokenRequest.getRefreshToken())
                    .getBody();



            return new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

        } catch (SignatureException e) {
            logger.error("TokenProvider refreshToken: SignatureException | {}", e.getMessage());
            throw new UnsignedJwtException( e.getMessage() );
        }
    }

    public TokenRequest generateTokenDto(Authentication authentication) {
        logger.trace("TokenProvider generateTokenDto: started.");

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        logger.info("TokenProvider generateTokenDto: roles has been collected.");
        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        logger.info("TokenProvider generateTokenDto: accessTokenExpiresIn has been appointed.");
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, roles)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        logger.info("TokenProvider generateTokenDto: accessToken has been created.");
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME * 2))
                .setSubject(authentication.getName())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        logger.info("TokenProvider generateTokenDto: refreshToken has been created.");
        logger.trace("TokenProvider generateTokenDto finished.");
        return TokenRequest.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public TokenRequest generateRefreshTokenDto(Authentication authentication, String token) {
        logger.trace("TokenProvider generateTokenDto: started.");
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        logger.info("TokenProvider generateTokenDto: roles has been collected.");
        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        logger.info("TokenProvider generateTokenDto: accessTokenExpiresIn has been appointed.");
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, roles)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        logger.info("TokenProvider generateTokenDto: accessToken has been created.");
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(claims.getExpiration().getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .setSubject(authentication.getName())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        logger.info("TokenProvider generateTokenDto: refreshToken has been created.");
        logger.trace("TokenProvider generateTokenDto finished.");
        return TokenRequest.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        logger.trace("TokenProvider: getAuthentication started.");
        Claims claims = parseClaims(accessToken);
        logger.info("TokenProvider getAuthentication: claims are parsed.");

        if (claims.get(AUTHORITIES_KEY) == null) {
            logger.error("TokenProvider getAuthentication: There's no authority info.");
            throw new TokenNotValidException("There's no authority info.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        logger.info("TokenProvider getAuthentication: authorities are parsed.");

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        logger.info("TokenProvider getAuthentication: principal is created.");
        logger.trace("TokenProvider: getAuthentication finished.");
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public ValidationResponseDto validateRefreshToken(String token) {
        logger.trace("TokenProvider: validateToken started.");
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("TokenProvider validateToken: claims are parsed.");
            UserDto userDto =userManagementServiceClient.getUserByEmail(claims.getSubject()).getBody();
            logger.info("TokenProvider validateToken: userDto is parsed.");

            claims.get("auth");
            logger.info("TokenProvider validateToken: auth is get.");
            logger.trace("TokenProvider: validateToken finished.");
            return new ValidationResponseDto(Collections.emptyList(), userDto.getId(), true);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.error("TokenProvider validateToken: Wrong JWT signature.");
        } catch (ExpiredJwtException e) {
            logger.error("TokenProvider validateToken: It is an expired JWT token.");
        } catch (UnsupportedJwtException e) {
            logger.error("TokenProvider validateToken: It is an unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            logger.error("TokenProvider validateToken: There's something wrong with JWT token.");
        }
        logger.error("TokenProvider validateToken: Token is not valid.");
        return new ValidationResponseDto(Collections.emptyList(), null, false);
    }

    public ValidationResponseDto validateToken(String token) throws TokenNotValidException{
        logger.trace("TokenProvider: validateToken started.");
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("TokenProvider validateToken: claims are parsed.");
            UserDto userDto =userManagementServiceClient.getUserByEmail(claims.getSubject()).getBody();
            logger.info("TokenProvider validateToken: userDto is parsed.");

            claims.get("auth");
            logger.info("TokenProvider validateToken: auth is get.");
            logger.trace("TokenProvider: validateToken finished.");
            return new ValidationResponseDto(Arrays.stream(claims.get("auth").toString().split(",")).toList(), userDto.getId(), true);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.error("TokenProvider validateToken: Wrong JWT signature.");
        } catch (ExpiredJwtException e) {
            logger.error("TokenProvider validateToken: It is an expired JWT token.");
        } catch (UnsupportedJwtException e) {
            logger.error("TokenProvider validateToken: It is an unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            logger.error("TokenProvider validateToken: There's something wrong with JWT token.");
        }
        logger.error("TokenProvider validateToken: Token is not valid.");
        return new ValidationResponseDto(Collections.emptyList(), null, false);
    }

    private Claims parseClaims(String accessToken) {
        logger.trace("TokenProvider: parseClaims started.");
        try {
            logger.info("TokenProvider parseClaims: accessToken is parsed.");
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("TokenProvider parseClaims: Token is expired.");
            return e.getClaims();
        }catch (SignatureException e) {
            logger.error("TokenProvider parseClaims: JWT signature does not match locally computed signature.");
            throw new UnsignedJwtException("JWT signature does not match locally computed signature.");
        }
    }
}
