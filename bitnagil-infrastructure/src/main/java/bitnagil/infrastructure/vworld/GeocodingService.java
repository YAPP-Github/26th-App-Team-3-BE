package bitnagil.infrastructure.vworld;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.infrastructure.vworld.dto.VworldAddressResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * vworld 지오코더를 이용해 좌표(위/경도)를 법정시군구코드(5자리)로 변환하는 서비스입니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeocodingService {

    // v-world 요청 파라미터
    private static final String SERVICE = "address"; //요청 서비스명
    private static final String VERSION = "2.0"; // 요청 서비스 버전
    private static final String REQUEST = "getAddress"; // 요청 서비스 오퍼레이션
    private static final String FORMAT = "json"; // 응답결과 포맷
    private static final String CRS = "EPSG:4326"; // 좌표계
    private static final String TYPE = "parcel"; // 지번주소 → 법정동 코드(level4LC) 반환
    private static final String STATUS_OK = "OK"; // 응답 성공
    private static final String STATUS_NOT_FOUND = "NOT_FOUND"; // 결과 없음
    private static final int SIGUNGU_CODE_LENGTH = 5;

    @Value("${vworld.api-key}")
    private String apiKey;

    private final VworldGeocoderClient vworldGeocoderClient;

    /**
     * 좌표를 법정시군구코드(법정동 코드 앞 5자리)로 변환합니다.
     * 대한민국 범위 밖과 같이 변환할 주소가 없으면 빈 Optional을 반환합니다.
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return 시군구코드 5자리(예: "11110"), 없으면 Optional.empty()
     */
    public Optional<String> resolveSigunguCode(double latitude, double longitude) {
        String point = longitude + "," + latitude; // vworld는 "경도,위도"(x,y) 순서

        VworldAddressResponse.Response response = callVworld(point);

        if (STATUS_NOT_FOUND.equals(response.getStatus())) {
            return Optional.empty();
        }
        if (!STATUS_OK.equals(response.getStatus())) {
            log.error("vworld 역지오코딩 비정상 응답. status={}, point={}", response.getStatus(), point);
            throw new CustomException(ErrorCode.VWORLD_FEIGN_CALL_FAILED);
        }

        return extractSigunguCode(response.getResult());
    }

    private VworldAddressResponse.Response callVworld(String point) {
        try {
            VworldAddressResponse body = vworldGeocoderClient.getAddress(
                SERVICE, VERSION, REQUEST, apiKey, FORMAT, null, point, CRS, TYPE, null, null, null
            );
            if (body == null || body.getResponse() == null) {
                throw new CustomException(ErrorCode.VWORLD_FEIGN_CALL_FAILED);
            }
            return body.getResponse();
        } catch (FeignException e) {
            log.error("vworld 역지오코딩 호출 실패. point={}", point, e);
            throw new CustomException(ErrorCode.VWORLD_FEIGN_CALL_FAILED);
        }
    }

    // 시군구코드 추출
    private Optional<String> extractSigunguCode(List<VworldAddressResponse.Result> results) {
        if (results == null || results.isEmpty()) {
            return Optional.empty();
        }
        VworldAddressResponse.Structure structure = results.get(0).getStructure();
        if (structure == null || structure.getLevel4LC() == null
            || structure.getLevel4LC().length() < SIGUNGU_CODE_LENGTH) {
            return Optional.empty();
        }
        return Optional.of(structure.getLevel4LC().substring(0, SIGUNGU_CODE_LENGTH));
    }
}