package CS402.hw4.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CS402.hw4.models.Token;
import CS402.hw4.repositories.TokenRepository;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public List<Token> getAllTokens() {
        return tokenRepository.findAll();
    }

    public Optional<Token> findToken(String id) {
        return tokenRepository.findById(id);
    }
}
