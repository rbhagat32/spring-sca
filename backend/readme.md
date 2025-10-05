# Run Services locally using Docker :-

## 1) Postgres

```
docker run -d --name spring-pg -p 5430:5432 -e POSTGRES_USER=raghav -e POSTGRES_PASSWORD=password -e POSTGRES_DB=spring-auth -v spring-pg:/var/lib/postgresql/data postgres
```

### If getting TimeZone error while connecting to DB, follow these steps in IntelliJ Idea:

1) Go to RUN in the topbar.
2) Edit Configurations.
3) Build and Run (Modify Options)
4) Add VM Options.
5) In the input field put -> `-Duser.timezone=Asia/Kolkata`

## 2) Redis

```
docker run -d --name spring-redis -p 6380:6379 redis
```
