package uk.gov.hmcts.reform.laubackend.cases.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity(name = "case_action_audit")
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings({"PMD.TooManyFields"})
public class CaseActionAudit implements Serializable {

    public static final long serialVersionUID = 5428747L;

    @Id
    @Column(name = "case_action_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long caseActionId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "case_ref")
    private String caseRef;

    @Column(name = "case_action", nullable = false)
    private String caseAction;

    @Column(name = "case_jurisdiction_id")
    private String caseJurisdictionId;

    @Column(name = "case_type_id")
    private String caseTypeId;

    @Column(name = "log_timestamp", nullable = false)
    private Timestamp timestamp;

    public CaseActionAudit(final String userId,
                           final String caseRef,
                           final String caseAction,
                           final String caseJurisdictionId,
                           String caseTypeId) {
        this.userId = userId;
        this.caseRef = caseRef;
        this.caseAction = caseAction;
        this.caseJurisdictionId = caseJurisdictionId;
        this.caseTypeId = caseTypeId;
    }
}
