# === Config ===
REDIS_CONTAINER = auth-redis
DOCKER_IMAGE = zalo-auth:v1.0.0
DOCKER_CONTAINER ?= zalo-auth-container
PORT_MAPPING = -p 8002:8001
POSTGRES_SERVER=host.docker.internal:5432

# Build Docker image
project-build:
	gradlew clean build --no-daemon
	docker image rm -f $(DOCKER_IMAGE)
	docker build --no-cache -t $(DOCKER_IMAGE) .

# Run container (detached)
project-run:
	  docker run $(PORT_MAPPING) --name $(DOCKER_CONTAINER) -d -e POSTGRES_SERVER=$(POSTGRES_SERVER) --network=zalo_shared_network $(DOCKER_IMAGE)

# Stop and remove container
project-stop:
	docker stop $(DOCKER_CONTAINER)
	docker rm $(DOCKER_CONTAINER)

project-sh:
	docker exec -it chat-ws-container sh

# Clean all (container + image)
clean:
	docker stop $(DOCKER_CONTAINER) || true
	docker rm $(DOCKER_CONTAINER) || true
	docker rmi $(DOCKER_IMAGE) || true

