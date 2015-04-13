package com.tridion.camel;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.tridion.camel.configuration.MongoConfiguration;
import com.tridion.camel.configuration.S3Configuration;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.util.jndi.JndiContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Make this a background service (to manage its lifecycle)
 * Author:  Ray McDermott
 * Date:    18/07/2013
 */

public class CamelPlugin {

    private final org.slf4j.Logger log;
    private final CamelContext camelContext;
    private final JndiRegistry registry;

    @Autowired
    private S3Configuration s3Configuration;
    @Autowired
    private AmazonS3Client s3AmazonClient;
    @Autowired
    private MongoConfiguration mongoConfiguration;
    @Autowired
    private MongoClient mongodb;

    ProducerTemplate template;

    public CamelPlugin(S3Configuration s3Configuration, AmazonS3 s3AmazonClient, MongoConfiguration mongoConfiguration, Mongo mongodb) {
        try {
            log = LoggerFactory.getLogger(CamelPlugin.class);
            registry = new JndiRegistry(new JndiContext());
            camelContext = new DefaultCamelContext(registry);

            registry.bind("s3AmazonClient", s3AmazonClient);
            registry.bind("mongodb", mongodb);

            camelContext.addRoutes(new S3RouteBuilder(s3Configuration));
            camelContext.addRoutes(new MongoRouteBuilder(mongoConfiguration));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public class MongoRouteBuilder extends RouteBuilder {

        private String mongoUrl;

        public MongoRouteBuilder(MongoConfiguration mongoConfiguration) {
            this.mongoUrl = "mongodb?"
                    + "database=" + mongoConfiguration.getMongoDB()
                    + "&collection=" + mongoConfiguration.getMongoCollection();
        }

        @Override
        public void configure() throws Exception {
            from("direct:mongo")
                    //.unmarshal().json(JsonLibrary.Gson)
                    .setHeader(MongoDbConstants.OPERATION_HEADER, simple("${in.header.CamelMongoDbOperation}"))
                    .to("mongodb://" + mongoUrl);
            from("direct:mongoObject")
                    .setHeader(MongoDbConstants.OPERATION_HEADER, simple("${in.header.CamelMongoDbOperation}"))
                    .to("mongodb://" + mongoUrl);
        }
    }

    public class S3RouteBuilder extends RouteBuilder {

        private String s3Url;

        public S3RouteBuilder() {
            //TODO: do something useful
        }

        public S3RouteBuilder(S3Configuration s3Configuration) {
            this.s3Url =  s3Configuration.getAwsBucketName()
                    + "?amazonS3Client=#s3AmazonClient";
        }


        @Override
        public void configure() throws Exception {
            from("direct:awss3")
                    .setHeader(S3Constants.ACTION_TYPE, simple("${in.header.CamelAwsS3ActionType}"))
                    .setHeader(S3Constants.KEY, method(this, "stripFirstChar(${properties:aws.prefix}${in.header.CamelFileName})"))
                    //.setHeader(S3Constants.CONTENT_LENGTH, simple("${in.header.CamelAwsS3ContentLength}"))
                    .setHeader(S3Constants.CONTENT_TYPE, simple("${in.header.CamelAwsS3ContentType}"))
                    //.setHeader(S3Constants.CACHE_CONTROL, simple("${in.header.CamelFileLength}"))
                    .to("aws-s3://" + s3Url)
                    .log("ETAG for saved resource is " + simple("${out.header.CamelAwsS3ETag}"));
        }

        public String stripFirstChar(String relativePath) {
            try {
                if (relativePath.charAt(0) == '/')
                    return relativePath.substring(1);
                else
                    return relativePath;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}

