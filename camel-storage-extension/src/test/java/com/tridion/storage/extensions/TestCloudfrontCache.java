package com.tridion.storage.extensions;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.cloudfront.model.CreateInvalidationRequest;
import com.amazonaws.services.cloudfront.model.CreateInvalidationResult;
import com.amazonaws.services.cloudfront.model.InvalidationBatch;
import com.amazonaws.services.cloudfront.model.Paths;
import com.tridion.camel.configuration.S3Configuration;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: AEY5753
 * Date: 22/01/15
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class TestCloudfrontCache {
    @Test
    public void clearCache() {


        S3Configuration s3Configuration = new S3Configuration();
        s3Configuration.setAwsAccessKey("AKIAIA5QECAXRRU2ADBQ");
        s3Configuration.setAwsSecretKey("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");
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProxyHost("10.101.110.47");
        clientConfiguration.setProxyPort(3128);

        AmazonCloudFrontClient cloudFrontClient = new AmazonCloudFrontClient(new BasicAWSCredentials("AKIAIA5QECAXRRU2ADBQ","tHGsimOYXkvEwv2iVcyYnSw6FBwRNm3AAl0mME37"),clientConfiguration);
        //AmazonCloudFrontClient cloudFrontClient = new AmazonCloudFrontClient(s3Configuration);



        String distributionId = "ES32CB9TV7DB7";
        Paths paths = new Paths();
        InvalidationBatch invalidationBatch = new InvalidationBatch(paths, "Tridiondeployer");
        CreateInvalidationRequest request = new CreateInvalidationRequest(distributionId, invalidationBatch);
        cloudFrontClient.createInvalidation(request);

        CreateInvalidationResult result = cloudFrontClient.createInvalidation(request);
        System.out.println(result.toString());



    }
}
