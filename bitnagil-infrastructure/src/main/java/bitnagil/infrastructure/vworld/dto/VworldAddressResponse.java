package bitnagil.infrastructure.vworld.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * vworld 지오코더 역변환(getAddress) 응답 DTO입니다.
 * 필요한 필드(status, result[].structure.level4LC)만 매핑합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VworldAddressResponse {

    private Response response;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        // OK / NOT_FOUND / ERROR
        private String status;
        private List<Result> result;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private Structure structure;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Structure {
        // 법정동 코드 10자리 (앞 5자리가 시군구코드)
        private String level4LC;
    }
}
