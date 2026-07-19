package bitnagil.infrastructure.youthcenter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 온통청년 청년정책 목록의 개별 공고 항목입니다.
 * 전체 60여 개 필드 중 서비스에서 사용하는 필드만 매핑합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouthPolicyItem {

    private String plcyNo;        // 공고 번호(고유 ID)
    private String plcyNm;        // 공고 제목
    private String lclsfNm;       // 대분류(일자리/주거 등). plcyNo 조회 시 null일 수 있음
    private String aplyPrdSeCd;   // 신청기간 구분: 0057001 특정기간 / 0057002 상시 / 0057003 마감
    private String aplyYmd;       // 신청기간 "YYYYMMDD ~ YYYYMMDD" (상시/마감은 빈 문자열)
    private String zipCd;         // 법정시군구코드 목록(콤마 구분)
    private String aplyUrlAddr;   // 신청 페이지 URL
}