package com.cookbook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "counters")
public class DatabaseSequence {
    @Id
    private String id;   // e.g. "accountId"
    private long seq;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public long getSeq() { return seq; }
    public void setSeq(long seq) { this.seq = seq; }
}