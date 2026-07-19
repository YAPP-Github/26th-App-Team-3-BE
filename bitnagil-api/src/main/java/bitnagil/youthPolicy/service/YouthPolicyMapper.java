package bitnagil.youthPolicy.service;

import bitnagil.youthPolicy.dto.PolicyStatus;
import bitnagil.youthPolicy.dto.RankedYouthPolicy;
import bitnagil.youthPolicy.dto.YouthPolicyDoc;
import bitnagil.youthPolicy.dto.response.YouthPolicyCardResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 도메인 조회 결과(RankedYouthPolicy)를 응답 카드로 변환합니다. dday를 계산합니다.
 */
@Component
public class YouthPolicyMapper {

    public YouthPolicyCardResponse toCard(RankedYouthPolicy ranked, boolean bookmarked, LocalDate today) {
        YouthPolicyDoc doc = ranked.doc();
        PolicyStatus status = ranked.status();
        return YouthPolicyCardResponse.builder()
            .plcyNo(doc.getPlcyNo())
            .title(doc.getTitle())
            .category(doc.getCategory())
            .thumbnailUrl(null)
            .status(status)
            .startDate(doc.getStartDate())
            .endDate(doc.getEndDate())
            .dday(computeDday(status, doc.getEndDate(), today))
            .applyUrl(doc.getApplyUrl())
            .bookmarked(bookmarked)
            .build();
    }

    private Integer computeDday(PolicyStatus status, LocalDate endDate, LocalDate today) {
        if (status != PolicyStatus.OPEN || endDate == null) {
            return null;
        }
        return (int) ChronoUnit.DAYS.between(today, endDate);
    }
}