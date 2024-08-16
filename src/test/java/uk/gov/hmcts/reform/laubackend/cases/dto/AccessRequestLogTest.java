package uk.gov.hmcts.reform.laubackend.cases.dto;


import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestAction;
import uk.gov.hmcts.reform.laubackend.cases.constants.AccessRequestType;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AccessRequestLogTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (
            ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
            validator = factory.usingContext().getValidator();
        }
    }

    @Test
    void validAccessRequestShouldNotFailValidation() {
        AccessRequestLog accessRequestLog = AccessRequestLog.builder()
            .requestType(AccessRequestType.CHALLENGED)
            .userId("1234-abcd")
            .caseRef("1234567890123456")
            .reason("I really really need this access")
            .action(AccessRequestAction.CREATED)
            .timestamp("2021-08-01T12:00:00Z")
            .build();

        assertThat(validator.validate(accessRequestLog)).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
        "          , userid, 1234567890123456, reason, CREATED, 2021-08-01T12:00:00Z, 1",
        "CHALLENGED,       , 1234567890123456, reason, CREATED, 2021-08-01T12:00:00Z, 1",
        "CHALLENGED, userid,                 , reason, CREATED, 2021-08-01T12:00:00Z, 1",
        "CHALLENGED, userid, 1234567890123456, reason,        , 2021-08-01T12:00:00Z, 1",
        "CHALLENGED, userid, 1234567890123456, reason, CREATED,                     , 1",
        "          ,       , 1234567890123456, reason, CREATED, 2021-08-01T12:00:00Z, 2",
        "CHALLENGED, userid, invalid         , reason, CREATED, 2021-08-01T12:00:00Z, 1",
        "CHALLENGED, userid, 123456789012    ,       , CREATED,                     , 3",
        "CHALLENGED,       , caseref123456   , reason, CREATED,                     , 3",
        ", only-long-user-id-given-that-exceeds-sixty-four-characters-and-it-should-fail-validation, , , ,  , 6"
    })
    void invalidAccessRequestShouldFailValidation(
        AccessRequestType requestType, String userId, String caseRef, String reason, AccessRequestAction action,
        String timestamp, int expectedErrorCount
    ) {
        AccessRequestLog accessRequestLog = AccessRequestLog.builder()
            .requestType(requestType)
            .userId(userId)
            .caseRef(caseRef)
            .reason(reason)
            .action(action)
            .timestamp(timestamp)
            .build();

        assertThat(validator.validate(accessRequestLog)).hasSize(expectedErrorCount);
    }
}
