package CS402.hw4.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import CS402.hw4.models.Game;

@Repository
@EnableMongoRepositories
public interface GameRepository extends MongoRepository<Game, String> {
    public List<Game> getGamesBySid(String sid);
    public Game getGameBySidAndId(String sid, String id);
}
