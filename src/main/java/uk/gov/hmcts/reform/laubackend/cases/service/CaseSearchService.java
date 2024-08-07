package uk.gov.hmcts.reform.laubackend.cases.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchInputParamsHolder;
import uk.gov.hmcts.reform.laubackend.cases.dto.SearchLog;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseSearchAuditFindCaseRepository;
import uk.gov.hmcts.reform.laubackend.cases.repository.CaseSearchAuditRepository;
import uk.gov.hmcts.reform.laubackend.cases.request.CaseSearchPostRequest;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.response.SearchLogPostResponse;
import uk.gov.hmcts.reform.laubackend.cases.utils.TimestampUtil;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Long.valueOf;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.data.domain.PageRequest.of;
import static uk.gov.hmcts.reform.laubackend.cases.response.CaseSearchGetResponse.caseSearchResponse;
import static uk.gov.hmcts.reform.laubackend.cases.utils.CaseSearchHelper.convertCaseRefsToLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class CaseSearchService {

    private final CaseSearchAuditRepository caseSearchAuditRepository;

    private final CaseSearchAuditFindCaseRepository caseSearchAuditFindCaseRepository;

    private final TimestampUtil timestampUtil;

    @Value("${default.page.size}")
    private String defaultPageSize;

    public CaseSearchGetResponse getCaseSearch(final SearchInputParamsHolder inputParamsHolder) {

        final Page<CaseSearchAudit> caseSearch = caseSearchAuditFindCaseRepository.findCaseSearch(
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
                convertCaseRefsToLong(caseSearchPostRequest.getSearchLog().getCaseRefs())
        );

        final CaseSearchAudit caseSearchAuditResponse = caseSearchAuditRepository.save(caseSearchAuditRequest);
        final String timestamp = timestampUtil.timestampConvertor(caseSearchAuditResponse.getTimestamp());
        return new CaseSearchPostResponse(new SearchLogPostResponse().toDto(caseSearchAuditResponse, timestamp));
    }

    public void deleteCaseSearchById(final String id) {
        caseSearchAuditRepository.deleteById(valueOf(id));
    }

    public void verifyCaseSearchExists(final String id) {
        if (caseSearchAuditRepository.findById(valueOf(id)).isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    private int calculateStartRecordNumber(final Page<CaseSearchAudit> caseView) {
        return caseView.getSize() * caseView.getNumber() + 1;
    }

    private Pageable getPage(final String size, final String page) {
        final String pageSize = isEmpty(size) ? defaultPageSize : size.trim();
        final String pageNumber = isEmpty(page) ? "1" : page.trim();

        return of(parseInt(pageNumber) - 1, parseInt(pageSize));
    }
}
