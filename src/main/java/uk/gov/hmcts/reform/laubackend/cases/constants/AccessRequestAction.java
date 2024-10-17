package uk.gov.hmcts.reform.laubackend.cases.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static java.util.Arrays.stream;


public enum AccessRequestAction {
    CREATED("CREATED"),
    APPROVED("APPROVED"),
    AUTO_APPROVED("AUTO-APPROVED"),
    REJECTED("REJECTED"),
    ;

    private final String value;

    AccessRequestAction(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AccessRequestAction getAccessRequestAction(String value) {
        return stream(AccessRequestAction.values())
            .filter(action -> action.getValue().equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No AccessRequestAction constant with name: " + value));
    }
}
