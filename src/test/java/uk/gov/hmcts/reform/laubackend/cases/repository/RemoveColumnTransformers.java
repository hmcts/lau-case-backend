package uk.gov.hmcts.reform.laubackend.cases.repository;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseActionAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAudit;
import uk.gov.hmcts.reform.laubackend.cases.domain.CaseSearchAuditCases;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import static com.gilecode.reflection.ReflectionAccessUtils.getReflectionAccessor;
import static java.lang.reflect.Proxy.getInvocationHandler;

@Component
@SuppressWarnings({"all"})
public class RemoveColumnTransformers implements HibernatePropertiesCustomizer {

    @SneakyThrows
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        final Field idFieldForCaseActionAudit = CaseActionAudit.class.getDeclaredField("caseActionId");
        final Field idFieldForCaseSearchAudit = CaseSearchAudit.class.getDeclaredField("id");
        final Field idFieldForCaseSearchAuditCases = CaseSearchAuditCases.class.getDeclaredField("id");

        final GeneratedValue idGeneratedValueForCaseActionAudit = idFieldForCaseActionAudit
            .getDeclaredAnnotation(GeneratedValue.class);

        final GeneratedValue idGeneratedValueForCaseSearchAudit = idFieldForCaseSearchAudit
            .getDeclaredAnnotation(GeneratedValue.class);

        final GeneratedValue idGeneratedValueForCaseSearchAuditCases = idFieldForCaseSearchAuditCases
            .getDeclaredAnnotation(GeneratedValue.class);

        updateAnnotationValue(idGeneratedValueForCaseActionAudit, "strategy", GenerationType.AUTO);
        updateAnnotationValue(idGeneratedValueForCaseSearchAudit, "strategy", GenerationType.AUTO);
        updateAnnotationValue(idGeneratedValueForCaseSearchAuditCases, "strategy", GenerationType.AUTO);
    }

    @SuppressWarnings({"unchecked"})
    private void updateAnnotationValue(final Annotation annotation,
                                       final String annotationProperty,
                                       final Object value) throws Exception {
        final Object handler = getInvocationHandler(annotation);
        final Field memberValuesField = handler.getClass().getDeclaredField("memberValues");

        getReflectionAccessor().makeAccessible(memberValuesField);

        final Map<String, Object> memberValues = (Map<String, Object>) memberValuesField.get(handler);

        memberValues.put(annotationProperty, value);
    }
}
