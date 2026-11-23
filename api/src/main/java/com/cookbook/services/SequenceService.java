package com.cookbook.services;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cookbook.model.DatabaseSequence;

@Service
public class SequenceService {

    private final MongoTemplate mongoTemplate;

    public SequenceService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public long nextAccountId() {
        Query query = new Query(Criteria.where("_id").is("accountId"));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true)      // get the post-increment value
                .upsert(true);        // create it on first call

        DatabaseSequence counter = mongoTemplate.findAndModify(
                query, update, options, DatabaseSequence.class);

        return counter.getSeq();
    }
}