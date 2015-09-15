package com.tridion.storage.extensions;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.cloudfront.model.*;
import com.tridion.deployer.Deployer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by AEY5753 on 26/05/2015.
 */
public class UnitS3Test {

/*    @Autowired
    private S3Configuration s3Configuration;*/
    private Deployer deployer = Deployer.getInstance();
    private String workdir = "C:\\Data\\tridion\\poc-incoming\\";

    @Before
    public void setUp() throws Exception {
        deployer.start();
    }

    @After
    public void tearDown() throws Exception {
        deployer.stop();
    }

    @Test
    public void publishToS3() throws IOException {
        URL url = getClass().getResource("/test-files/zip/tcm_0-155452-66560.Content.zip");
        File sourceFile = new File(url.getPath());

        File destinationFile = new File(workdir + sourceFile.getName());
        File successFile = new File(workdir + "Success\\" + sourceFile.getName());

        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath());
            System.out.println("File copied, check deployer");
            Thread.sleep(500000);
            System.out.println("Waiting is over, something happened ?");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(successFile.exists());
    }

    @Test
    public void unpublishFromS3() throws IOException {
        URL url = getClass().getResource("/test-files/zip/tcm_0-155452-66560.Content.zip");
        File sourceFile = new File(url.getPath());

        File destinationFile = new File(workdir + sourceFile.getName());
        File successFile = new File(workdir + "Success\\" + sourceFile.getName());

        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath());
            System.out.println("File copied, check deployer");
            Thread.sleep(150000);
            System.out.println("Waiting is over, something happened ?");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(successFile.exists());
    }


    @Test
    public void clearCache() throws IOException {

        String prefix = "/toyotaone";
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProxyHost("10.101.110.11");
        clientConfiguration.setProxyPort(3128);

        try {

            AmazonCloudFrontClient cloudFrontClient = new AmazonCloudFrontClient(new BasicAWSCredentials("AKIAIA5QECAXRRU2ADBQ", "tHGsimOYXkvEwv2iVcyYnSw6FBwRNm3AAl0mME37"), clientConfiguration);
            //AmazonCloudFrontClient cloudFrontClient = new AmazonCloudFrontClient(s3Configuration);

            GetDistributionConfigRequest getDistributionConfigRequest = new GetDistributionConfigRequest();

            //GetDistributionConfigResult distributionConfig = cloudFrontClient.getDistribution(getDistributionConfigRequest);

            String distributionId = "ES32CB9TV7DB7";
            Paths paths = new Paths();

            Collection<String> items = new ArrayList<String>();
            items.add(prefix + "/euen/Movies/*");

            paths.setItems(items);
            paths.setQuantity(1);
            InvalidationBatch invalidationBatch = new InvalidationBatch(paths, "Tridiondeployer");
            CreateInvalidationRequest request = new CreateInvalidationRequest(distributionId, invalidationBatch);
            cloudFrontClient.createInvalidation(request);

            CreateInvalidationResult result = cloudFrontClient.createInvalidation(request);
            System.out.println(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
