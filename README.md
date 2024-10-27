## Online Auction System

Implementation of the **Online Auction System** Domain Driven Design with Spring MVC.

Use ***Docker*** to build and run the application.

### Pre-requisites

- You need to have ***Docker*** (and ***Docker Compose***) installed
- You need to sign up for a mail service (eg. ***mailtrap.io***)

### Building with compose

In the folder of file `docker-compose.yml`:
```
    EMAILSERV_HOST=<email_server_host> EMAILSERV_PORT=<email_server_port> EMAILSERV_USERNAME=<email_server_username> EMAILSERV_PASSWORD=<email_server_password> docker compose up -d --build
```
Once the application is running, navigate to: http://localhost:8080/

### Detail build with docker
In the folder of file `Dockerfile`:
#### 1. Building docker image

```
     docker image build -t auction-system-app .
```

#### 2. Create Network

```
     docker network create db-network
```

#### 3. Start Postgres Database

```
    docker run -d --name postgres-db\
     --network db-network \
     -e POSTGRES_DB=auctionsDb \
     -e POSTGRES_USER=puser \
     -e POSTGRES_PASSWORD=pass \
     -v postgresdata:/var/lib/postgresql/data \
     -p 5432:5432 postgres:16
```

#### 4. Launch application

```
   docker run -d --name auction-app \
    --network db-network \
    -e POSTGRES_HOST=postgres-db \
    -e POSTGRES_DB=auctionsDb \
    -e EMAILSERV_HOST=<email_server_host>\
    -e EMAILSERV_PORT=<email_server_port>\
    -e EMAILSERV_USERNAME=<email_server_username>\
    -e EMAILSERV_PASSWORD=<email_server_password>\
    -p 8080:8080 auction-system-app
```
#### 5. Navigate to: http://localhost:8080/
