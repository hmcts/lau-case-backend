ALTER TABLE access_request_audit DROP COLUMN time_limit;

ALTER TABLE access_request_audit ADD COLUMN request_start_timestamp TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE access_request_audit ADD COLUMN request_end_timestamp TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE access_request_audit ALTER COLUMN request_start_timestamp SET NOT NULL;
ALTER TABLE access_request_audit ALTER COLUMN request_end_timestamp SET NOT NULL;


COMMENT ON COLUMN access_request_audit.request_start_timestamp IS 'when the access should start';
COMMENT ON COLUMN access_request_audit.request_end_timestamp IS 'when the access should end';
