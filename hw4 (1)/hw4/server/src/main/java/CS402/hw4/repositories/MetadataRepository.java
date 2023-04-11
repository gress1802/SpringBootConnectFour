package CS402.hw4.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import CS402.hw4.models.Metadata;

@Repository
@EnableMongoRepositories
public interface MetadataRepository extends MongoRepository<Metadata, String> {
    
}
