--  Add column
ALTER TABLE case_search_audit ADD COLUMN case_refs bigint[];
CREATE INDEX case_search_audit_case_refs_idx ON case_search_audit USING GIN(case_refs);

-- case_search_audit new column comments
comment on column case_search_audit.case_refs is 'bigint array of case references.';

-- Migration Scripts
UPDATE case_search_audit cs
	  SET case_refs = csa.case_refs
	  FROM (SELECT csa.search_id, array_agg(csa.case_ref::bigint) as case_refs
	  FROM case_search_audit_cases csa WHERE (csa.case_ref <> '') IS TRUE GROUP BY csa.search_id) AS csa
	  WHERE cs.id=csa.search_id;

-- Drop existing case_search_audit_cases table
--DROP TABLE IF EXISTS case_search_audit_cases CASCADE;
