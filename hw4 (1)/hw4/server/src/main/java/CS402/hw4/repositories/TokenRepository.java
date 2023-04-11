package CS402.hw4.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import CS402.hw4.models.Token;

@Repository
@EnableMongoRepositories
public interface TokenRepository extends MongoRepository<Token, String>{

    public List<Token> findAll();
    public Optional<Token> findById(String id);
    public Optional<Token> findByName(String name);
    
}
