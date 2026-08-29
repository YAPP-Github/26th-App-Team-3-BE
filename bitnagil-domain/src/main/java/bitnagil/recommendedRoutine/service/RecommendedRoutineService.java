package bitnagil.recommendedRoutine.service;

import bitnagil.emotionMarble.domain.EmotionMarble;
import bitnagil.emotionMarble.repository.EmotionMarbleRepository;
import bitnagil.onboarding.domain.Onboarding;
import bitnagil.recommendedRoutine.domain.RecommendedRoutine;
import bitnagil.recommendedRoutine.domain.RecommendedSubRoutine;
import bitnagil.recommendedRoutine.domain.enums.RecommendedRoutineType;
import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineSearchResponse;
import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineSearchResult;
import bitnagil.recommendedRoutine.dto.response.RecommendedSubRoutineSearchResult;
import bitnagil.recommendedRoutine.repository.RecommendedRoutineRepository;
import bitnagil.recommendedRoutine.repository.RecommendedSubRoutineRepository;
import bitnagil.user.domain.User;
import bitnagil.user.service.UserManager;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendedRoutineService {

    private final RecommendedRoutineRepository recommendedRoutineRepository;
    private final RecommendedSubRoutineRepository recommendedSubRoutineRepository;
    private final EmotionMarbleRepository emotionMarbleRepository;

    private final RecommendedRoutineMapper recommendedRoutineMapper;
    private final RecommendedRoutineFactory recommendedRoutineFactory;
    private final UserManager userManager;

    /**
     * 추천 카테고리별 루틴, 서브루틴을 조회
     * key: 추천 루틴 타입 value: 추천 루틴, 서브 루틴 리스트
     */
    @Transactional(readOnly = true)
    public RecommendedRoutineSearchResponse searchRecommendedRoutines(User user) {

        LocalDate nowDate = LocalDate.now();

        // 맞춤추천을 제외한 이외의 카테고리에 대한 추천 루틴을 response 추가
        Map<String, List<RecommendedRoutineSearchResult>> response = recommendedRoutineFactory.addCategoryRecommendedRoutines();

        // 카테고리 별 추천루틴에 대한 response 객체 생성
        response.put(RecommendedRoutineType.PERSONALIZED.name(), new ArrayList<>()); // 맞춤 루틴은 미리 초기화 한다.(감정구슬, 온보딩 결과를 넣기 위해)

        // 영속성 객체에 user를 저장하기 위해 user를 조회
        User persistedUser = userManager.getPersistedUser(user);

        // 맞춤 추천(감정구슬 + 온보딩)을 조회하고 response에 추가
        EmotionMarble emotionMarble = addPersonalizedRecommendedRoutine(persistedUser, nowDate, response);

        return recommendedRoutineMapper.toRecommendedRoutineSearchResponse(response, emotionMarble);
    }

    /**
     * 추천 루틴 단건 조회
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "recommendedRoutine", key = "#recommendedRoutineId")
    public RecommendedRoutineSearchResult searchRecommendedRoutine(Long recommendedRoutineId) {
        RecommendedRoutine recommendedRoutine = recommendedRoutineRepository.findById(recommendedRoutineId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECOMMENDED_ROUTINE));

        List<RecommendedSubRoutineSearchResult> recommendedSubRoutineSearchResults = new ArrayList<>();
        // 추천 루틴에 해당하는 서브루틴 조회
        List<RecommendedSubRoutine> recommendedSubRoutines = recommendedSubRoutineRepository.findByRecommendedRoutine(recommendedRoutine);
        for (RecommendedSubRoutine recommendedSubRoutine : recommendedSubRoutines) {
            RecommendedSubRoutineSearchResult recommendedSubRoutineSearchResult = recommendedRoutineMapper.toRecommendedSubRoutineSearchResult(recommendedSubRoutine);
            recommendedSubRoutineSearchResults.add(recommendedSubRoutineSearchResult);
        }

        return recommendedRoutineMapper.toRecommendedRoutineSearchResult(recommendedRoutine, recommendedSubRoutineSearchResults);
    }

    private EmotionMarble addPersonalizedRecommendedRoutine(User user, LocalDate nowDate,
        Map<String, List<RecommendedRoutineSearchResult>> response) {
        // 감정구슬(당일에 감정구슬을 선택한 경우만 조회)
        EmotionMarble emotionMarble = emotionMarbleRepository.findByUserIdAndDateIs(user.getUserId(), nowDate);
        if(emotionMarble != null) { // 조회 결과가 존재하는 경우
            recommendedRoutineFactory.makeEmotionMarbleResponse(emotionMarble, response);
        }

        // 온보딩 결과에 따른 추천 루틴 조회
        Onboarding onboarding = user.getOnboarding();
        if (onboarding != null) { // 온보딩을 수행한 유저의 경우(온보딩은 필수지만 방어 로직으로 추가)
            recommendedRoutineFactory.makeOnboardingResponse(onboarding, response);
        }
        return emotionMarble;
    }
}

