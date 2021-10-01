package uk.gov.hmcts.reform.laubackend.cases.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;


@Entity(name = "case_search_audit_cases")
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings({"PMD.TooManyFields"})
public class CaseSearchAuditCases implements Serializable {

    public static final long serialVersionUID = 5428747L;

    @Id
    @Column(name = "search_id")
    private Integer searchId;

    @Column(name = "case_ref", nullable = false)
    private String caseRef;

    public CaseSearchAuditCases(Integer searchId, String caseRef) {
        this.searchId = searchId;
        this.caseRef = caseRef;
    }
}
