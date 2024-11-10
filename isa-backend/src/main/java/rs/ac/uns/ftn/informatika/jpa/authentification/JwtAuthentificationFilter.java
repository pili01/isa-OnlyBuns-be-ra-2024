package rs.ac.uns.ftn.informatika.jpa.authentification;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.ac.uns.ftn.informatika.jpa.token.Token;
import rs.ac.uns.ftn.informatika.jpa.service.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    @Autowired
    private Token jwtToken;

    @Autowired
    private UserDetailsService userDetailsService;

    public JwtAuthentificationFilter(Token token, UserDetailsService userDetailsService) {
        this.jwtToken = token;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        try {
            if (jwt != null && jwtToken.validateJwtToken(jwt)) {
                String username = jwtToken.getUsernameFromJwtToken(jwt);

                if (username != null) {
                   // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UserDetails userDetails = new User(username, "88888888", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));


                    if (jwtToken.validateJwtToken(jwt)) {
                        // Kreiranje instance TokenBasedAuthentication sa korisničkim podacima
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(jwt); // Postavljanje JWT tokena
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (ExpiredJwtException ex) {
            System.out.println("Token expired!");
        }

        // Prosleđivanje zahteva dalje u sledeći filter
        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
