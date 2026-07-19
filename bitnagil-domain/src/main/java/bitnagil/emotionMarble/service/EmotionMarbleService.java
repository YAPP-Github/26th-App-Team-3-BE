package bitnagil.emotionMarble.service;

import bitnagil.emotionMarble.domain.EmotionMarble;
import bitnagil.emotionMarble.domain.enums.EmotionMarbleType;
import bitnagil.emotionMarble.dto.request.RegisterEmotionMarbleRequest;
import bitnagil.emotionMarble.dto.response.EmotionMarbleDailyResponse;
import bitnagil.emotionMarble.dto.response.EmotionMarbleTypeResponse;
import bitnagil.emotionMarble.dto.response.EmotionMarbleTypeResponseV2;
import bitnagil.emotionMarble.dto.response.RegisterEmotionMarbleResponse;
import bitnagil.emotionMarble.repository.EmotionMarbleRepository;
import bitnagil.badge.domain.enums.BadgeTriggerAction;
import bitnagil.badge.event.BadgeTriggerEvent;
import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineDto;
import bitnagil.recommendedRoutine.service.RecommendedRoutineManager;
import bitnagil.user.domain.User;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 감정 구슬에 대한 로직을 관리하는 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class EmotionMarbleService {

    private final EmotionMarbleRepository emotionMarbleRepository;

    private final RecommendedRoutineManager recommendedRoutineManager;
    private final EmotionMarbleFactory emotionMarbleFactory;
    private final EmotionMarbleMapper emotionMarbleMapper;
    private final ApplicationEventPublisher eventPublisher;

    // 감정 구술 조회(enum의 value를 가져온다.)
    public List<EmotionMarbleTypeResponse> getEmotionMarbles() {
        return Arrays.stream(EmotionMarbleType.values())
                .map(emotionMarbleType -> emotionMarbleMapper.toEmotionMarbleTypeResponse(emotionMarbleType))
                .collect(Collectors.toList());
    }

    // 감정 구슬 등록(1일 1회)
    @Transactional
    public RegisterEmotionMarbleResponse registryEmotionMarble(User user, RegisterEmotionMarbleRequest request) {
        LocalDate nowDate = LocalDate.now();
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.of(nowDate, LocalTime.of(23, 59, 59));

        // 감정구슬은 1일 1회만 선택할 수 있으므로, 존재 여부를 확인한다.
        if (emotionMarbleRepository.existsByUserIdAndDate(user.getUserId(), nowDate)) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_EMOTION_MARBLE);
        }

        EmotionMarble emotionMarble = emotionMarbleFactory.createTodayEmotionMarble(
            user, request, nowDate, nowDateTime, endDateTime);

        emotionMarbleRepository.save(emotionMarble);

        eventPublisher.publishEvent(
            new BadgeTriggerEvent(user.getUserId(), BadgeTriggerAction.EMOTION_MARBLE_SELECT, YearMonth.from(nowDate)));

        // 감정 구슬에 따른 추천 루틴 응답
        List<RecommendedRoutineDto> recommendedRoutineDtoList =
            recommendedRoutineManager.recommendRoutinesByEmotionMarble(emotionMarble.getResultCase());

        return RegisterEmotionMarbleResponse.builder()
                .recommendedRoutines(recommendedRoutineDtoList)
                .build();
    }

    @Transactional(readOnly = true)
    public EmotionMarbleTypeResponseV2 getEmotionMarbleBySearchDateV2(User user, LocalDate searchDate) {
        EmotionMarble emotionMarble = emotionMarbleRepository.findByUserIdAndDateIs(user.getUserId(), searchDate);

        return emotionMarbleMapper.toEmotionMarbleTypeResponseV2(emotionMarble);
    }

    @Transactional(readOnly = true)
    public EmotionMarbleTypeResponse getEmotionMarbleBySearchDate(User user, LocalDate searchDate) {
        EmotionMarble emotionMarble = emotionMarbleRepository.findByUserIdAndDateIs(user.getUserId(), searchDate);

        return emotionMarbleMapper.toEmotionMarbleTypeResponse(emotionMarble);
    }

    // 지정한 기간(startDate ~ endDate)에 선택한 일별 감정 구슬 목록 조회 (선택한 날짜만 포함, 날짜 오름차순)
    @Transactional(readOnly = true)
    public List<EmotionMarbleDailyResponse> getDailyEmotionMarbles(User user, LocalDate startDate, LocalDate endDate) {
        return emotionMarbleRepository.findByUserIdAndDateBetweenOrderByDateAsc(user.getUserId(), startDate, endDate)
            .stream()
            .map(emotionMarbleMapper::toDailyResponse)
            .toList();
    }
}
