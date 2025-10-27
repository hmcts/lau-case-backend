DROP INDEX concurrently IF EXISTS
  case_action_audit_timestamp_jurisdiction_idx;

DROP INDEX concurrently IF EXISTS
  case_action_audit_timestamp_case_type_idx;

CREATE INDEX concurrently IF NOT EXISTS
  case_action_audit_case_type_id_timestamp_case_type_idx
ON case_action_audit USING btree (case_type_id, log_timestamp);

CREATE INDEX concurrently IF NOT EXISTS
  case_action_audit_jurisdiction_timestamp_jurisdiction_idx
ON case_action_audit USING btree (case_jurisdiction_id, log_timestamp)
