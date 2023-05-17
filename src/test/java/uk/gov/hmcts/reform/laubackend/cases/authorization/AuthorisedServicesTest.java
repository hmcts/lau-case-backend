package uk.gov.hmcts.reform.laubackend.cases.authorization;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.reflect.FieldUtils;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorisedServicesTest {

    private static final String AUTHORISED_SERVICES_LIST = "authorisedServicesList";
    private static final String AUTHORISED_ROLES_LIST = "authorisedRolesList";
    private static final String VALID_AUTHORISED_SERVICE = "lau_case_backend";
    private static final String VALID_AUTHORISED_ROLE = "my_role";

    private static final String INVALID_AUTHORISED_SERVICE = "made_up";
    private static final String INVALID_AUTHORISED_ROLE = "my_invalid_role";

    private static final String AUTHORISED_SUCCESS = "Should return true";
    private static final String AUTHORISED_FAILURE = "Should return false";

    @Autowired
    private AuthorisedServices authorisedServices;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        authorisedServices = new AuthorisedServices();
        final List<String> authorizedServices = asList(VALID_AUTHORISED_SERVICE);
        final List<String> authorizedRoles = asList(VALID_AUTHORISED_ROLE);

        FieldUtils.writeField(authorisedServices, AUTHORISED_SERVICES_LIST, authorizedServices, true);
        FieldUtils.writeField(authorisedServices, AUTHORISED_ROLES_LIST, authorizedRoles, true);
    }

    @Test
    void testKnownAuthorisedServiceSuccess() {
        assertTrue(authorisedServices.hasService(VALID_AUTHORISED_SERVICE), AUTHORISED_SUCCESS);
    }

    @Test
    void testUnknownAuthorisedService() {
        assertFalse(authorisedServices.hasService(INVALID_AUTHORISED_SERVICE), AUTHORISED_FAILURE);
    }

    @Test
    void testKnownAuthorisedRolesSuccess() {
        assertTrue(authorisedServices
                .hasRole(List.of(INVALID_AUTHORISED_ROLE, VALID_AUTHORISED_ROLE)), AUTHORISED_SUCCESS);
    }

    @Test
    void testUnknownAuthorisedRoles() {
        assertFalse(authorisedServices.hasRole(List.of(INVALID_AUTHORISED_ROLE)), AUTHORISED_SUCCESS);
    }
}
