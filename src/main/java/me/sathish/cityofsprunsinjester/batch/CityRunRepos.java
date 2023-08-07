package me.sathish.cityofsprunsinjester.batch;

import me.sathish.cityofsprunsinjester.data.RunsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

public interface CityRunRepos extends MongoRepository<RunsData, String> {
    @Autowired
    MongoTemplate mongoTemplate;
}
