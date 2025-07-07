# 📡 chat-ws - WebSocket Chat Service with Kafka (Spring Boot + Gradle)

Dự án `chat-ws` là một ứng dụng **chat real-time** sử dụng **Spring Boot WebSocket** kết hợp với **Kafka** để xử lý
message. Đây là một project phục vụ mục đích học tập và thực hành **CI/CD chuyên nghiệp** (với Jenkins hoặc GitHub
Actions), Docker, và kiến trúc microservices.

---

## 🚀 Tính năng

- Gửi và nhận tin nhắn theo phòng (chat room)
- WebSocket real-time bằng STOMP
- Gửi message qua Kafka topic
- Có thể mở rộng thành hệ thống chat đa service
- Tích hợp Gradle, Docker, CI/CD pipeline

---

## 🧰 Tech Stack

- ✅ Spring Boot 3.x (WebSocket, Kafka)
- ✅ Gradle
- ✅ Apache Kafka
- ✅ Docker
- ✅ Jenkins hoặc GitHub Actions (CI/CD)
- ✅ Java 21+

---

## 🛠️ Cài đặt & chạy local

### ✅ 1. Clone source code

```bash
git clone git@github.com:ndlamdev/zalo-backend.git
cd chat-ws
```

### ✅ 2. Run kafka
```bash
docker compose -f cli/docker/kafka/docker-compose.yml up -d --build
```

### ✅ 2. Chạy dự án
```bash
./gradlew bootRun
```
