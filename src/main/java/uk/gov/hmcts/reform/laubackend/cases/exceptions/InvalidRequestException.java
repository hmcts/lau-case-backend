package uk.gov.hmcts.reform.laubackend.cases.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class InvalidRequestException extends Exception {

    @Serial
    private static final long serialVersionUID = 432973322;

    private final HttpStatus errorCode;

    public InvalidRequestException(final String errorMessage, final HttpStatus errCode) {
        super(errorMessage);
        errorCode = errCode;
    }

}
