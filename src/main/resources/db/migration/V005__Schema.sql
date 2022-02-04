ALTER TABLE case_action_audit ALTER COLUMN case_type_id TYPE VARCHAR(70);
ALTER TABLE case_action_audit ALTER COLUMN case_jurisdiction_id TYPE VARCHAR(70);

-- drop extension safely
DO
$do$
BEGIN
  IF EXISTS (
  SELECT FROM pg_extension  -- SELECT list can be empty for this
    WHERE extname = 'citext') THEN
  DROP EXTENSION IF EXISTS citext;
END IF;
exception
  when sqlstate '42501' then
  RAISE NOTICE 'Dropping citext failed.';
END
$do$;


