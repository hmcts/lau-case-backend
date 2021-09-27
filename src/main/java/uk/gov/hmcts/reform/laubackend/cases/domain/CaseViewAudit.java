package uk.gov.hmcts.reform.laubackend.cases.domain;

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


@Entity(name = "case_view_audit")
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings({"PMD.TooManyFields"})
public class CaseViewAudit implements Serializable {

    public static final long serialVersionUID = 5428747L;

    @Id
    @Column(name = "case_view_id")
    @GeneratedValue(strategy = IDENTITY)
    private Integer caseViewId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "case_ref", nullable = false)
    private String caseRef;

    @Column(name = "case_jurisdiction_id", nullable = false)
    private String caseJurisdictionId;

    @Column(name = "case_type_id", nullable = false)
    private String caseTypeId;

    @Column(name = "log_timestamp", nullable = false)
    private Timestamp timestamp;
}
