package uk.gov.hmcts.reform.laubackend.cases.service;

import lombok.extern.slf4j.Slf4j;
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
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.SearchLogPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.lang.Long.valueOf;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse.caseSearchResponse;

@Service
@Slf4j
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
                .withTotalNumberOfRecords(caseSearch.getTotalElements())
                .build();
    }

    public CaseSearchPostResponse saveCaseSearch(final CaseSearchPostRequest
                                                         caseSearchPostRequest) {

        final CaseSearchAudit caseSearchAuditRequest = new CaseSearchAudit(caseSearchPostRequest
                .getSearchLog()
                .getUserId(),
                timestampUtil.getUtcTimestampValue(caseSearchPostRequest.getSearchLog().getTimestamp()),
                caseSearchPostRequest.getSearchLog().getCaseRefs().toArray(new String[0])
        );

        final CaseSearchAudit caseSearchAuditResponse = caseSearchAuditRepository.save(caseSearchAuditRequest);
        final String timestamp = timestampUtil.timestampConvertor(caseSearchAuditResponse.getTimestamp());
        return new CaseSearchPostResponse(new SearchLogPostResponse().toDto(caseSearchAuditResponse, timestamp));
    }

    public void deleteCaseSearchBbyId(final String id) {
        caseSearchAuditRepository.deleteById(valueOf(id));
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
