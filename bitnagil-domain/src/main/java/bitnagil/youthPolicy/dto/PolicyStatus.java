package bitnagil.youthPolicy.dto;

/**
 * 조회 시점 기준으로 계산되는 공고의 노출 상태입니다.
 */
public enum PolicyStatus {
    OPEN,    // 신청 기간 내(마감 전)
    ALWAYS,  // 상시 모집
    CLOSED   // 마감 (찜 탭에서만 노출)
}