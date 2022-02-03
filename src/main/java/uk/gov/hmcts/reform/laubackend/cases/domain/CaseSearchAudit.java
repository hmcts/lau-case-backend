package uk.gov.hmcts.reform.laubackend.cases.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "case_search_audit")
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(
        name = "CaseSearch.List",
        attributeNodes = {@NamedAttributeNode(value = "caseSearchAuditCases")}
)
@SuppressWarnings({"PMD.TooManyFields","PMD.UnnecessaryAnnotationValueElement"})
public class CaseSearchAudit implements Serializable {

    public static final long serialVersionUID = 5428747L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "log_timestamp", nullable = false)
    private Timestamp timestamp;

    @OneToMany(
            cascade = ALL,
            mappedBy = "caseSearchAudit",
            orphanRemoval = true)
    private List<CaseSearchAuditCases> caseSearchAuditCases = new ArrayList<>();

    public CaseSearchAudit(final String userId, final Timestamp timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public List<String> getCaserefs() {
        List<String> caseRefs = new ArrayList<>();
        for (CaseSearchAuditCases caseSearchAuditCase : caseSearchAuditCases) {
            caseRefs.add(caseSearchAuditCase.getCaseRef());
        }
        return caseRefs;
    }

    public void addCaseSearchAuditCases(final CaseSearchAuditCases caseSearchAuditCase) {
        caseSearchAuditCase.setCaseSearchAudit(this);
        this.caseSearchAuditCases.add(caseSearchAuditCase);
    }
}
