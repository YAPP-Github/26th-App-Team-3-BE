package bitnagil.emotionMarble.controller;

import bitnagil.emotionMarble.controller.spec.EmotionMarbleSpec;
import bitnagil.emotionMarble.dto.request.RegisterEmotionMarbleRequest;
import bitnagil.emotionMarble.dto.response.EmotionMarbleTypeResponse;
import bitnagil.emotionMarble.dto.response.EmotionMarbleTypeResponseV2;
import bitnagil.emotionMarble.dto.response.RegisterEmotionMarbleResponse;
import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.emotionMarble.service.EmotionMarbleService;
import bitnagil.user.domain.User;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class EmotionMarbleController implements EmotionMarbleSpec {
    private final EmotionMarbleService emotionMarbleService;

    // 감정구슬 조회 API
    @GetMapping("/v1/emotion-marbles")
    public CustomResponseDto<List<EmotionMarbleTypeResponse>> getEmotionMarbles() {
        return CustomResponseDto.from(emotionMarbleService.getEmotionMarbles());
    }

    // 감정구슬 등록 API
    @PostMapping("/v1/emotion-marbles")
    public CustomResponseDto<RegisterEmotionMarbleResponse> registryEmotionMarble(
        @CurrentUser User user,
        @RequestBody RegisterEmotionMarbleRequest request) {

        return CustomResponseDto.from(emotionMarbleService.registryEmotionMarble(user, request));
    }

    // todo: 당일의 유저가 선택한 감정 구슬 조회 API V2로 변환
    @GetMapping("/v2/emotion-marbles/{searchDate}")
    public CustomResponseDto<EmotionMarbleTypeResponseV2> getEmotionMarbleBySearchDateV2(
            @CurrentUser User user,
            @PathVariable LocalDate searchDate) {

        return CustomResponseDto.from(emotionMarbleService.getEmotionMarbleBySearchDateV2(user, searchDate));
    }

    // 당일의 유저가 선택한 감정 구슬 조회 API
    // TODO: v2로 전환 시 deprecated 처리
    @Deprecated()
    @GetMapping("/v1/emotion-marbles/{searchDate}")
    public CustomResponseDto<EmotionMarbleTypeResponse> getEmotionMarbleBySearchDate(
        @CurrentUser User user,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate) {

        return CustomResponseDto.from(emotionMarbleService.getEmotionMarbleBySearchDate(user, searchDate));
    }
}
