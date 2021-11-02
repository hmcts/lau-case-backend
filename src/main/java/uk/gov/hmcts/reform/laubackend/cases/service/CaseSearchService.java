package uk.gov.hmcts.reform.laubackend.cases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAuditCases;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseSearchAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse.caseSearchResponse;

@Service
public class CaseSearchService {

    @Autowired
    private CaseSearchAuditRepository caseSearchAuditRepository;

    @Autowired
    private TimestampUtil timestampUtil;

    public CaseSearchGetResponse getCaseSearch(final SearchInputParamsHolder inputParamsHolder) {

        final Page<CaseSearchAudit> caseSearch = caseSearchAuditRepository.findCaseSearch(
                inputParamsHolder.getUserId(),
                inputParamsHolder.getCaseRef(),
                timestampUtil.getTimestampValue(inputParamsHolder.getStartTime()),
                timestampUtil.getTimestampValue(inputParamsHolder.getEndTime()),
                getPage(inputParamsHolder.getSize(), inputParamsHolder.getPage())
        );

        final List<SearchLog> searchLogList = new ArrayList<>();

        caseSearch.getContent().forEach(caseSearchAudit -> {
            final String timestamp = timestampUtil.timestampConvertor(caseSearchAudit.getTimestamp());
            searchLogList.add(
                    new SearchLog().toDto(caseSearchAudit, timestamp)
            );
        });

        return caseSearchResponse()
                .withSearchLog(searchLogList)
                .withMoreRecords(caseSearch.hasNext())
                .withStartRecordNumber(calculateStartRecordNumber(caseSearch))
                .build();
    }

    public CaseSearchPostRequest saveCaseSearch(final CaseSearchPostRequest
                                                        caseSearchPostRequest) {

        final CaseSearchAudit caseSearchAuditRequest = new CaseSearchAudit(caseSearchPostRequest
                .getSearchLog()
                .getUserId(),
                timestampUtil.getUtcTimestampValue(caseSearchPostRequest.getSearchLog().getTimestamp()));

        if (!isEmpty(caseSearchPostRequest.getSearchLog().getCaseRefs())) {
            caseSearchPostRequest.getSearchLog().getCaseRefs()
                    .forEach(caseRef -> caseSearchAuditRequest
                    .addCaseSearchAuditCases(new CaseSearchAuditCases(caseRef, caseSearchAuditRequest)));
        }

        final CaseSearchAudit caseSearchAuditResponse = caseSearchAuditRepository.save(caseSearchAuditRequest);
        final String timestamp = timestampUtil.timestampConvertor(caseSearchAuditResponse.getTimestamp());
        return new CaseSearchPostRequest(new SearchLog().toDto(caseSearchAuditResponse, timestamp));
    }

    private int calculateStartRecordNumber(final Page<CaseSearchAudit> caseView) {
        return caseView.getSize() * caseView.getNumber() + 1;
    }

    private Pageable getPage(final String size, final String page) {
        final String pageSize = Optional.ofNullable(size).orElse("10000");
        final String pageNumber = Optional.ofNullable(page).orElse("1");

        return of(parseInt(pageNumber) - 1, parseInt(pageSize), Sort.by("timestamp"));
    }
}
