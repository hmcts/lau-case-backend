
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

  -- create user for application access
  --CREATE USER lauuser WITH ENCRYPTED PASSWORD 'laupass';
  --GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE case_view_audit, case_search_audit, case_search_audit_cases TO lauuser;
