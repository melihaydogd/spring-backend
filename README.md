# Spring Backend

This is a spring project that I started to develop to gain experience in Java 17 and Spring 3. I am following Bouali Ali's [YouTube playlist](https://youtube.com/playlist?list=PL41m5U3u3wwl5FoM2Y5gIu1Q-Wr5ascD_). You can use this repository as a starting point for backend projects. 

Following features are implemented:
* User registration
* JWT authentication
* Refresh token mechanism
* Logout mechanism (Sent request to /api/v1/auth/logout endpoint to revoke JWT token)
* Swagger interface (You can access to Swagger UI from this endpoint http://localhost:8080/api/v1/swagger-ui/index.html)
* Global exception handling
* MVC unit testing with JUnit5

## Database

You can start a PostgreSQL database in Docker using following command:
```console
docker run -d -p 5432:5432 \
    --restart unless-stopped \
    --name postgres \
    -e POSTGRES_USER=user \
    -e POSTGRES_PASSWORD=password \
    -v postgresdata:/var/lib/postgresql/data \
    postgres:15.3-alpine
```

This application uses default postgres database so you do not need create another database. You can change the database by creating a new database and changing the database application in application.yml file.

## Running the Application

You need to have Java 17 and a running PostgreSQL database. After that, you can use the application using IntelliJ IDEA.

## Contact

If you have any questions, you can contact me by email. My email is amelih6@gmail.com.