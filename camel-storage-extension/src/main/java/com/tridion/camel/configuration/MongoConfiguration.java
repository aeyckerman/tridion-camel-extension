package com.tridion.camel.configuration;

/**
 * Created with IntelliJ IDEA.
 * User: AEY5753
 * Date: 27/08/14
 * Time: 12:38
 * To change this template use File | Settings | File Templates.
 */
public class MongoConfiguration {

    private String mongoHost;
    private String mongoPort;
    private String mongoUser;
    private String mongoPassword;
    private String mongoDatabase;
    private String mongoCollection;

    public String getMongoHost() {
        return mongoHost;
    }

    public void setMongoHost(String mongoHost) {
        this.mongoHost = mongoHost;
    }

    public String getMongoUser() {
        return mongoUser;
    }

    public void setMongoUser(String mongoUser) {
        this.mongoUser = mongoUser;
    }

    public String getMongoPassword() {
        return mongoPassword;
    }

    public void setMongoPassword(String mongoPassword) {
        this.mongoPassword = mongoPassword;
    }

    public String getMongoDB() {
        return mongoDatabase;
    }

    public void setMongoDB(String mongoDB) {
        this.mongoDatabase = mongoDB;
    }

    public String getMongoCollection() {
        return mongoCollection;
    }

    public void setMongoCollection(String mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    public String getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(String mongoPort) {
        this.mongoPort = mongoPort;
    }
}
