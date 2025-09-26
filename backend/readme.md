# Run PostgreSQL locally using Docker

```
docker run -d -p 5430:5432 --name spring-pg -e POSTGRES_USER=raghav -e POSTGRES_PASSWORD=password -e POSTGRES_DB=spring-auth -v spring-pg:/var/lib/postgresql/data postgres
```

### If getting TimeZone error while connecting to DB, follow these steps in IntelliJ Idea:

1) Go to RUN in the topbar.
2) Edit Configurations.
3) Build and Run (Modify Options)
4) Add VM Options.
5) In the input field put -> `-Duser.timezone=Asia/Kolkata`