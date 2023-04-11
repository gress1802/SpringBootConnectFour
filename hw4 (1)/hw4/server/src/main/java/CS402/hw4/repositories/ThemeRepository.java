package CS402.hw4.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import CS402.hw4.models.Theme;

@Repository
@EnableMongoRepositories
public interface ThemeRepository extends MongoRepository<Theme, String> {
    Theme findFirstBy();
}
