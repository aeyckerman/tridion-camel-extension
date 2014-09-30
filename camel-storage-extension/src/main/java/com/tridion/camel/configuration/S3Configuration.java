package com.tridion.camel.configuration;

/**
 * Created with IntelliJ IDEA.
 * User: AEY5753
 * Date: 18/08/14
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class S3Configuration {


    private String awsBucketName;
    private String awsAccessKey;
    private String awsSecretKey;
    private String awsRegion;

/*    private AWSCredentials awsCredentials;
    private ClientConfiguration clientConfiguration;
    private AmazonS3 client;*/

    public void S3Configuration() {
        //System.out.println("Init config");
  /*      awsCredentials = new BasicAWSCredentials(this.awsAccessKey, this.awsSecretKey);

        clientConfiguration = new ClientConfiguration();

        client = new AmazonS3Client(awsCredentials, clientConfiguration);
*/
    }


    public String getAwsBucketName() {
        return awsBucketName;
    }

    public void setAwsBucketName(String awsBucketName) {
        this.awsBucketName = awsBucketName;
    }

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public void setAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }
}
