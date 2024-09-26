package uk.gov.hmcts.reform.laubackend.cases.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "access_request_audit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "request_type", nullable = false)
    private String requestType;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "case_ref", nullable = false)
    private String caseRef;

    @Column(nullable = false)
    private String reason;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "request_start_timestamp")
    private Timestamp requestStart;

    @Column(name = "request_end_timestamp")
    private Timestamp requestEnd;

    @Column(name = "log_timestamp", nullable = false)
    private Timestamp timestamp;
}
