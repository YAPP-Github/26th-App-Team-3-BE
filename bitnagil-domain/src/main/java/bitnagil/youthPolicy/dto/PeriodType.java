package bitnagil.youthPolicy.dto;

/**
 * 공고의 신청기간 구분입니다. 온통청년 aplyPrdSeCd 값을 정규화한 것입니다.
 */
public enum PeriodType {
    FIXED,   // 특정기간 (0057001) — 신청 시작/마감일 존재
    ALWAYS,  // 상시 (0057002)
    CLOSED   // 마감 (0057003)
}