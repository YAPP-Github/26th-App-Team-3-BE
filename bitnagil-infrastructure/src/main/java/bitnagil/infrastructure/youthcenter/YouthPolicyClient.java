package bitnagil.infrastructure.youthcenter;

import bitnagil.infrastructure.youthcenter.dto.YouthPolicyApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 온통청년(청년정책) Feign 클라이언트입니다.
 * 청년정책 목록을 조회합니다. pageSize를 크게 주면 한 번에 전체를 받을 수 있습니다.
 */
@Component
@FeignClient(
    name = "youth-policy",
    url = "${youthcenter.base-url}"
)
public interface YouthPolicyClient {

    /**
     * 청년정책 목록을 조회합니다.
     *
     * @param apiKeyNm 발급받은 api key
     * @param rtnType  응답 포맷 (json)
     * @param pageNum  페이지 번호
     * @param pageSize 페이지 크기 (전체를 한 번에 받으려면 총건수 이상)
     */
    @GetMapping("/go/ythip/getPlcy")
    YouthPolicyApiResponse getPolicies(
        @RequestParam("apiKeyNm") String apiKeyNm,
        @RequestParam("rtnType") String rtnType,
        @RequestParam("pageNum") int pageNum,
        @RequestParam("pageSize") int pageSize
    );
}