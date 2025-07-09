# === Config ===
REDIS_DOCKER_IMAGE = redis:8.0
REDIS_CONTAINER = auth-redis

# === Targets ===

redis-run:
	docker run -d --name $(REDIS_CONTAINER) -p 6379:6379 -v F:\tai_lieu_hoc_tap\lap_trinh_android\zalo\server\auth\data\redis:\data  $(REDIS_DOCKER_IMAGE)

redis-stop:
	docker stop $(REDIS_CONTAINER)
	docker rm $(REDIS_CONTAINER)
