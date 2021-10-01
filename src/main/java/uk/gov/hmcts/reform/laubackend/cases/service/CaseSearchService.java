package uk.gov.hmcts.reform.laubackend.cases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseSearchAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.SearchLogPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

@Service
public class CaseSearchService {

    @Autowired
    private CaseSearchAuditRepository caseSearchAuditRepository;

    @Autowired
    private TimestampUtil timestampUtil;

    public CaseSearchPostResponse saveCaseSearch(final SearchLog searchLog) {

        final CaseSearchAudit caseSearchAudit = new CaseSearchAudit();
        caseSearchAudit.setUserId(searchLog.getUserId());
        caseSearchAudit.setCaseRefs(searchLog.getCaseRefs());
        caseSearchAudit.setTimestamp(timestampUtil.getUtcTimestampValue(searchLog.getTimestamp()));

        final CaseSearchAudit caseSearchAuditResponse = caseSearchAuditRepository.save(caseSearchAudit);
        final String timestamp = timestampUtil.timestampConvertor(caseSearchAudit.getTimestamp());

        return new CaseSearchPostResponse(new SearchLogPostResponse().toDto(caseSearchAuditResponse, timestamp));
    }
}
