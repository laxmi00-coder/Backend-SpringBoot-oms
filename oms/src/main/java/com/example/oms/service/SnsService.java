package com.example.oms.service;

import com.example.oms.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
public class SnsService {

        private final SnsClient snsClient;

        @Value("${cloud.aws.sns.topic.ordercreatedarn}")
        private String topicArn;

        public SnsService(@Value("${cloud.aws.credentials.accesskey}") String accessKey,
                        @Value("${cloud.aws.credentials.secretkey}") String secretKey,
                        @Value("${aws.region}") String region) {

                this.snsClient = SnsClient.builder()
                                .region(Region.of(region))
                                .credentialsProvider(
                                                StaticCredentialsProvider.create(
                                                                AwsBasicCredentials.create(accessKey, secretKey)))
                                .build();
        }

        public void publishOrderNotification(Order order) {
                String message = String.format("New order created:%nCustomer: %s%nProduct: %s%nAmount: %.2f",
                                order.getCustomerName(), order.getProduct(), order.getPrice());

                PublishRequest request = PublishRequest.builder()
                                .topicArn(topicArn)
                                .message(message)
                                .subject("New Order Created")
                                .build();

                snsClient.publish(request);
        }
}
