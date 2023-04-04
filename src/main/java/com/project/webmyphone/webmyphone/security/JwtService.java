package com.project.webmyphone.webmyphone.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt_secret_staff}")
    public String staff_key;

    @Value("${jwt_secret_user}")
    public String user_key;

    @Value("${jwt_expire_time_staff}")
    public String expire_time_staff;

    @Value("${jwt_expire_time_user}")
    public String expire_time_user;


    public String createTokenAdminStaff(String email) {
        String token = null;
        try {
            JWSSigner signer = new MACSigner(createAdminStaffSecret());
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim("Email", email);
            builder.expirationTime(createExpirationDateAdminStaff());
            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet);
            signedJWT.sign(signer);
            token = signedJWT.serialize();
        } catch (Exception e) {
            return null;
        }
        return token;
    }

    public String createTokenUser(String email) {
        String token = null;
        try {
            JWSSigner signer = new MACSigner(createUserSecret());
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim("Email", email);
            builder.expirationTime(createExpirationDateUser());
            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet);
            signedJWT.sign(signer);
            token = signedJWT.serialize();
        } catch (Exception e) {
            return null;
        }
        return token;
    }

    private JWTClaimsSet getClaimsFromToken(String token) {
        JWTClaimsSet claims = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifierAdminStaff = new MACVerifier(createAdminStaffSecret());
            JWSVerifier verifierUser = new MACVerifier(createUserSecret());
            if (signedJWT.verify(verifierAdminStaff)) {
                claims = signedJWT.getJWTClaimsSet();
            } else if (signedJWT.verify(verifierUser)) {
                claims = signedJWT.getJWTClaimsSet();
            }
        } catch (Exception e) {
            return null;
        }
        return claims;
    }

    private byte[] createAdminStaffSecret() {
        byte[] staff_secret = new byte[32];
        staff_secret = staff_key.getBytes();
        return staff_secret;
    }

    private byte[] createUserSecret() {
        byte[] user_secret = new byte[32];
        user_secret = user_key.getBytes();
        return user_secret;
    }

    private Date createExpirationDateAdminStaff() {
        return new Date(System.currentTimeMillis() + expire_time_staff);
    }

    private Date createExpirationDateUser() {
        return new Date(System.currentTimeMillis() + expire_time_user);
    }

    private Date getExpirationDateFromToken(String token) {
        Date expiration = null;
        JWTClaimsSet claims = getClaimsFromToken(token);
        expiration = claims.getExpirationTime();
        return expiration;
    }

    public String getEmailFromToken(String token) {
        String email = null;
        try {
            JWTClaimsSet claims = getClaimsFromToken(token);
            email = claims.getStringClaim("Email");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        if (expiration.before(new Date())) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean validateTokenLogin(String token) {
        if (token == null || token.trim().length() == 0) {
            return false;
        }
        String email = getEmailFromToken(token);
        if (email == null || email.isEmpty()) {
            return false;
        }
        if (isTokenExpired(token)) {
            return false;
        }
        return true;
    }
}
