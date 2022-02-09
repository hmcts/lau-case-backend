-- Missing index?
--CREATE INDEX case_search_audit_cases_case_ref_idx ON case_search_audit_cases (case_ref);

--  Add column
ALTER TABLE case_search_audit ADD COLUMN case_refs VARCHAR(16)[];
CREATE INDEX case_search_audit_case_refs_idx ON case_search_audit USING GIN(case_refs);

-- case_search_audit_cases comments
comment on column case_search_audit.case_refs is 'String array of case references.';

-- Migration Scripts
UPDATE case_search_audit cs
	  SET case_refs = csa.case_refs
	  FROM (SELECT csa.search_id, array_agg(csa.case_ref) as case_refs
	  FROM case_search_audit_cases csa GROUP BY csa.search_id) AS csa
	  WHERE cs.id=csa.search_id;

-- Drop existing case_search_audit_cases table
DROP TABLE IF EXISTS case_search_audit_cases CASCADE;
