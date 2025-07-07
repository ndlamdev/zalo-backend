# 🟢 Chat WebSocket Microservices with Kafka, Config Server, Discovery

Dự án này là một hệ thống **Chat thời gian thực** sử dụng **Spring Boot**, **WebSocket (STOMP)**, **Kafka**, kết hợp với **Spring Cloud Config Server** và **Discovery Server (Eureka)**. Hệ thống được xây dựng theo mô hình **Microservices**, hỗ trợ **scaling theo chiều ngang**, dễ dàng mở rộng và cấu hình tập trung.

---

## 🚀 Mục tiêu

- Gửi & nhận tin nhắn thời gian thực giữa nhiều người dùng.
- Mở rộng hệ thống dễ dàng theo chiều ngang.
- Cấu hình tập trung cho toàn bộ hệ thống.
- Dễ dàng phát hiện và định tuyến các service nhờ Eureka.
- Lưu trữ và truy xuất lịch sử tin nhắn.

---

## 🧱 Kiến trúc hệ thống

```text
Client
   |
   v
[Spring Cloud Gateway]
   |
   v
[Auth Service] ---> [User Service]
   |
   v
[Chat WebSocket Service] <--> Kafka <--> [Chat Service]
                               |
                               v
                            [Redis Pub/Sub]
                               |
                               v
                      [Other WebSocket Instances]

(Config Server & Discovery Server ở tầng control plane)
```

---

## ⚙️ Công nghệ sử dụng

| Thành phần             | Công nghệ                     |
|------------------------|-------------------------------|
| API Gateway            | Spring Cloud Gateway          |
| Config Server          | Spring Cloud Config           |
| Service Discovery      | Eureka Server                 |
| Authentication         | Spring Security + JWT         |
| WebSocket Chat         | Spring WebSocket + STOMP      |
| Messaging Queue        | Apache Kafka                  |
| Data Persistence       | MongoDB                       |
| Cache & Session        | Redis                         |
| Deploy & Container     | Docker + Docker Compose       |

---

## 📁 Cấu trúc thư mục

```
chat-ws/                   # WebSocket service
auth-service/              # Auth (JWT)
chat-service/              # Lưu lịch sử tin nhắn
user-service/              # Quản lý user
gateway/                   # API Gateway
config-server/             # Spring Cloud Config
discovery-server/          # Eureka Server
README.md
```

---

## 🐳 Khởi chạy hệ thống

### Bước 1: Khởi tạo Kafka Cluster

```bash
cd kafka-cluster
docker-compose up -d
```

### Bước 2: Khởi chạy các Service chính

```bash
docker-compose -f docker-compose-app.yml up -d
```

> Có thể dùng thêm Kafka UI để quan sát trạng thái topic, broker, message.

---

## 📡 Giao tiếp WebSocket (STOMP)

- **Endpoint WebSocket:** `ws://localhost:8080/ws`
- **Gửi tin nhắn:** `/app/chat.send`
- **Subscribe:** `/topic/chat/{roomId}`

### 📨 Message mẫu:

```json
{
  "sender_id": "123",
  "received_id": "456",
  "content": "Hello world",
  "timestamp": "2025-07-06T12:00:00"
}
```

---

## 📦 Tạo Topic Kafka thủ công (nếu cần)

```bash
docker exec kafka1 kafka-topics   --bootstrap-server kafka1:19092   --create   --topic chat-topic   --replication-factor 3   --partitions 3
```

---

## ✅ Các tính năng chính

- [x] Gửi/nhận tin nhắn real-time
- [ ] Đăng nhập & xác thực người dùng bằng JWT
- [ ] Lưu trữ lịch sử chat vào database
- [ ] Broadcast message qua Kafka
- [ ] Hỗ trợ scale WebSocket ngang qua nhiều instance
- [ ] Cấu hình tập trung qua Spring Cloud Config
- [ ] Service discovery tự động qua Eureka
- [ ] Tích hợp gửi thông báo (FCM) - _đang phát triển_
- [ ] Hệ thống tìm kiếm (Elasticsearch) - _đang phát triển_

---

## 🔐 Authentication

Sử dụng JWT, token được gắn vào quá trình bắt tay WebSocket:

```
ws://localhost:8080/ws?token=YOUR_JWT_TOKEN
```

---

## 🛠 Cấu hình Spring Boot (ví dụ)

```properties
spring.kafka.bootstrap-servers=localhost:9092,localhost:9093,localhost:9094
spring.kafka.consumer.group-id=chat-group
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

spring.cloud.config.uri=http://localhost:8888
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

---

## 👨‍💻 Yêu cầu hệ thống

- Java 17+
- Gradle hoặc Maven
- Docker & Docker Compose
- Redis, Kafka, Zookeeper
- MongoDB hoặc PostgreSQL
- Spring Cloud (Config, Eureka)

---

## 📜 License

MIT License — tự do sử dụng cho học tập, phát triển, nghiên cứu.

---

## 📫 Liên hệ

Nguyễn Đình Lam  
📧 Email: ndlam.dev@gmail.com  
📍 Bình Dương, Việt Nam  
GitHub: [github.com/nguyendinhlam](https://github.com/nguyendinhlam)

---
_Đây là dự án demo kiến trúc microservices chat realtime có khả năng scale & cấu hình tập trung._