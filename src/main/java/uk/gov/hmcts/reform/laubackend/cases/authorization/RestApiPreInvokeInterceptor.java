package uk.gov.hmcts.reform.laubackend.cases.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.hmcts.reform.laubackend.cases.constants.GetCaseActionAccess;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidAuthorizationException;
import uk.gov.hmcts.reform.laubackend.cases.exceptions.InvalidServiceAuthorizationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CaseActionConstants.CASE_ACCESS;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_AUDIT_INVESTIGATOR_ROLE;
import static uk.gov.hmcts.reform.laubackend.cases.constants.CommonConstants.AUTHORISATION_SERVICE_LOGS_ROLE;

@Slf4j
@SuppressWarnings({"PMD.LawOfDemeter"})
public class RestApiPreInvokeInterceptor implements HandlerInterceptor {

    @Autowired
    private ServiceAuthorizationAuthenticator serviceAuthorizationAuthenticator;

    @Autowired
    private AuthorizationAuthenticator authorizationAuthenticator;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws IOException {

        try {
            serviceAuthorizationAuthenticator.authorizeServiceToken(request);

            if (request.getMethod().equalsIgnoreCase(GET.name())
                    || request.getMethod().equalsIgnoreCase(DELETE.name())) {
                List<String> roles = authorizationAuthenticator.authorizeAuthorizationToken(request);
                if (roles.contains(AUTHORISATION_AUDIT_INVESTIGATOR_ROLE)) {
                    request.setAttribute(CASE_ACCESS, GetCaseActionAccess.FULL_ACCESS);
                } else if (roles.contains(AUTHORISATION_SERVICE_LOGS_ROLE)) {
                    request.setAttribute(CASE_ACCESS, GetCaseActionAccess.DELETE_ONLY);
                }
            }

        } catch (final InvalidServiceAuthorizationException exception) {
            log.error("Service authorization token failed due to error - {}",
                    exception.getMessage(),
                    exception);
            response.sendError(SC_FORBIDDEN, exception.getMessage());

            return false;

        } catch (final InvalidAuthorizationException exception) {
            log.error("Authorization token failed due to error - {}",
                    exception.getMessage(),
                    exception);
            response.sendError(SC_UNAUTHORIZED, exception.getMessage());

            return false;
        }
        return true;
    }
}
