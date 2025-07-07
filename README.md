# LAU Case Backend Application 

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=uk.gov.hmcts.reform%3Alau-case-backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=uk.gov.hmcts.reform%3Alau-case-backend) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=uk.gov.hmcts.reform%3Alau-case-backend&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=uk.gov.hmcts.reform%3Alau-case-backend) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=uk.gov.hmcts.reform%3Alau-case-backend&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=uk.gov.hmcts.reform%3Alau-case-backend) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=uk.gov.hmcts.reform%3Alau-case-backend&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=uk.gov.hmcts.reform%3Alau-case-backend) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=uk.gov.hmcts.reform%3Alau-case-backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=uk.gov.hmcts.reform%3Alau-case-backend)

## Purpose

This is the Log and Audit Case Back-End application that will provide an endpoint to capture and retrieve case log actions for a users views and searches to the database.

The API will be invoked by two components - LAU Case front-end which allows CFT Auditors to view log entries for case views and searches and for the source system to store log data into the Log and Audit system.

## Overview

<p align="center">
<a href="https://github.com/hmcts/lau-frontend">lau-frontend</a> • <b><a href="https://github.com/hmcts/lau-case-backend">lau-case-backend</a></b> • <a href="https://github.com/hmcts/lau-idam-backend">lau-idam-backend</a> • <a href="https://github.com/hmcts/lau-shared-infrastructure">lau-shared-infrastructure</a>
</p>

<br>

<p align="center">
  <img src="https://raw.githubusercontent.com/hmcts/reform-api-docs/master/docs/c4/lau/images/structurizr-lau-overview.png" width="500"/>
</p>

## What's inside

The application exposes health endpoint (http://localhost:4550/health).

## Plugins

The application uses the following plugins:

  * checkstyle https://docs.gradle.org/current/userguide/checkstyle_plugin.html
  * pmd https://docs.gradle.org/current/userguide/pmd_plugin.html
  * jacoco https://docs.gradle.org/current/userguide/jacoco_plugin.html
  * io.spring.dependency-management https://github.com/spring-gradle-plugins/dependency-management-plugin
  * org.springframework.boot http://projects.spring.io/spring-boot/
  * org.owasp.dependencycheck https://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html
  * com.github.ben-manes.versions https://github.com/ben-manes/gradle-versions-plugin

## Building and deploying the application

### Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle.

To build the project execute the following command:
```bash
  ./gradlew build
```

### Running the application

To run the PostgreSQL 15 LAU database, execute docker-compose to build the database from the postgreSQL docker image:
```
  docker-compose -f docker-compose.yml up lau-case-database
```

To apply flyway database scripts for PostgreSQL 15 LAU database, execute gradle migratePostgresDatabase task command - this will create all necessary users and tables.
```
  ./gradlew migratePostgresDatabase
```

To run the LAU Backend application, execute the gradle run command - this also adds the LAU schema to the database.
```
  ./gradlew run
```

To test database access using psql or your favourite SQL client use the equivalent parameters below. See .env file for dev database password.
 ```
  psql -h 0.0.0.0 -p 5054 -d lau_case -U lauuser
 ```
On a MAC you may have to put the full path in for psql - something like:
 ```
  /Library/PostgreSQL/11/bin/psql -h 0.0.0.0 -p 5054 -d lau_case -U lauuser
 ```

#### Environment variables

The `.env` file has a list of the environment variables in use by the lau-case-backend and lau-database components. These are as follows:
* LAU_DB_NAME
* LAU_DB_HOST
* LAU_DB_PORT
* LAU_DB_USERNAME
* LAU_DB_PASSWORD
* FLYWAY_URL
* FLYWAY_USER
* FLYWAY_PASSWORD
* FLYWAY_NOOP_STRATEGY

There is no need to export these values if lau-case-backend repo is checked out.
If another service is using the lau-case-backend application, the emnvironment values are available through a batch script:
```bash
  source ./bin/set-lau-docker-env.sh
```

#### Manually building the LAU Database using Flyway

The lau-case-backend application will automatically create the LAU database definitions from the flyway scripts included in the build.
However if you would like to add the table definitions manually you'll need to execute the gradle migrate script:
```
  ./gradlew -Pflyway.user=lauadmin -Pflyway.password=laupass -Pflyway.url=jdbc:postgresql://0.0.0.0:5054/lau_case flywayMigrate
```

## Running the application in Docker

### Build docker image

Create docker image:
```bash
  docker-compose build
```

Bring the lau-case-database and lau-case-backend application up in Docker.
by executing the following command:
```bash
  docker-compose up
```

az login
az acr login --name hmctspublic --subscription DCD-CNP-Prod

This will start the API container exposing the application's port
(set to `4550` in this template app).

In order to test if the application is up, you can call its health endpoint:
```bash
  curl http://localhost:4550/health/readiness
```

You should get a response similar to this:
```
  {"status":"UP","diskSpace":{"status":"UP","total":249644974080,"free":137188298752,"threshold":10485760}}
```

### Notes:

### Accessing Azure Container Repository

Run the following to install the azure and kubernetes command-line tools
```bash
  brew install azure-cli
  az acs kubernetes install-cli
```
login to azure - you'll not need hmctsprivate access for LAU.
```bash
  az login (will open a browser to login)
  az acr login --name hmctspublic --subscription DCD-CNP-Prod
  az acr login --name hmctsprivate --subscription DCD-CNP-Prod
```

#### Removing old docker images

Old containers can be removed by executing the following command:
```bash
  docker rm $(docker ps -a -q)
```
You may need to forcibly remove any relevant images by executing the following command:
```bash
  docker rmi $(docker images -q)
```
Finally you can remove all volumes - not this rmoves all existing database values:
```bash
  docker volume rm $(docker volume ls -q)
```

There is no need to remove postgres and java or similar core images.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details..

