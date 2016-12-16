package com.ilashka.projects.crud;

import com.ilashka.projects.model.WallPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WallPostRepository extends MongoRepository<WallPost, Integer> {

}
