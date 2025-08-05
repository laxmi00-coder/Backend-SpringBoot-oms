# Order Management System - Spring Boot Backend

This is the backend service for the Order Management System. It is built using Spring Boot and integrated with the following AWS services:

- **Amazon S3** for storing invoice files.
- **Amazon DynamoDB** for storing order data.
- **Amazon SNS** for sending notifications upon order creation.

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3.x
- AWS SDK v2 (S3, DynamoDB Enhanced Client, SNS)
- Maven

---

## 📁 Project Structure

oms/
├── src/
│ ├── main/
│ │ ├── java/com/example/oms/
│ │ │ ├── controller/ # REST endpoints
│ │ │ ├── model/ # Order entity
│ │ │ ├── service/ # S3Service, SnsService, OrderService
│ │ │ └── ... # Main application
│ │ └── resources/
│ │ └── application.properties
└── pom.xml

yaml
Copy
Edit

---

## ⚙️ Configuration

### `application.properties`

```properties
# AWS Credentials (should be passed securely using environment variables)
cloud.aws.credentials.accesskey=${AWS_ACCESS_KEY}
cloud.aws.credentials.secretkey=${AWS_SECRET_KEY}
aws.region=ap-south-1

# S3
aws.s3.bucket-name=your-s3-bucket-name

# DynamoDB
aws.dynamodb.tableName=orders

# SNS
aws.sns.topicArn=arn:aws:sns:ap-south-1:your-account-id:your-topic-name

# Spring
server.port=8080
⚠️ Never commit real AWS credentials. Use environment variables or a secure secrets manager.

🧪 API Endpoints
Method	Endpoint	Description
POST	/orders	Create a new order
GET	/orders	List all orders
GET	/orders/{id}	Get order by ID

🚀 Running Locally
Prerequisites
Java 21

Maven

AWS account (S3 bucket, DynamoDB table, and SNS topic created)

Run
bash
Copy
Edit
# Set your AWS credentials (or use aws configure)
export AWS_ACCESS_KEY=your-key
export AWS_SECRET_KEY=your-secret

# Run the app
mvn spring-boot:run
📦 AWS Services Required
✅ DynamoDB
Table Name: orders

Primary Key: id (String)

✅ S3 Bucket
Bucket name: Your bucket (e.g., oms-invoices)

Permissions: PutObject, GetObject

✅ SNS Topic
Topic ARN: arn:aws:sns:ap-south-1:your-account-id:your-topic-name

Permissions: sns:Publish

🔒 IAM Permissions Required
Your IAM user/role should have permissions:

json
Copy
Edit
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:PutObject",
        "s3:GetObject"
      ],
      "Resource": "arn:aws:s3:::your-s3-bucket-name/*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "dynamodb:PutItem",
        "dynamodb:GetItem",
        "dynamodb:Scan"
      ],
      "Resource": "arn:aws:dynamodb:ap-south-1:your-account-id:table/orders"
    },
    {
      "Effect": "Allow",
      "Action": "sns:Publish",
      "Resource": "arn:aws:sns:ap-south-1:your-account-id:your-topic-name"
    }
  ]
}
🧹 TODO / Improvements
Add unit/integration tests

Add pagination to order listing

Add authentication using Spring Security

Use AWS Parameter Store or Secrets Manager for credentials
