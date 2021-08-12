#!/usr/bin/env bash

set -e

if [ -z "$LAU_DB_NAME" ] || [ -z "$LAU_DB_USERNAME" ] || [ -z "$LAU_DB_PASSWORD" ]; then
  echo "ERROR: Missing environment variable. Set value for 'LAU_DB_NAME', 'LAU_DB_USERNAME' and 'LAU_DB_PASSWORD'."
  exit 1
fi

# Create roles and databases
psql -v ON_ERROR_STOP=1 --username postgres --set USERNAME=$LAU_DB_USERNAME --set PASSWORD=$LAU_DB_PASSWORD <<-EOSQL
  CREATE USER :USERNAME WITH PASSWORD ':PASSWORD';
EOSQL

psql -v ON_ERROR_STOP=1 --username postgres --set USERNAME=$LAU_DB_USERNAME --set PASSWORD=$LAU_DB_PASSWORD --set DATABASE=$LAU_DB_NAME <<-EOSQL
  CREATE DATABASE :DATABASE
    WITH OWNER = :USERNAME
    ENCODING = 'UTF-8'
    CONNECTION LIMIT = -1;
    ALTER SCHEMA public OWNER TO :USERNAME;
EOSQL

psql -v ON_ERROR_STOP=1 --username postgres --set USERNAME=$LAU_DB_USERNAME --set PASSWORD=$LAU_DB_PASSWORD <<-EOSQL
  CREATE EXTENSION IF NOT EXISTS pgcrypto;
EOSQL

psql -v ON_ERROR_STOP=1 --username $LAU_DB_USERNAME $LAU_DB_NAME <<-EOSQL

  -- case_view_audit table
  CREATE TABLE case_view_audit (
   case_view_id SERIAL PRIMARY KEY,
   user_id VARCHAR(64) NOT NULL,
   case_ref VARCHAR(64) NOT NULL,
   case_jurisdiction_id VARCHAR(70) NOT NULL,
   case_type_id VARCHAR(70) NOT NULL,
   log_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
  );

  -- case_view_audit comments
  comment on column case_view_audit.case_view_id is 'Unique lau case view id';
  comment on column case_view_audit.user_id is 'User id performing view action';
  comment on column case_view_audit.case_ref is 'Case reference number';
  comment on column case_view_audit.case_jurisdiction_id is 'Case jurisdiction id';
  comment on column case_view_audit.case_type_id is 'Case type id';
  comment on column case_view_audit.log_timestamp is 'Case search log timestamp';

  -- case_view_audit indexes
  CREATE INDEX case_view_audit_user_id_idx ON case_view_audit (user_id);
  CREATE INDEX case_view_audit_case_ref_idx ON case_view_audit (case_ref);
  CREATE INDEX case_view_audit_case_jurisdiction_id_idx ON case_view_audit (case_jurisdiction_id);
  CREATE INDEX case_view_audit_case_type_id_idx ON case_view_audit (case_type_id);
  CREATE INDEX case_view_audit_log_timestamp_idx ON case_view_audit (log_timestamp);

  -- case_search_audit table
  CREATE TABLE case_search_audit (
   search_id SERIAL PRIMARY KEY,
   user_id VARCHAR(64) NOT NULL,
   log_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
  );

  -- case_search_audit comments
  comment on column case_search_audit.search_id is 'Unique lau case search id';
  comment on column case_search_audit.user_id is 'User id performing search action';
  comment on column case_search_audit.log_timestamp is 'Case search log timestamp';

  -- case_search_audit indexes
  CREATE INDEX case_search_audit_user_id_idx ON case_search_audit (user_id);
  CREATE INDEX case_search_audit_log_timestamp_idx ON case_search_audit (log_timestamp);

  -- case_search_audit_cases table
  CREATE TABLE case_search_audit_cases (
   search_id BIGINT,
   case_ref VARCHAR(16),
   PRIMARY KEY(search_id, case_ref)
  );

  -- case_search_audit_cases comments
  comment on column case_search_audit_cases.search_id is 'Unique lau case search id';
  comment on column case_search_audit_cases.case_ref is 'Case reference number';

EOSQL
  echo "Database $service: Created"

