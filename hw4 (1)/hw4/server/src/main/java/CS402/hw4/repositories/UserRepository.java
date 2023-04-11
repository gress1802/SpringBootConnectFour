package CS402.hw4.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import CS402.hw4.models.User;

@Repository
@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User, String> {
    //query derivation is possible with method names that follow the convention findBy<field name>
    User findByUsername(String username);
    User findByid(String id);
}
