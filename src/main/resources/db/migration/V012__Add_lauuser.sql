DO
$do$
  BEGIN
    IF NOT EXISTS (
      SELECT FROM pg_catalog.pg_roles  -- SELECT list can be empty for this
      WHERE  rolname = 'lauuser') THEN
      CREATE ROLE lauuser LOGIN PASSWORD '${LAU_DB_PASSWORD}';
    END IF;
  END
$do$;

GRANT USAGE, SELECT ON SEQUENCE case_action_audit_case_action_id_seq TO lauuser;
GRANT USAGE, SELECT ON SEQUENCE case_search_audit_id_seq TO lauuser;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE case_action_audit, case_search_audit, flyway_schema_history TO lauuser;
