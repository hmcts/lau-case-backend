-- LAU-305 Sevices creating audit without caase_type_id / case_ref.
ALTER TABLE case_action_audit ALTER COLUMN case_type_id DROP NOT NULL;
ALTER TABLE case_action_audit ALTER COLUMN case_ref DROP NOT NULL;

-- Drop existing case_search_audit_cases table
DROP TABLE IF EXISTS case_search_audit_cases CASCADE;
