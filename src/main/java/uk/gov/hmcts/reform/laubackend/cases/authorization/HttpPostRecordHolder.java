package uk.gov.hmcts.reform.laubackend.cases.authorization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class HttpPostRecordHolder {
    private boolean isPost;
}
