  -- Update case_action_audit table to include actions checks .
ALTER TABLE access_request_audit
    ADD CONSTRAINT access_request_audit_action_check CHECK (action in ('CREATED','APPROVED','AUTO-APPROVED','REJECTED'));


