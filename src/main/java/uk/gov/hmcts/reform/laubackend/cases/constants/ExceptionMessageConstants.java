package uk.gov.hmcts.reform.laubackend.cases.constants;

import static java.lang.String.valueOf;

public final class ExceptionMessageConstants {

    //GET exception messages
    public static final String TIMESTAMP_GET_EXCEPTION_MESSAGE = "Unable to verify timestamp path parameter pattern: ";
    public static final String CASEREF_GET_EXCEPTION_MESSAGE = "Unable to verify caseRef path parameter pattern: ";
    public static final String USERID_GET_EXCEPTION_MESSAGE = "Unable to verify userId path parameter pattern: ";
    public static final String CASETYPEID_GET_EXCEPTION_MESSAGE = "Unable to verify caseTypeId "
           + "path parameter pattern: ";
    public static final String CASEACTION_GET_EXCEPTION_MESSAGE = "Unable to verify caseAction pattern: ";
    public static final String CASE_JURISDICTION_GET_EXCEPTION_MESSAGE = "Unable to verify "
            + "caseJurisdictionId path parameter pattern: ";

    //POST exception messages
    public static final String TIMESTAMP_POST_EXCEPTION_MESSAGE = "Unable to verify timestamp pattern: ";
    public static final String CASEREF_POST_EXCEPTION_MESSAGE = "Unable to verify caseRef pattern: ";
    public static final String USERID_POST_EXCEPTION_MESSAGE = "Unable to verify userId pattern: ";
    public static final String CASETYPEID_POST_EXCEPTION_MESSAGE = "Unable to verify caseTypeId pattern: ";
    public static final String CASE_ACTION_POST_EXCEPTION_MESSAGE = "Unable to verify action pattern: ";
    public static final String CASE_JURISDICTION_POST_EXCEPTION_MESSAGE = "Unable to verify "
            + "caseJurisdictionId pattern: ";


    public static String appendExceptionParameter(final String exceptionMessage,
                                                  final String exceptionParameter) {
        //Use String.valueOf(Object) to avoid NullPointerException
        return exceptionMessage.concat(valueOf(exceptionParameter));
    }

    private ExceptionMessageConstants() {
    }
}
