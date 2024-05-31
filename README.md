## Online Auction System

Implementation of the **Online Auction System** Domain Driven Design with Spring MVC.

### Building with compose

```
    docker compose up -d --build
```

### Detail build with docker

#### Building docker image

```
     docker image build -t auction-system-app .
```

#### Create Network

```
     docker network create db-network
```

##### Postgres Database

```
    docker run -d --name postgres-db\
     --network db-network \
     -e POSTGRES_DB=auctionsDb \
     -e POSTGRES_USER=puser \
     -e POSTGRES_PASSWORD=pass \
     -v postgresdata:/var/lib/postgresql/data \
     -p 5432:5432 postgres:16
```

#### Launch application

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
Navigate to: http://localhost:8080/
