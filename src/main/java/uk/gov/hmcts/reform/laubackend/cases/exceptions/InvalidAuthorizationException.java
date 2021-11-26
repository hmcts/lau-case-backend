package uk.gov.hmcts.reform.laubackend.cases.exceptions;

public class InvalidAuthorizationException extends RuntimeException {
    private static final long serialVersionUID = -4L;

    public InvalidAuthorizationException(String message) {
        super(message);
    }
}
