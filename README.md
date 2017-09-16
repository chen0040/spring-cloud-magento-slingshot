# spring-cloud-magento-slingshot

Spring Cloud Slingshot project that extends from spring-boot-slingshot

# Architecture

The system consists of the following component

* sbs-eureka-server: this is the eureka server which serves as the server registry for the micro-services
* sbs-syslog4j-server: this is the syslog4j server that servers as the centralized logging system for the micro-services
* sbs-eureka-app: this is application server, which can be considered as one of the micro-service which serves as the service prodvider
* sbs-eureka-app-magento: this is the application server which provides the magento e-commerce service to other micro services.
* sbs-eureka-web: this is web server, which can consumes service provided by application servers such as sbs-eureka-app
* sbs-mariadb-server: this is a standalone mariadb server for local development and testing
* sbs-redis-server: this is a standalone redis server for local development and testing

Each of these standalone application servers has embedded jetty or tomcat server running inside them, so
they don't need to have another servlet container to host them.

Among these application servers, the following form the eureka micro-service architecture:

* sbs-eureka-server
* sbs-eureka-app
* sbs-eureka-app-magento
* sbs-eureka-web

# Powershell Support on Running Cluster Locally

To build all the modules into standalone server jars, run the following command in the root directory:

```bash
./make.ps1
```

To start locally the cluster of the spring cloud application servers and web servers, run the following command in the root 
directory:

```bash
./start-cluster.ps1
```

To stop the cluster running locally, run the following command in the root directory:

```bash
./stop-cluster.ps1
```

Note that the cluster already has a mariadb instance running inside, so you do not need to have another separate mysql or mariadb 
running locally.

# sbs-eureka-web

* Spring Data JPA and Spring Security for Authentication
* Spring Data JPA configuration for database
* Jest for ElasticSearch
* Websocket + sockjs + stompjs + AngularJS + AngularUI
* Bootstrap + thymeleaf
* Language (cn + en)

## Configuration

To use this project create a database named spring_boot_slingshot in your mysql database (make sure it is running at localhost:3306)

```sql
CREATE DATABASE spring_boot_slingshot CHARACTER SET utf8 COLLATE utf8_unicode_ci;
```

In case you do not have the mariadb or mysql server installed, locally, you can also run the sbs-mariadb-server which is a standalone mariadb server 
that can be run at port 3306 locally.

Note that the default username and password for the mysql is configured to 

* username: root
* password: chen0469

If your mysql or mariadb does not use these configuration, please change the settings in src/resources/config/application-default.properties

## Usage

This is just a template project that provides slingshot for spring cloud. Just use it as the starting point for your spring cloud project development.

Note that the application will generate two accounts in the database on startup if they don't exist:

ADMIN:

* username: admin
* password: admin

DEMO:

* username: demo
* password: demo

