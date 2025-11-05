docker build -t rbhagat32/spring-sca-frontend --build-arg VITE_BACKEND_URL=https://sca.void9.space ./frontend

docker build -t rbhagat32/spring-sca-backend ./backend

docker push rbhagat32/spring-sca-frontend

docker push rbhagat32/spring-sca-backend

sudo docker compose up --scale backend=2 -d

sudo docker compose down --rmi local --remove-orphans && sudo docker image prune -f && sudo docker volume rm spring-sca_redis-dump

sudo systemctl reload nginx

certbot

1. t3.2xlarge ec2 instance
2. associate elastic ip
3. install docker on ec2
4. make folder spring-sca on ec2
5. make compose.yaml, nginx.conf, .env.frontend on ec2
6. compose up command
