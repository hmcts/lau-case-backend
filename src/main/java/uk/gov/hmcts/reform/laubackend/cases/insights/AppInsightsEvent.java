package uk.gov.hmcts.reform.laubackend.cases.insights;

public enum AppInsightsEvent {
    GET_SEARCH_REQUEST_INFO("Audit Case Search GET Request Info: "),
    GET_SEARCH_REQUEST_INVALID_REQUEST_EXCEPTION("Audit GET Case Search Requst InvalidRequestException: "),
    GET_ACTIVITY_REQUEST_INFO("Audit GET Case Activity Request Info: "),
    GET_ACTIVITY_REQUEST_EXCEPTION("Audit GET Case Activity Request Exception: "),
    GET_ACTIVITY_REQUEST_INVALID_REQUEST_EXCEPTION("Audit GET Case Activity Requst InvalidRequestException: "),
    POST_SEARCH_REQUEST_EXCEPTION("Audit POST Case Search Request Exception: "),
    POST_SEARCH_REQUEST_INVALID_REQUEST_EXCEPTION("Audit POST Case Search Requst InvalidRequestException: "),
    POST_ACTIVITY_REQUEST_EXCEPTION("Audit POST Case Activity Request Exception: "),
    POST_ACTIVITY_REQUEST_INVALID_REQUEST_EXCEPTION("Audit POST Case Activity Requst InvalidRequestException: ");

    private final String displayName;

    AppInsightsEvent(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
