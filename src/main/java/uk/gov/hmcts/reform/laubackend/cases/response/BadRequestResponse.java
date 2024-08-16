package uk.gov.hmcts.reform.laubackend.cases.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BadRequestResponse {

    private static final String TITLE = "Bad Request";

    private static final int STATUS = 400;

    private static final String DETAIL = "Invalid request content";

    private List<String> errors;

    public String getTitle() {
        return TITLE;
    }

    public int getStatus() {
        return STATUS;
    }

    public String getDetail() {
        return DETAIL;
    }
}
