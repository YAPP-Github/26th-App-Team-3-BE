package bitnagil.youthPolicy.controller.spec;

import bitnagil.errorcode.ErrorCode;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiErrorCodeExamples;
import bitnagil.global.swagger.ApiTags;
import bitnagil.user.domain.User;
import bitnagil.youthPolicy.dto.response.YouthPolicyListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

@Tag(name = ApiTags.YOUTH_POLICY)
public interface YouthPolicySpec {

    @Operation(summary = "청년 공고 목록 조회",
        description = "현재 위/경도를 시군구로 변환해 해당 지역의 공고를 마감 임박순으로 조회합니다. "
            + "마감 공고는 제외되고 상시 공고는 목록 뒤에 위치합니다. 커서 기반 무한 스크롤입니다.")
    @Parameters({
        @Parameter(name = "latitude", description = "현재 위도", required = true, example = "37.5729"),
        @Parameter(name = "longitude", description = "현재 경도", required = true, example = "126.9794"),
        @Parameter(name = "cursor", description = "다음 페이지 커서(첫 페이지는 생략)", example = ""),
        @Parameter(name = "size", description = "페이지 크기(기본 10, 최대 50)", example = "10")
    })
    @ApiErrorCodeExamples({
        ErrorCode.LOCATION_NOT_RESOLVED,
        ErrorCode.INVALID_YOUTH_POLICY_CURSOR,
        ErrorCode.VWORLD_FEIGN_CALL_FAILED,
        ErrorCode.YOUTHCENTER_FEIGN_CALL_FAILED
    })
    CustomResponseDto<YouthPolicyListResponse> getYouthPolicies(
        User user, @NotNull Double latitude, @NotNull Double longitude, String cursor, Integer size);

    @Operation(summary = "찜한 공고 목록 조회",
        description = "사용자가 찜한 공고를 최근 찜한 순으로 조회합니다. 지역 무관이며 마감 공고도 포함됩니다. 커서 기반 무한 스크롤입니다.")
    @Parameters({
        @Parameter(name = "cursor", description = "다음 페이지 커서(첫 페이지는 생략)", example = ""),
        @Parameter(name = "size", description = "페이지 크기(기본 10, 최대 50)", example = "10")
    })
    @ApiErrorCodeExamples({
        ErrorCode.INVALID_YOUTH_POLICY_CURSOR,
        ErrorCode.YOUTHCENTER_FEIGN_CALL_FAILED
    })
    CustomResponseDto<YouthPolicyListResponse> getBookmarkedYouthPolicies(User user, String cursor, Integer size);

    @Operation(summary = "공고 찜 등록", description = "공고를 찜합니다. 이미 찜한 경우에도 성공(멱등)합니다.")
    @Parameter(name = "plcyNo", description = "공고 번호", required = true, example = "20260625005400113245")
    CustomResponseDto<Void> addBookmark(User user, String plcyNo);

    @Operation(summary = "공고 찜 해제", description = "찜을 해제합니다. 찜하지 않은 경우에도 성공(멱등)합니다.")
    @Parameter(name = "plcyNo", description = "공고 번호", required = true, example = "20260625005400113245")
    CustomResponseDto<Void> removeBookmark(User user, String plcyNo);
}