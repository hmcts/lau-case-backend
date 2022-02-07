-- Remove duplicate indexes covered by other table constaints.
drop index concurrently case_action_audit_action_idx;
drop index concurrently case_search_audit_cases_search_id_idx;
