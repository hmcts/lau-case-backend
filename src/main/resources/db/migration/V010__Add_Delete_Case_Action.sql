  -- Update case_action_audit table to include additinal check 'DELETE'.
  ALTER TABLE case_action_audit
    DROP CONSTRAINT IF EXISTS case_action_audit_case_action_check;
  ALTER TABLE case_action_audit
    ADD CONSTRAINT case_action_audit_case_action_check CHECK (case_action in ('VIEW','UPDATE','CREATE','DELETE'));


