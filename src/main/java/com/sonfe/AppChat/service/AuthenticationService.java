package com.sonfe.AppChat.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sonfe.AppChat.dto.request.AuthenticationRequest;
import com.sonfe.AppChat.dto.request.IntrospectRequest;
import com.sonfe.AppChat.dto.response.AuthenticationResponse;
import com.sonfe.AppChat.dto.response.IntrospectResponse;
import com.sonfe.AppChat.entity.User;
import com.sonfe.AppChat.exception.AppException;
import com.sonfe.AppChat.exception.ErrorCode;
import com.sonfe.AppChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationService {

    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Autowired
    UserRepository userRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated =  passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        String token = generateToken(user);

        return AuthenticationResponse.builder().authenticated(true).token(token).build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        String token = request.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean verified = signedJWT.verify(jwsVerifier);
        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return IntrospectResponse.builder().valid(verified && expityTime.after(new Date())).build();
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("sonFe")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .claim("name", user.getName())
                .claim("scope", user.getRole())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader ,payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
