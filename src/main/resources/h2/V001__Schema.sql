
  -- case_action_audit table
  CREATE TABLE case_action_audit (
   case_action_id SERIAL PRIMARY KEY,
   user_id VARCHAR(64) NOT NULL,
   case_action VARCHAR(16)  NOT NULL CHECK (case_action in ('VIEW','UPDATE','CREATE')),
   case_ref VARCHAR(16) NOT NULL,
   case_jurisdiction_id VARCHAR(70) NOT NULL,
   case_type_id VARCHAR(70) NOT NULL,
   log_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
  );

  -- case_action_audit comments
  comment on column case_action_audit.case_action_id is 'Unique lau case action id';
  comment on column case_action_audit.user_id is 'User id performing view action';
  comment on column case_action_audit.case_action is 'The action carried out-(R= View)';
  comment on column case_action_audit.case_ref is 'Case reference number';
  comment on column case_action_audit.case_jurisdiction_id is 'Case jurisdiction id';
  comment on column case_action_audit.case_type_id is 'Case type id';
  comment on column case_action_audit.log_timestamp is 'Case search log timestamp';

  -- case_action_audit indexes
  CREATE INDEX case_action_audit_user_id_idx ON case_action_audit (user_id);
  CREATE INDEX case_action_audit_case_ref_idx ON case_action_audit (case_ref);
  CREATE INDEX case_action_audit_action_idx ON case_action_audit (case_action);
  CREATE INDEX case_action_audit_case_jurisdiction_id_idx ON case_action_audit (case_jurisdiction_id);
  CREATE INDEX case_action_audit_case_type_id_idx ON case_action_audit (case_type_id);
  CREATE INDEX case_action_audit_log_timestamp_idx ON case_action_audit (log_timestamp);

  -- case_search_audit table
  CREATE TABLE case_search_audit (
   id SERIAL PRIMARY KEY,
   user_id VARCHAR(64) NOT NULL,
   log_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
  );

  -- case_search_audit comments
  comment on column case_search_audit.id is 'Unique lau case search id';
  comment on column case_search_audit.user_id is 'User id performing search action';
  comment on column case_search_audit.log_timestamp is 'Case search log timestamp';

  -- case_search_audit indexes
  CREATE INDEX case_search_audit_user_id_idx ON case_search_audit (user_id);
  CREATE INDEX case_search_audit_log_timestamp_idx ON case_search_audit (log_timestamp);

  -- case_search_audit_cases table
  CREATE TABLE case_search_audit_cases(
      id        SERIAL PRIMARY KEY,
      search_id BIGINT,
      case_ref  VARCHAR(16),
      CONSTRAINT case_search_constraint UNIQUE (search_id, case_ref),
      CONSTRAINT fk_search_id
          FOREIGN KEY(search_id)
              REFERENCES case_search_audit(id)
  );

  -- case_search_audit_cases comments
  comment on column case_search_audit_cases.search_id is 'Unique lau case search id';
  comment on column case_search_audit_cases.case_ref is 'Case reference number';
