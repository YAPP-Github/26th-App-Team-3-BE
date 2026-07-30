package bitnagil.youthPolicy.dto;

import java.util.List;

/**
 * 커서 페이징된 공고 조회 결과입니다. 도메인 쿼리 서비스의 반환 DTO입니다.
 */
public record YouthPolicyPage(
    List<RankedYouthPolicy> items,
    int totalCount,
    boolean hasNext,
    String nextCursor
) {
}