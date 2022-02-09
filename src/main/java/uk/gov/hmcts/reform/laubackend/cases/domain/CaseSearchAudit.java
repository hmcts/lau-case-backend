package uk.gov.hmcts.reform.laubackend.cases.domain;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "case_search_audit_new")
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings({"PMD.TooManyFields","PMD.UnnecessaryAnnotationValueElement"})
@TypeDef(
    name = "string-array",
    typeClass = StringArrayType.class
)
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

    @Type(type = "string-array")
    @Column(name = "case_refs", columnDefinition = "varchar[]")
    private String[] caseRefs;

    public CaseSearchAudit(final String userId, final Timestamp timestamp, final String[] caseRefs) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.caseRefs = caseRefs;
    }

}
