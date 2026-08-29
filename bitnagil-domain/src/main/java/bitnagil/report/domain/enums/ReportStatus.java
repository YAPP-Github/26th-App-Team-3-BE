package bitnagil.report.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReportStatus {

    PENDING("제보 완료"),
    IN_PROGRESS("처리 중"),
    COMPLETED("처리 완료");

    private final String description;
}
