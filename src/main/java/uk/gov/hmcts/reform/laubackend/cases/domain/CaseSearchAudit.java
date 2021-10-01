package uk.gov.hmcts.reform.laubackend.cases.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;


@Entity(name = "case_search_audit")
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings({"PMD.TooManyFields"})
public class CaseSearchAudit implements Serializable {

    public static final long serialVersionUID = 5428747L;

    @Id
    @Column(name = "search_id")
    @GeneratedValue(strategy = IDENTITY)
    private Integer searchId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "case_ref", nullable = false)
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "case_search_audit_cases", joinColumns = {
        @JoinColumn(name = "search_id")})
    private List<CaseSearchAuditCases> caseRefs = new ArrayList<CaseSearchAuditCases>();

    @Column(name = "log_timestamp", nullable = false)
    private Timestamp timestamp;

    public void addCaseRef(String caseRef) {
        this.caseRefs.add(new CaseSearchAuditCases(caseRef));
    }

    public void setCaseRefs(List<String> caseRefsList) {
        for(String caseRefStr : caseRefsList) {
            addCaseRef(caseRefStr);
        }
    }

    public List<String> getCaseRefs() {
        ArrayList<String> caseRefStrList = new ArrayList<String>();
        for(CaseSearchAuditCases caseRef : caseRefs) {
            String caseRefStr = caseRef.getCaseRef();
            caseRefStrList.add(caseRefStr);
        }
        return caseRefStrList;
    }

}
