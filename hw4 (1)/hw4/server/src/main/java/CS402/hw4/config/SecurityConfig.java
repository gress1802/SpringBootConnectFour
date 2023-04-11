package CS402.hw4.config;

import CS402.hw4.models.User;
import CS402.hw4.repositories.UserRepository;
import CS402.hw4.security.CustomAuthenticationProvider;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
                .requestMatchers("/api/v2/login-page").permitAll()
                .requestMatchers("/api/v2/login/success", "/api/v2/login/failure").permitAll()
                .requestMatchers("/api/v2/logout").permitAll()
                .requestMatchers("/api/v2/meta").permitAll()
                .requestMatchers("/api/v2/login").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginProcessingUrl("/api/v2/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpStatus.OK.value());
                    ObjectMapper objectMapper = new ObjectMapper();
                    String userJson = objectMapper.writeValueAsString(authentication.getPrincipal());
                    userJson = userJson.substring(1, userJson.length() - 1);
                    User newUserJson = userRepository.findByUsername(userJson);
                    String finalValue = objectMapper.writeValueAsString(newUserJson);
                    response.getWriter().write(finalValue);
                    //this is if the user has a successful login

                    HttpSession session = request.getSession();
                    session.setAttribute("user", newUserJson);
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    //this is if the user has a failed login
                })
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/api/v2/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .expiredUrl("/login");
        return http.build();
    }
}
