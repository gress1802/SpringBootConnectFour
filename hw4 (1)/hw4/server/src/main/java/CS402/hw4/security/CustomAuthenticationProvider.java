package CS402.hw4.security;

import CS402.hw4.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails user = null;
        try { 
            user = userDetailsService.loadUserByUsername(username);
            System.out.println("\nuser: \n" + user);
        } catch (UsernameNotFoundException e) {
            System.out.println("\nuser wansnt found properly\n");
            throw new BadCredentialsException("Invalid Username or Password");
        }
        if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            System.out.println("username: " + username + "\npassword: " + password + "\nuser: " + user);
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        }
        System.out.println("CustomAuthenticationProvider\n\n\n");
        throw new BadCredentialsException("Invalid Username or Password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

