package com.ajsa.dyrepo.aws.config;

import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class DynamoDbConfig {

    @Value("${amazon.access.key}")
    private String awsAccessKey;

    @Value("${amazon.access.secret-key}")
    private String awsSecretKey;

    @Value("${amazon.end-point.url}")
    private String awsDynamoDBEndPoint;

    @Value("${amazon.region}")
    private String region;

    @Value("${amazon.table}")
    public static String dynamoTable;

    @Value("${amazon.s3.bucket}")
    public static String bucket;

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint, region))
                .withCredentials(amazonAWSCredentialsProvider())
                .build();
    }

    @Bean
    public DynamoDBMapper mapper() {
        return new DynamoDBMapper(amazonDynamoDB());
    }

    @Bean
    public S3Client s3Client(){
        return S3Client.builder().region(Region.of(region)).build();
    }

    @Bean
    public S3Presigner  presigner(){
        return S3Presigner.builder()
                .region(Region.of(region))
                .build();
    }

    @Bean
    public String S3Bucket(){
        return bucket;
    }


}
