package uk.gov.hmcts.reform.laubackend.cases.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;


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

    public CaseActionAudit(String userId, String caseRef, String caseAction, String caseJurisdictionId, String caseTypeId, Timestamp timestamp) {
        this.userId = userId;
        this.caseRef = caseRef;
        this.caseAction = caseAction;
        this.caseJurisdictionId = caseJurisdictionId;
        this.caseTypeId = caseTypeId;
        this.timestamp = timestamp;
    }
}
