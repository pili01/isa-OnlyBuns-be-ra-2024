package rs.ac.uns.ftn.informatika.jpa.token;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;

import java.util.Date;

@Component
public class Token {
    private final String SECRET_KEY = "papi";

    @Autowired
    private UserService userService;

    public String generateToken(String email,String username) {
        return Jwts.builder()
                .setSubject(email)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*3))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }


    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }



    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {

        }
        return false;
    }




}