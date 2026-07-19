package bitnagil.infrastructure.youthcenter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 온통청년 청년정책 목록 조회(getPlcy) 응답 DTO입니다.
 * 실제로 사용하는 필드(resultCode, result.youthPolicyList)만 매핑합니다.
 * (전체 목록을 한 번에 받아 캐시하므로 pagging은 사용하지 않습니다.)
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouthPolicyApiResponse {

    private Integer resultCode;
    private String resultMessage;
    private Result result;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private List<YouthPolicyItem> youthPolicyList;
    }
}