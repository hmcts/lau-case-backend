ALTER TABLE access_request_audit ALTER COLUMN request_start_timestamp DROP NOT NULL;
ALTER TABLE access_request_audit ALTER COLUMN request_end_timestamp DROP NOT NULL;
