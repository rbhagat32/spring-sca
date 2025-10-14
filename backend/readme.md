# Run Services locally using Docker :-

## 1) Postgres

```
docker run -d --name spring-pg -p 5430:5432 -e POSTGRES_USER=raghav -e POSTGRES_PASSWORD=password -e POSTGRES_DB=spring-sca -v spring-pg:/var/lib/postgresql/data postgres
```

### If getting TimeZone error while connecting to Postgres, follow these steps in IntelliJ Idea:

1. Go to RUN in the topbar.
2. Edit Configurations.
3. Build and Run (Modify Options)
4. Add VM Options.
5. In the input field put -> `-Duser.timezone=Asia/Kolkata`

## 2) Redis

```
docker run -d --name spring-redis -p 6380:6379 redis
```

## 3) Kafka

```
docker run -d --name spring-kafka -p 9092:9092 -e KAFKA_ENABLE_KRAFT=yes -e KAFKA_CFG_NODE_ID=1 -e KAFKA_CFG_PROCESS_ROLES=broker,controller -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 -e KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE=true -e ALLOW_PLAINTEXT_LISTENER=yes bitnami/kafka
```

## Get Admin role through DB Query:

```
docker exec -it <postgres_container_id> bash

psql -U raghav -d spring-sca

UPDATE users SET roles = array_append(roles, 'ROLE_ADMIN') WHERE email = 'raghavbhagat32@gmail.com';
```
