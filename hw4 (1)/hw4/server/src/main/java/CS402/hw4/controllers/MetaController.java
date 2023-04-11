package CS402.hw4.controllers;

import CS402.hw4.models.Metadata;
import CS402.hw4.models.Theme;
import CS402.hw4.models.Token;
import CS402.hw4.models.User;
import CS402.hw4.repositories.UserRepository;
import CS402.hw4.services.ThemeService;
import CS402.hw4.services.TokenService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class MetaController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ThemeService defService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/meta")
    public ResponseEntity<Metadata> getMetadata() {
        try {
            List<Token> tokens = tokenService.getAllTokens();
            System.out.println(tokens);
            Theme defaultTheme = defService.getDefaultTheme();
            defaultTheme.setComputerToken(defaultTheme.getComputerToken());
            defaultTheme.setPlayerToken(defaultTheme.getPlayerToken());
            Metadata metadata = new Metadata.Builder()
                    .tokens(tokens)
                    .defaultTheme(defaultTheme)
                    .build();
            return new ResponseEntity<>(metadata, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Token> findToken(String id) {
        return tokenService.findToken(id);
    }

    public User getAuthenticatedUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
