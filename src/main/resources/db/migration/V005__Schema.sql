ALTER TABLE case_action_audit ALTER COLUMN case_type_id TYPE VARCHAR(70);
ALTER TABLE case_action_audit ALTER COLUMN case_jurisdiction_id TYPE VARCHAR(70);

DROP EXTENSION IF EXISTS citext;


