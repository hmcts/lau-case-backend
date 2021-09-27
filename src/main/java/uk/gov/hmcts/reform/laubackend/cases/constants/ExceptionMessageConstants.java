package uk.gov.hmcts.reform.laubackend.cases.constants;

public final class ExceptionMessageConstants {

    //CaseView POST exception messages
    public static final String TIMESTAMP_GET_EXCEPTION_MESSAGE = "Unable to verify timestamp path parameter pattern";
    public static final String CASEREF_GET_EXCEPTION_MESSAGE = "Unable to verify caseRef path parameter pattern";
    public static final String USERID_GET_EXCEPTION_MESSAGE = "Unable to verify userId path parameter pattern";
    public static final String CASETYPEID_GET_EXCEPTION_MESSAGE = "Unable to verify caseTypeId path parameter pattern";
    public static final String CASE_JURISDICTION_GET_EXCEPTION_MESSAGE = "Unable to verify "
            + "caseJurisdictionId path parameter pattern";

    //CaseView POST exception messages
    public static final String TIMESTAMP_POST_EXCEPTION_MESSAGE = "Unable to verify ViewLog timestamp pattern";
    public static final String CASEREF_POST_EXCEPTION_MESSAGE = "Unable to verify ViewLog caseRef pattern";
    public static final String USERID_POST_EXCEPTION_MESSAGE = "Unable to verify ViewLog userId pattern";
    public static final String CASETYPEID_POST_EXCEPTION_MESSAGE = "Unable to verify ViewLog caseTypeId pattern";
    public static final String CASE_JURISDICTION_POST_EXCEPTION_MESSAGE = "Unable to verify ViewLog "
            + "caseJurisdictionId pattern";

    private ExceptionMessageConstants() {
    }
}
