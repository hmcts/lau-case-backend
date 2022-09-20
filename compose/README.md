# To use ccd-docker elements

## Purpose
Guidance on how to set up lau backend microservices locally using the updated docker images.

##### 1) Install https://stedolan.github.io/jq/

```bash
  sudo apt-get install jq
```

##### 2) Login to azure

```bash
  az login
  az acr login --name hmctspublic --subscription DCD-CNP-Prod
  az acr login --name hmctsprivate --subscription DCD-CNP-Prod
```

##### 3) Reset your docker images, containers etc.
Removing images will result in downloading all images again which you may want to skip.
```bash
   docker image rm $(docker image ls -a -q)
   docker container rm $(docker container ls -a -q)
   docker volume rm $(docker volume ls -q)
```

##### 4) Run environments scripts
First run the CCD login script
```bash
   ./ccd login
```
Load the docker env variables (for mac):
```bash
   source ./bin/set-environment-variables.sh
```

In addition all scripts require the following environment variables to be set with the corresponding values from the confluence page at https://tools.hmcts.net/confluence/x/eQP3P
```bash
export IDAM_ADMIN_USER=<value of username>
export IDAM_ADMIN_PASSWORD=<value of password>
```

##### 4) Start up docker
```bash
   docker network create compose_default
   ./ccd compose pull
   ./ccd compose build
   ./ccd compose up -d
```

##### 5) Setup IDAM data.
```bash
   ./bin/idam-client-setup.sh
```
To check the IDAM data, you can log into IDAM-web `http://localhost:8082/login` with `idamOwner@hmcts.net` and the password used for `IDAM_ADMIN_PASSWORD`.

##### 6) Generate roles, add users

###### Create roles and users
```bash
   ./bin/create-log-and-audit-users.sh
```
You can check the user and roles on the IDAM-web by searching for `auditor@gmail.com` on Manager Users page.


##### 7) Start your local service

###### Log into LAU-Frontend application
Login to ccd on `http://localhost:4000`. User: `auditor@gmail.com / Pa55word11`.
