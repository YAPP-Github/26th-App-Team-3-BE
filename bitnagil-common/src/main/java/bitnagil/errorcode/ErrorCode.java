package bitnagil.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * ErrorCode는 모든 에러 코드를 정의한 Enum입니다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 에러 코드
    OK("CO000", HttpStatus.OK, "OK"),
    INVALID_PARAMETER("CO001", HttpStatus.BAD_REQUEST, "올바르지 않은 파라미터입니다."),
    RESOURCE_NOT_FOUND("CO002", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("CO003", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    NOT_FOUND_HANDLER("CO004", HttpStatus.NOT_FOUND, "요청한 핸들러를 찾을 수 없습니다."),
    REQUIRED_PARAMETER_NOT_FOUND("CO005", HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다.."),

    // JWT 관련 에러 코드
    FORBIDDEN_USER("JW000", HttpStatus.FORBIDDEN, "필요한 권한이 없는 사용자입니다."),
    UNAUTHENTICATED_USER("JW001", HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_JWT_SIGNATURE("JW002", HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN("JW003", HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN("JW004", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    INVALID_JWT_TOKEN("JW005", HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다."),

    // User 관련 에러 코드
    INACTIVE_USER("US000", HttpStatus.FORBIDDEN, "사용할 수 없는 사용자입니다."),
    NOT_FOUND_USER("US001", HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다. (탈퇴 회원 가능성도 있습니다.)"),

    // User 인증 관련 에러 코드
    AGREEMENT_NOT_ACCEPTED("UA000", HttpStatus.BAD_REQUEST, "필수 약관에 모두 동의해야 서비스를 이용할 수 있습니다."),

    // 소셜 공통 에러 코드
    UNSUPPORTED_SOCIAL_TYPE("SO000", HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 타입입니다."),

    // 애플 로그인 관련 에러 코드
    APPLE_FEIGN_CALL_FAILED("SO001", HttpStatus.BAD_GATEWAY, "애플 소셜 로그인 Feign API 호출에 실패했습니다."),
    APPLE_TOKEN_DECODE_ERROR("SO002", HttpStatus.BAD_REQUEST, "토큰 디코드 중 오류가 발생했습니다."),
    APPLE_PRIVATE_KEY_CONVERT_ERROR("SO003", HttpStatus.INTERNAL_SERVER_ERROR, "개인 키 변환 중 오류가 발생했습니다."),
    APPLE_UNLINK_PENDING("SO004", HttpStatus.BAD_REQUEST, "애플 회원 탈퇴가 진행 중입니다. 잠시 후 다시 시도해주세요."),

    // 카카오 로그인 관련 에러 코드
    KAKAO_USER_INFO_FAILED("KA001", HttpStatus.BAD_REQUEST, "카카오 회원정보 조회 API 호출에 실패했습니다."),
    KAKAO_LOGOUT_FAILED("KA002", HttpStatus.UNAUTHORIZED, "카카오 로그아웃 API 호출에 실패했습니다."),
    KAKAO_UNLINK_FAILED("KA003", HttpStatus.FORBIDDEN, "카카오 회원탈퇴 API 호출에 실패했습니다."),
    KAKAO_FEIGN_CALL_FAILED("KA004", HttpStatus.BAD_GATEWAY, "카카오 서버 Feign Client 호출에 실패했습니다."),

    // 루틴 관련 에러 코드
    NOT_FOUND_ROUTINE("RT001", HttpStatus.NOT_FOUND, "존재하지 않는 루틴입니다."),
    ROUTINE_USER_NOT_MATCHED("RT002", HttpStatus.FORBIDDEN, "루틴의 유저 정보와 로그인 유저 정보가 일치하지 않습니다."),
    NOT_FOUND_ROUTINE_INFO("RT003", HttpStatus.NOT_FOUND, "존재하지 않는 루틴 정보입니다."),
    INVALID_ROUTINE_DATE_RANGE("RT004", HttpStatus.BAD_REQUEST, "루틴의 시작 날짜는 종료 날짜보다 이후일 수 없습니다."),

    // 서브 루틴 관련 에러 코드
    NOT_FOUND_SUB_ROUTINE("SR001", HttpStatus.NOT_FOUND, "해당 복합 키에 맞는 서브 루틴이 존재하지 않습니다."),
    SUB_ROUTINE_USER_NOT_MATCHED("SR002", HttpStatus.FORBIDDEN, "서브루틴의 유저 정보와 로그인 유저 정보가 일치하지 않습니다."),

    // 변경 루틴 관련 에러 코드
    NOT_FOUND_CHANGED_ROUTINE("CR001", HttpStatus.NOT_FOUND, "존재하지 않는 변경 루틴입니다."),
    CHANGED_ROUTINE_USER_NOT_MATCHED("CR002", HttpStatus.FORBIDDEN, "변경 루틴의 유저 정보와 로그인 유저 정보가 일치하지 않습니다."),

    // 변경 서브루틴 관련 에러 코드
    NOT_FOUND_CHANGED_SUB_ROUTINE("CSR001", HttpStatus.NOT_FOUND, "존재하지 않는 변경 서브루틴입니다."),
    CHANGED_SUB_ROUTINE_USER_NOT_MATCHED("CSR002", HttpStatus.FORBIDDEN, "변경 서브루틴의 유저 정보와 로그인 유저 정보가 일치하지 않습니다."),

    // 루틴 완료 여부 관련 에러 코드
    ROUTINE_ID_MISMATCH("RC001", HttpStatus.BAD_REQUEST, "루틴 완료 여부 테이블에서 조회된 routineId와 요청받은 routineId가 다릅니다."),
    NOT_FOUND_ROUTINE_COMPLETION("RC002", HttpStatus.NOT_FOUND, "루틴 완료 여부 데이터를 찾을 수 없습니다."),

    // 온보딩 관련 에러 코드
    NOT_FOUND_RECOMMENDED_ROUTINE("ON000", HttpStatus.NOT_FOUND, "조건에 맞는 추천 루틴을 찾을 수 없습니다."),
    NOT_FOUND_USER_ONBOARDING_INFO("ON001", HttpStatus.NOT_FOUND, "온보딩 정보가 존재하지 않습니다."),

    // 감정구슬 관련 에러코드
    ALREADY_REGISTERED_EMOTION_MARBLE("EM000", HttpStatus.CONFLICT, "감정구슬은 하루에 한번만 등록할 수 있습니다."),

    // 기타 에러 코드
    JSON_CONVERT_ERROR("ETC001", HttpStatus.INTERNAL_SERVER_ERROR, "List를 JSON 문자열로 변환하는 데 실패했습니다."),
    JSON_PARSE_ERROR("ETC001", HttpStatus.INTERNAL_SERVER_ERROR, "JSON 문자열을 List<String> 으로 변환하는 데 실패했습니다."),

    // 제보 관련 에러코드
    NOT_FOUND_REPORT("RP001", HttpStatus.NOT_FOUND, "존재하지 않는 제보입니다."),

    // 안드로이드 버전 관련 에러코드
    NOT_FOUND_ANDROID_APP_VERSION("AAV001", HttpStatus.NOT_FOUND, "안드로이드 앱 버전을 찾을 수 없습니다."),

    // 청년 공고 관련 에러코드
    YOUTHCENTER_FEIGN_CALL_FAILED("AN000", HttpStatus.BAD_GATEWAY, "온통청년 API 호출에 실패했습니다."),
    YOUTHCENTER_INVALID_RESPONSE("AN001", HttpStatus.BAD_GATEWAY, "온통청년 API 응답이 올바르지 않습니다."),
    INVALID_YOUTH_POLICY_CURSOR("AN002", HttpStatus.BAD_REQUEST, "잘못된 커서 값입니다."),
    VWORLD_FEIGN_CALL_FAILED("AN003", HttpStatus.BAD_GATEWAY, "주소 변환 API 호출에 실패했습니다."),
    LOCATION_NOT_RESOLVED("AN004", HttpStatus.BAD_REQUEST, "위치를 시군구로 변환하지 못했습니다.");



    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable throwable) {
        return this.getMessage(this.getMessage(this.getMessage() + " - " + throwable.getMessage()));
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }


}