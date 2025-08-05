package com.example.oms.service;

import com.example.oms.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final DynamoDbTable<Order> orderTable;
    private final S3Service s3Service;
    private final SnsService snsService;

    public OrderService(S3Service s3Service, SnsService snsService,
            @Value("${cloud.aws.credentials.accesskey}") String accessKey,
            @Value("${cloud.aws.credentials.secretkey}") String secretKey,
            @Value("${aws.region}") String region,
            @Value("${aws.dynamodb.tableName}") String tableName) {

        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        this.orderTable = enhancedClient.table(tableName, TableSchema.fromBean(Order.class));
        this.s3Service = s3Service;
        this.snsService = snsService;
    }

    public Order createOrder(String customerName, String product, int quantity, Double price,
            MultipartFile invoiceFile) {
        String orderId = UUID.randomUUID().toString();
        String fileUrl = s3Service.uploadFile(orderId, invoiceFile);

        Order order = new Order();
        order.setId(orderId);
        order.setCustomerName(customerName);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setPrice(price);
        order.setCreatedAt(Instant.now().toString());
        order.setInvoiceFileUrl(fileUrl);

        orderTable.putItem(order);
        snsService.publishOrderNotification(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orderTable.scan().items().stream().toList();
    }

    public Order getOrderById(String id) {
        Order key = new Order();
        key.setId(id);
        return orderTable.getItem(key);
    }
}
