package uk.gov.hmcts.reform.laubackend.cases.domain;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "case_search_audit")
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings({"PMD.TooManyFields","PMD.UnnecessaryAnnotationValueElement","PMD.UnusedAssignment"})
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

    @Type(ListArrayType.class)
    @Column(name = "case_refs", columnDefinition = "bigint[]")
    private List<Long> caseRefs = new ArrayList<>();

    public CaseSearchAudit(final String userId, final Timestamp timestamp, final List<Long> caseRefs) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.caseRefs = caseRefs;
    }

    public void addCaseRef(final Long caseRef) {
        caseRefs.add(caseRef);
    }

}
