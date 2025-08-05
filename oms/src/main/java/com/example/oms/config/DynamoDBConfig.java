package com.example.oms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class DynamoDBConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${cloud.aws.credentials.accesskey}")
    private String accessKeyId;

    @Value("${cloud.aws.credentials.secretkey}")
    private String secretKey;

    @Bean
    public StaticCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretKey));
    }

    @Bean
    public DynamoDbClient dynamoDbClient(StaticCredentialsProvider credentialsProvider) {
        return DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public S3Client s3Client(StaticCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public SnsClient snsClient(StaticCredentialsProvider credentialsProvider) {
        return SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
