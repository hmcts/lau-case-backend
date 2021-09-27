package uk.gov.hmcts.reform.laubackend.cases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseViewAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.InputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ViewLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseViewAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseViewPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.ViewLogPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static org.springframework.data.domain.PageRequest.of;
import static uk.gov.hmcts.reform.laubackend.cases.response.CaseViewGetResponse.caseViewResponse;

@Service
public class CaseViewService {

    @Autowired
    private CaseViewAuditRepository caseViewAuditRepository;

    @Autowired
    private TimestampUtil timestampUtil;

    public CaseViewGetResponse getCaseView(final InputParamsHolder inputParamsHolder) {

        final Page<CaseViewAudit> caseView = caseViewAuditRepository.findCaseView(
                inputParamsHolder.getUserId(),
                inputParamsHolder.getCaseRef(),
                inputParamsHolder.getCaseTypeId(),
                inputParamsHolder.getCaseJurisdictionId(),
                timestampUtil.getTimestampValue(inputParamsHolder.getStartTime()),
                timestampUtil.getTimestampValue(inputParamsHolder.getEndTime()),
                getPage(inputParamsHolder.getSize(), inputParamsHolder.getPage())
        );

        final List<ViewLog> viewLogList = new ArrayList<>();

        caseView.getContent().forEach(caseViewAudit -> {
            final String timestamp = timestampUtil.timestampConvertor(caseViewAudit.getTimestamp());
            viewLogList.add(
                    new ViewLog().toDto(caseViewAudit, timestamp)
            );
        });

        return caseViewResponse()
                .withViewLog(viewLogList)
                .withMoreRecords(caseView.hasNext())
                .withStartRecordNumber(calculateStartRecordNumber(caseView))
                .build();
    }

    public CaseViewPostResponse saveCaseView(final ViewLog viewLog) {

        final CaseViewAudit caseViewAudit = new CaseViewAudit();
        caseViewAudit.setUserId(viewLog.getUserId());
        caseViewAudit.setCaseRef(viewLog.getCaseRef());
        caseViewAudit.setCaseJurisdictionId(viewLog.getCaseJurisdictionId());
        caseViewAudit.setCaseTypeId(viewLog.getCaseTypeId());
        caseViewAudit.setTimestamp(timestampUtil.getUtcTimestampValue(viewLog.getTimestamp()));

        final CaseViewAudit caseViewAuditResponse = caseViewAuditRepository.save(caseViewAudit);
        final String timestamp = timestampUtil.timestampConvertor(caseViewAudit.getTimestamp());

        return new CaseViewPostResponse(new ViewLogPostResponse().toDto(caseViewAuditResponse, timestamp));
    }


    private int calculateStartRecordNumber(final Page<CaseViewAudit> caseView) {
        return caseView.getSize() * caseView.getNumber() + 1;
    }

    private Pageable getPage(final String size, final String page) {
        final String pageSize = Optional.ofNullable(size).orElse("10000");
        final String pageNumber = Optional.ofNullable(page).orElse("1");

        return of(parseInt(pageNumber) - 1, parseInt(pageSize), Sort.by("timestamp"));
    }
}
