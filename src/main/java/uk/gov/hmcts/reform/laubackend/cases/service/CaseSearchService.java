package uk.gov.hmcts.reform.laubackend.cases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAuditCases;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseSearchAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.util.List;

@Service
public class CaseSearchService {

    @Autowired
    private CaseSearchAuditRepository caseSearchAuditRepository;

    @Autowired
    private TimestampUtil timestampUtil;


    public CaseSearchPostRequest saveCaseSearch(final CaseSearchPostRequest
                                                        caseSearchPostRequest) {

        final CaseSearchAudit caseSearchAuditRequest = new CaseSearchAudit(caseSearchPostRequest
                .getSearchLog()
                .getUserId(),
                timestampUtil.getUtcTimestampValue(caseSearchPostRequest.getSearchLog().getTimestamp()));

        final List<String> caseRefs = caseSearchPostRequest.getSearchLog().getCaseRefs();

        caseRefs.forEach(caseRef -> caseSearchAuditRequest
                .addCaseSearchAuditCases(new CaseSearchAuditCases(caseRef, caseSearchAuditRequest)));


        final CaseSearchAudit caseSearchAuditResponse = caseSearchAuditRepository.save(caseSearchAuditRequest);
        final String timestamp = timestampUtil.timestampConvertor(caseSearchAuditResponse.getTimestamp());
        return new CaseSearchPostRequest(new SearchLog().toDto(caseSearchAuditResponse, timestamp));
    }
}
