package bitnagil.youthPolicy.service;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.infrastructure.youthcenter.YouthPolicyClient;
import bitnagil.infrastructure.youthcenter.dto.YouthPolicyApiResponse;
import bitnagil.infrastructure.youthcenter.dto.YouthPolicyItem;
import bitnagil.youthPolicy.dto.YouthPolicyDoc;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 온통청년 전체 공고를 조회·정규화하여 Redis에 캐싱하는 서비스입니다.
 * 조회 요청은 캐시만 읽고(getAll), 스케줄러가 하루 1회 갱신합니다(refresh).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class YouthPolicyCacheService {

    static final String CACHE_NAME = "youthPolicyAll";

    private static final String RTN_TYPE = "json";
    private static final int FIRST_PAGE = 1;
    private static final int FULL_PAGE_SIZE = 3000; // 전체(약 2,644건)를 한 번에 수신
    private static final int RESULT_CODE_OK = 200;

    @Value("${youthcenter.api-key}")
    private String apiKey;

    private final YouthPolicyClient youthPolicyClient;
    private final YouthPolicyParser youthPolicyParser;

    /**
     * 캐시된 전체 공고를 반환합니다. 캐시가 비어 있으면 한 번 채웁니다.
     */
    @Cacheable(cacheNames = CACHE_NAME, key = "'ALL'", sync = true)
    public List<YouthPolicyDoc> getAll() {
        return fetchAndNormalize();
    }

    /**
     * 전체 공고를 다시 받아 캐시를 덮어씁니다. 스케줄러 전용.
     * 조회에 실패하면 예외로 중단되어 기존 캐시가 유지됩니다.
     */
    @CachePut(cacheNames = CACHE_NAME, key = "'ALL'")
    public List<YouthPolicyDoc> refresh() {
        return fetchAndNormalize();
    }

    private List<YouthPolicyDoc> fetchAndNormalize() {
        YouthPolicyApiResponse response = callYouthCenter(); // 온통청년 API 호출

        if (response.getResultCode() == null || response.getResultCode() != RESULT_CODE_OK) {
            log.error("온통청년 API 비정상 응답. resultCode={}, message={}",
                response.getResultCode(), response.getResultMessage());
            throw new CustomException(ErrorCode.YOUTHCENTER_INVALID_RESPONSE);
        }

        // 온통청년 API 응답에서 공고 목록 추출 후, 정규화
        List<YouthPolicyItem> items = extractItems(response);
        return items.stream()
            .map(youthPolicyParser::parse)
            .toList();
    }

    // 온통청년 API 호출
    private YouthPolicyApiResponse callYouthCenter() {
        try {
            YouthPolicyApiResponse response =
                youthPolicyClient.getPolicies(apiKey, RTN_TYPE, FIRST_PAGE, FULL_PAGE_SIZE);
            if (response == null) {
                throw new CustomException(ErrorCode.YOUTHCENTER_INVALID_RESPONSE);
            }
            return response;
        } catch (FeignException e) {
            log.error("온통청년 API 호출 실패", e);
            throw new CustomException(ErrorCode.YOUTHCENTER_FEIGN_CALL_FAILED);
        }
    }

    private List<YouthPolicyItem> extractItems(YouthPolicyApiResponse response) {
        if (response.getResult() == null || response.getResult().getYouthPolicyList() == null) {
            return List.of();
        }
        return response.getResult().getYouthPolicyList();
    }
}