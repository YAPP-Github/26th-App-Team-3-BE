package bitnagil.emotionMarble.controller.spec;

import bitnagil.emotionMarble.dto.request.RegisterEmotionMarbleRequest;
import bitnagil.emotionMarble.dto.response.EmotionMarbleTypeResponse;
import bitnagil.emotionMarble.dto.response.EmotionMarbleTypeResponseV2;
import bitnagil.emotionMarble.dto.response.RegisterEmotionMarbleResponse;
import bitnagil.errorcode.ErrorCode;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiErrorCodeExamples;
import bitnagil.global.swagger.ApiTags;
import bitnagil.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = ApiTags.EMOTION_MARBLE)
public interface EmotionMarbleSpec {

    @Operation(summary = "감정 구슬을 조회합니다")
    public CustomResponseDto<List<EmotionMarbleTypeResponse>> getEmotionMarbles();

    @Operation(summary = "감정 구슬을 등록합니다. 감정 구슬에 따른 추천 루틴을 응답합니다.")
    @ApiErrorCodeExamples({ErrorCode.NOT_FOUND_RECOMMENDED_ROUTINE})
    public CustomResponseDto<RegisterEmotionMarbleResponse> registryEmotionMarble(
        User user, RegisterEmotionMarbleRequest request);

    @Operation(summary = "(V2) 검색 날짜 기준으로 대한 유저의 감정구슬 정보를 조회합니다.")
    @Parameters({
            @Parameter(name = "searchDate", description = "감정 구슬 조회 날짜", required = true, example = "2025-08-15",
                    in = ParameterIn.PATH)
    })
    CustomResponseDto<EmotionMarbleTypeResponseV2> getEmotionMarbleBySearchDateV2(
            User user, @PathVariable LocalDate searchDate);

    @Operation(summary = "검색 날짜 기준으로 대한 유저의 감정구슬 정보를 조회합니다.")
    @Parameters({
        @Parameter(name = "searchDate", description = "감정 구슬 조회 날짜", required = true, example = "2025-07-01",
            in = ParameterIn.PATH)
    })
    CustomResponseDto<EmotionMarbleTypeResponse> getEmotionMarbleBySearchDate(
        User user, @PathVariable LocalDate searchDate);
}
