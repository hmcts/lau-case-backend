package uk.gov.hmcts.reform.laubackend.cases.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "case_search_audit_cases")
@Getter
@Setter
@NoArgsConstructor
public class CaseSearchAuditCases implements Serializable {

    public static final long serialVersionUID = 5428747L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "case_ref", nullable = false)
    private String caseRef;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "search_id", referencedColumnName = "id", nullable = false)
    private CaseSearchAudit caseSearchAudit;

    public CaseSearchAuditCases(final String caseRef, final CaseSearchAudit caseSearchAudit) {
        this.caseRef = caseRef;
        this.caseSearchAudit = caseSearchAudit;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CaseSearchAuditCases that = (CaseSearchAuditCases) obj;
        return id.equals(that.id) && caseRef.equals(that.caseRef) && caseSearchAudit.equals(that.caseSearchAudit);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
