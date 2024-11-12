package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import rs.ac.uns.ftn.informatika.jpa.authentification.JwtAuthentificationFilter;
import rs.ac.uns.ftn.informatika.jpa.service.CustomUserDetailsService;
import rs.ac.uns.ftn.informatika.jpa.token.Token;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public CustomUserDetailsService customUserDetailsService;

    @Autowired
    private final Token token;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, Token token) {
        this.customUserDetailsService = customUserDetailsService;
        this.token = token;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable() // Isključuje CSRF zaštitu
                .authorizeRequests()
                .antMatchers("/api/users/register", "/api/users/verify", "/api/users/login", "/register",
                        "/v3/api-docs", "/v3/api-docs/swagger-config", "/swagger-resources/**", "/swagger-ui.html",
                        "/webjars/**", "/swagger-ui/**", "/api/posts", "/api/users/profile/**",
                        "/api/posts/allPaged", "/api/posts/{{userId}}", "/api/posts/all", "/api/posts/1",
                        "/api/users/all", "/api/post/comment/1/1", "/api/posts/like/1/3", "/api/posts/unlike/1/3",
                        "/api/post/comment/2", "/api/posts/upload-image", "/api/posts/upload/string",
                        "/api/posts/images/1731356446387-zeka1.jpg")
                .permitAll()
                .antMatchers("/api/posts/like/**","/api/posts/unlike/**").authenticated()
                .and()
                .rememberMe()
                .key("papi") // Postavite jedinstveni ključ za vašu aplikaciju
                .tokenValiditySeconds(1209600) // 14 dana u sekundama
                .and()
                .addFilterBefore(new JwtAuthentificationFilter(token, customUserDetailsService),
                        BasicAuthenticationFilter.class);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
