# === Config ===
DOCKER_IMAGE = chat-ws:v1.0.0
DOCKER_CONTAINER ?= chat-ws-container
DOCKERFILE = ./cli/docker/project/Dockerfile
KAFKA_COMPOSE_FILE = ./cli/docker/kafka/docker-compose.yaml
PORT_MAPPING = -p 7000:1305
KAFKA_SERVER ?= kafka1:9092,kafka2:9093,kafka3:9094
JENKINS_DOCKERFILE = ./cli/docker/jenkins/Dockerfile
JENKINS_CONTAINER = jenkins_container
JENKINS_IMAGE = jenkins_image

# === Targets ===

# Build Docker image
project-build:
	gradlew clean build --no-daemon
	docker image rm -f $(DOCKER_IMAGE)
	docker build --no-cache -t $(DOCKER_IMAGE) -f $(DOCKERFILE) .

# Run container (detached)
project-run:
	  docker run $(PORT_MAPPING) --name $(DOCKER_CONTAINER) -d -e KAFKA_SERVER=$(KAFKA_SERVER) --network=zalo_shared_network $(DOCKER_IMAGE)

# Stop and remove container
project-stop:
	docker stop $(DOCKER_CONTAINER)
	docker rm $(DOCKER_CONTAINER)

project-sh:
	docker exec -it chat-ws-container sh

# Kafka cluster up
kafka-up:
	docker compose -f $(KAFKA_COMPOSE_FILE) up -d --build

# Kafka cluster down
kafka-down:
	docker compose -f $(KAFKA_COMPOSE_FILE) down

# Kafka cluster pause
kafka-pause:
	docker compose -f $(KAFKA_COMPOSE_FILE) pause

# Kafka cluster restart
kafka-restart:
	docker compose -f $(KAFKA_COMPOSE_FILE) restart

jenkins-build:
	docker image rm -f $(JENKINS_CONTAINER)
	docker build --no-cache -t $(JENKINS_IMAGE) -f $(JENKINS_DOCKERFILE) .

jenkins-run:
	docker run --name $(JENKINS_CONTAINER) -p 8080:8080 -p 50000:50000 -d -v F:\tai_lieu_hoc_tap\lap_trinh_android\zalo\server\chat-ws\data\jenkins:/var/jenkins_home -v //var/run/docker.sock:/var/run/docker.sock $(JENKINS_IMAGE)

jenkins-stop:
	docker stop $(JENKINS_CONTAINER)
	docker rm -f $(JENKINS_CONTAINER)

jenkins-password:
	docker exec -it $(JENKINS_CONTAINER) cat /var/jenkins_home/secrets/initialAdminPassword

jenkins-bash:
	docker exec -it $(JENKINS_CONTAINER) bash
ip:
	@docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(DOCKER_CONTAINER)

# Clean all (container + image)
clean:
	docker stop $(DOCKER_CONTAINER) || true
	docker rm $(DOCKER_CONTAINER) || true
	docker rmi $(DOCKER_IMAGE) || true
