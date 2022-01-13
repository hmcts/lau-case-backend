package uk.gov.hmcts.reform.laubackend.cases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.ActionLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseActionAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.response.ActionLogPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseActionPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.lang.Long.valueOf;
import static org.springframework.data.domain.PageRequest.of;
import static uk.gov.hmcts.reform.laubackend.cases.response.CaseActionGetResponse.caseViewResponse;

@Service
public class CaseActionService {

    @Autowired
    private CaseActionAuditRepository caseActionAuditRepository;

    @Autowired
    private TimestampUtil timestampUtil;

    public CaseActionGetResponse getCaseView(final ActionInputParamsHolder inputParamsHolder) {

        final Page<CaseActionAudit> caseView = caseActionAuditRepository.findCaseView(
                inputParamsHolder.getUserId(),
                inputParamsHolder.getCaseRef(),
                inputParamsHolder.getCaseTypeId(),
                inputParamsHolder.getCaseJurisdictionId(),
                timestampUtil.getTimestampValue(inputParamsHolder.getStartTime()),
                timestampUtil.getTimestampValue(inputParamsHolder.getEndTime()),
                getPage(inputParamsHolder.getSize(), inputParamsHolder.getPage())
        );

        final List<ActionLog> actionLogList = new ArrayList<>();

        caseView.getContent().forEach(caseViewAudit -> {
            final String timestamp = timestampUtil.timestampConvertor(caseViewAudit.getTimestamp());
            actionLogList.add(
                    new ActionLog().toDto(caseViewAudit, timestamp)
            );
        });

        return caseViewResponse()
                .withActionLog(actionLogList)
                .withMoreRecords(caseView.hasNext())
                .withStartRecordNumber(calculateStartRecordNumber(caseView))
                .withTotalNumberOfRecords(caseView.getTotalElements())
                .build();
    }

    public CaseActionPostResponse saveCaseAction(final ActionLog actionLog) {

        final CaseActionAudit caseActionAudit = new CaseActionAudit();
        caseActionAudit.setUserId(actionLog.getUserId());
        caseActionAudit.setCaseAction(actionLog.getCaseAction());
        caseActionAudit.setCaseRef(actionLog.getCaseRef());
        caseActionAudit.setCaseJurisdictionId(actionLog.getCaseJurisdictionId());
        caseActionAudit.setCaseTypeId(actionLog.getCaseTypeId());
        caseActionAudit.setTimestamp(timestampUtil.getUtcTimestampValue(actionLog.getTimestamp()));

        final CaseActionAudit caseActionAuditResponse = caseActionAuditRepository.save(caseActionAudit);
        final String timestamp = timestampUtil.timestampConvertor(caseActionAudit.getTimestamp());

        return new CaseActionPostResponse(new ActionLogPostResponse().toDto(caseActionAuditResponse, timestamp));
    }

    public void deleteCaseActionById(final String id) {
        caseActionAuditRepository.deleteById(valueOf(id));
    }

    private int calculateStartRecordNumber(final Page<CaseActionAudit> caseView) {
        return caseView.getSize() * caseView.getNumber() + 1;
    }

    private Pageable getPage(final String size, final String page) {
        final String pageSize = Optional.ofNullable(size).orElse("10000");
        final String pageNumber = Optional.ofNullable(page).orElse("1");

        return of(parseInt(pageNumber) - 1, parseInt(pageSize), Sort.by("timestamp"));
    }
}
