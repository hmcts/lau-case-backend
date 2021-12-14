CREATE EXTENSION IF NOT EXISTS citext;

ALTER TABLE case_action_audit ALTER COLUMN case_type_id TYPE citext;
ALTER TABLE case_action_audit ALTER COLUMN case_jurisdiction_id TYPE citext;


