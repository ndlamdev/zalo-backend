# === Config ===
DOCKER_IMAGE = chat-ws:v1.0.0
DOCKER_CONTAINER ?= chat-ws-container
DOCKERFILE = ./cli/docker/project/Dockerfile
KAFKA_COMPOSE_FILE = ./cli/docker/kafka/docker-compose.yaml
JENKINS_COMPOSE_FILE = ./cli/docker/jenkins/docker-compose.yaml
PORT_MAPPING = -p 7000:1305
KAFKA_SERVER ?= kafka_1:9092,kafka_2:9093,kafka_3:9094

# === Targets ===

# Build Docker image
build-project:
	docker image rm -f $(DOCKER_IMAGE)
	docker build --no-cache -t $(DOCKER_IMAGE) -f $(DOCKERFILE) .

# Run container (detached)
run-project:
	  docker run $(PORT_MAPPING) --name $(DOCKER_CONTAINER) -d -e KAFKA_SERVER=$(KAFKA_SERVER) --network=zalo_shared_network $(DOCKER_IMAGE)

# Stop and remove container
stop-project:
	docker stop $(DOCKER_CONTAINER)
	docker rm $(DOCKER_CONTAINER)

sh-project:
	docker exec -it chat-ws-container sh

# Kafka cluster up
kafka-up:
	docker compose -f $(KAFKA_COMPOSE_FILE) up -d --build

# Kafka cluster down
kafka-down:
	docker compose -f $(KAFKA_COMPOSE_FILE) down

# Kafka cluster down
kafka-pause:
	docker compose -f $(KAFKA_COMPOSE_FILE) pause

jenkins-up:
	docker compose -f $(JENKINS_COMPOSE_FILE) up -d --build

jenkins-down:
	docker compose -f $(JENKINS_COMPOSE_FILE) down

jenkins-pause:
	docker compose -f $(JENKINS_COMPOSE_FILE) pause

jenkins-password:
	docker exec -it jenkins_container cat /var/jenkins_home/secrets/initialAdminPassword

ip:
	@docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(DOCKER_CONTAINER)

# Clean all (container + image)
clean:
	docker stop $(DOCKER_CONTAINER) || true
	docker rm $(DOCKER_CONTAINER) || true
	docker rmi $(DOCKER_IMAGE) || true
