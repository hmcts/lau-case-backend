#!/bin/sh

export SERVER_PORT=4550

# Database
export LAU_DB_NAME=lau_case
export LAU_DB_HOST=0.0.0.0
export LAU_DB_PORT=5054
export LAU_DB_USERNAME=lauuser
export LAU_DB_PASSWORD=lau1234
export LAU_DB_ADMIN_USERNAME=lauadmin
export LAU_DB_ADMIN_PASSWORD=lau1234
export CASE_BACKEND_ENCRYPTION_KEY=my_very_secure_key

export FLYWAY_PLACEHOLDERS_LAU_DB_USERNAME=lauuser
export FLYWAY_PLACEHOLDERS_LAU_DB_PASSWORD=lau1234
export FLYWAY_URL=jdbc:postgresql://0.0.0.0:5054/lau_case
export FLYWAY_USER=lauadmin
export FLYWAY_PASSWORD=lau1234
export FLYWAY_NOOP_STRATEGY=false

