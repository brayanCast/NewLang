package com.newlang.backend.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SecretJwk;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    //Metodo para crear el token por medio de la autenticacion
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date actualTime = new Date();
        Date expiracionToken = new Date(actualTime.getTime() + ConstantSecurity.JWT_EXPIRATION_TOKEN);

        //Linea para generar el token
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512, ConstantSecurity.JWT_FIRMA)
                .compact();
        return token;
    }

    //Extraer el username a partir de un token
    public String obtenerUsernameDeJwt(String token){
        Claims claims = Jwts.parser() //Metodo parser que se utiliza con el fin de analizar el token
                .setSigningKey(ConstantSecurity.JWT_FIRMA) //Establece la clavde de la firma que se utiliza para validar la firma del token
                .build().parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    //Metodo para validar el token
    public boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(ConstantSecurity.JWT_FIRMA).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Jwt ha expirado o es incorrecto");
        }
    }
}
