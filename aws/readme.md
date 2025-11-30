# Deploying Spring SCA on AWS EC2

This guide explains how to deploy **Spring SCA** on **AWS EC2** using Docker and Nginx.

### Youtube Tutorial:

## [![Deploying Spring SCA on AWS EC2](https://img.youtube.com/vi/kTohKO9Zb4U/maxresdefault.jpg)](https://www.youtube.com/watch?v=kTohKO9Zb4U)

## 1. Build & Push Docker Images

Build the Docker images locally and push them to Docker Hub.

```bash
   docker build -t rbhagat32/spring-sca-frontend --build-arg VITE_BACKEND_URL=https://sca.void9.space ./frontend

   docker build -t rbhagat32/spring-sca-backend --build-arg SPRING_PROFILES_ACTIVE=prod ./backend

   docker push rbhagat32/spring-sca-frontend
   docker push rbhagat32/spring-sca-backend
```

---

## 2. Set Up EC2 Instance

1. Launch an **EC2 instance** (recommended: `t3.2xlarge`)

2. Associate an **Elastic IP**

3. Attach a **Security Group** (allow ports `22`, `80`, `443`, `5173`, `8080` initially.)

4. Install **Docker** on EC2

5. Create a project folder at: `/home/ubuntu/spring-sca`

6. Create the following files inside the folder:
   - `compose.yaml`
   - `nginx.conf`

---

## 3. Set Up Your Domain

Add an `A record` that points your domain/subdomain to your EC2 Elastic IP.

---

## 4. Deploy with Docker Compose

Start all containers:

```bash
   sudo docker compose up --scale backend=2 -d
```

To stop and clean up resources:

```bash
   sudo docker compose down --rmi local --remove-orphans && sudo docker image prune -f && sudo docker volume rm spring-sca_redis-dump
```

---

## 5. Configure Nginx

1. Install **Nginx** on EC2

2. Place **Nginx.conf** (from aws folder) at:

```
   /etc/nginx/sites-enabled/default
```

3. **Reload** Nginx after any config change:

```bash
   sudo systemctl reload nginx
```

---

## 6. Enable HTTPS with Certbot

Install **Certbot** to automatically generate and manage SSL certificates.

---

## 7. Security Group (Inbound Rules)

Allow traffic only on ports `22` and `443` only after setting up HTTPS.
