package bitnagil.youthPolicy.dto;

/**
 * 조회 시점 기준 상태가 계산된 공고입니다. 정렬·페이징의 단위인 도메인 서비스 DTO입니다.
 */
public record RankedYouthPolicy(YouthPolicyDoc doc, PolicyStatus status) {
}