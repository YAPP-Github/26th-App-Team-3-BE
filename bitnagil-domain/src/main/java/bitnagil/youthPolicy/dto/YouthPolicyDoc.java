package bitnagil.youthPolicy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * 온통청년 공고를 정규화한 모델입니다. Redis 캐시에 저장되는 단위입니다.
 * 캐시 직렬화(activateDefaultTyping)를 위해 기본 생성자 + getter/setter를 갖는 평범한 클래스로 둡니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouthPolicyDoc {

    private String plcyNo;
    private String title;
    private String category;        // 대분류(nullable)
    private PeriodType periodType;
    private LocalDate startDate;    // 상시/마감이면 null 가능
    private LocalDate endDate;      // 상시면 null
    private List<String> zipCodes;  // 법정시군구코드 목록
    private String applyUrl;        // 신청 URL(nullable)
}
