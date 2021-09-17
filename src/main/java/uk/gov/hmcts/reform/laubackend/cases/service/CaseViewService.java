package uk.gov.hmcts.reform.laubackend.cases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseViewAudit;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseViewAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.utils.InputParamsHolder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.sql.Timestamp.valueOf;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.data.domain.PageRequest.of;
import static uk.gov.hmcts.reform.laubackend.cases.response.CaseViewResponse.caseViewResponse;

@Component
public class CaseViewService {

    @Autowired
    private CaseViewAuditRepository caseViewAuditRepository;

    public CaseViewResponse getCaseView(final InputParamsHolder inputParamsHolder) {

        final Page<CaseViewAudit> caseView = caseViewAuditRepository.findCaseView(
                inputParamsHolder.getUserId(),
                inputParamsHolder.getCaseRef(),
                inputParamsHolder.getCaseTypeId(),
                inputParamsHolder.getCaseJurisdictionId(),
                getTimestampValue(inputParamsHolder.getStartTime()),
                getTimestampValue(inputParamsHolder.getEndTime()),
                getPage(inputParamsHolder.getSize(), inputParamsHolder.getPage())
        );

        final List<ViewLog> viewLogList = new ArrayList<>();
        caseView.getContent().forEach(caseViewAudit -> viewLogList.add(new ViewLog().toDto(caseViewAudit)));

        return caseViewResponse()
                .withViewLog(viewLogList)
                .withMoreRecords(caseView.hasNext())
                .withStartRecordNumber(calculateStartRecordNumber(caseView))
                .build();
    }

    private int calculateStartRecordNumber(final Page<CaseViewAudit> caseView) {
        return caseView.getSize() * caseView.getNumber() + 1;
    }

    private Pageable getPage(final String size, final String page) {
        if (isEmpty(size) && isEmpty(page)) {
            return null;
        }
        return of(parseInt(page) - 1, parseInt(size), Sort.by("timestamp"));
    }

    private Timestamp getTimestampValue(final String timestamp) {
        if (!isEmpty(timestamp)) {
            return valueOf(LocalDateTime.parse(timestamp));
        }
        return null;
    }
}
