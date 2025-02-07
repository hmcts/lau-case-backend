CREATE INDEX concurrently IF NOT EXISTS
  case_action_audit_timestamp_jurisdiction_idx
ON case_action_audit (log_timestamp, case_jurisdiction_id);

CREATE INDEX concurrently IF NOT EXISTS
  case_action_audit_timestamp_case_type_idx
    ON case_action_audit (log_timestamp, case_type_id);
