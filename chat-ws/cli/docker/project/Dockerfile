# Stage 1: Build JDK runtime using jlink
FROM eclipse-temurin:23-alpine AS build_tool

LABEL authors="lam-nguyen"

WORKDIR /build_dir

# Sử dụng jlink để tạo một JRE tối ưu hóa
RUN jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress 2 \
    --output /jre

# Stage 2: Build application image
FROM alpine:latest

LABEL authors="lam-nguyen"

WORKDIR /application

# Copy JRE được tạo từ build_tool
COPY --from=build_tool /jre /library/jre

# Thiết lập môi trường Java
ENV JAVA_HOME=/library/jre
ENV PATH=${JAVA_HOME}/bin:${PATH}
ENV CHAT_WS_PORT=1305

# Copy JAR ứng dụng vào container
COPY ../../../build/libs/chat-ws-0.0.1-SNAPSHOT.jar chat-ws.jar

EXPOSE 1305

# Run application
ENTRYPOINT ["java", "-jar", "chat-ws.jar"]
