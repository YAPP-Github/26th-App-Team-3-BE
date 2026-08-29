package bitnagil.infrastructure.vworld;

import bitnagil.infrastructure.vworld.dto.VworldAddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * vworld 지오코더 Feign 클라이언트입니다.
 * 좌표(위/경도)를 주소로 역변환(getAddress)하며, 지번주소의 법정동 코드를 얻는 데 사용합니다.
 */
@Component
@FeignClient(
    name = "vworld-geocoder",
    url = "${vworld.base-url:https://api.vworld.kr}"
)
public interface VworldGeocoderClient {

    /**
     * 좌표를 주소로 역변환합니다.
     *
     * @param service     요청 서비스명 (address)
     * @param version     요청 서비스 버전 (2.0)
     * @param request     요청 오퍼레이션 (getAddress)
     * @param key         발급받은 api key
     * @param format      응답결과 포맷 (json, xml)
     * @param errorFormat 에러 응답결과 포맷 (json, xml)
     * @param point       주소를 찾을 좌표. 포맷: "경도,위도" (x,y)
     * @param crs         응답결과 좌표계 (EPSG:4326)
     * @param type        검색 주소 유형 (PARCEL, ROAD, BOTH)
     * @param zipcode     우편번호 반환 여부 (true, false)
     * @param simple      응답결과 간략 출력 여부 (true, false)
     * @param callback    format이 json일 경우 callback 함수
     */
    @GetMapping("/req/address")
    VworldAddressResponse getAddress(
        @RequestParam(value = "service", required = false) String service,
        @RequestParam(value = "version", required = false) String version,
        @RequestParam("request") String request,
        @RequestParam("key") String key,
        @RequestParam(value = "format", required = false) String format,
        @RequestParam(value = "errorFormat", required = false) String errorFormat,
        @RequestParam("point") String point,
        @RequestParam(value = "crs", required = false) String crs,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "zipcode", required = false) String zipcode,
        @RequestParam(value = "simple", required = false) String simple,
        @RequestParam(value = "callback", required = false) String callback
    );
}