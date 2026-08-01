# Chạy docker trên docker jenkins container
## Với hệ điều hành linux hoặc mac

- Mount volume `/var/run/docker.sock:/var/run/docker.sock` vào docker container thì có thể kết nối được với docker

## Với hệ điều hành window

- Mở Docker Desktop lên
- Truy cập vào phần cài đặt
- Tại tab `General`, tích chọn vào `Expose daemon on tcp://localhost:2375 without TLS` và save lại cài đặt
- Chạy docker với biến môi trường `DOCKER_HOST=tcp://host.docker.internal:2375` để có thể điều kiến docker trên window