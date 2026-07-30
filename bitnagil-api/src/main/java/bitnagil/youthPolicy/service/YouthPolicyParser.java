package bitnagil.youthPolicy.service;

import bitnagil.infrastructure.youthcenter.dto.YouthPolicyItem;
import bitnagil.youthPolicy.dto.PeriodType;
import bitnagil.youthPolicy.dto.YouthPolicyDoc;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 온통청년 원본 공고 항목(YouthPolicyItem)을 정규화 모델(YouthPolicyDoc)로 변환합니다.
 */
@Component
public class YouthPolicyParser {

    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final String CODE_FIXED = "0057001";
    private static final String CODE_ALWAYS = "0057002";
    private static final String CODE_CLOSED = "0057003";

    // 신청기간이 여러 개일 때 리터럴 "\N"(백슬래시+N)으로 이어붙는 경우가 있다.
    private static final Pattern MULTI_PERIOD_DELIMITER = Pattern.compile(Pattern.quote("\\N"));
    private static final String PERIOD_RANGE_DELIMITER = "~";

    // 청년 공고 원본 항목을 정규화 모델로 변환합니다.
    public YouthPolicyDoc parse(YouthPolicyItem item) {
        PeriodType periodType = toPeriodType(item.getAplyPrdSeCd());
        LocalDate[] period = parsePeriod(item.getAplyYmd(), periodType);

        return YouthPolicyDoc.builder()
            .plcyNo(item.getPlcyNo())
            .title(item.getPlcyNm())
            .category(item.getLclsfNm())
            .periodType(periodType)
            .startDate(period[0])
            .endDate(period[1])
            .zipCodes(parseZipCodes(item.getZipCd()))
            .applyUrl(emptyToNull(item.getAplyUrlAddr()))
            .build();
    }

    private PeriodType toPeriodType(String aplyPrdSeCd) {
        if (CODE_FIXED.equals(aplyPrdSeCd)) {
            return PeriodType.FIXED;
        }
        if (CODE_CLOSED.equals(aplyPrdSeCd)) {
            return PeriodType.CLOSED;
        }
        // 상시(0057002) 및 알 수 없는 값은 상시로 취급(마감일 없음)
        return PeriodType.ALWAYS;
    }

    /**
     * aplyYmd를 파싱해 [startDate, endDate]를 반환합니다.
     * 여러 기간이면 가장 이른 시작일과 가장 늦은 종료일로 합칩니다.
     * FIXED가 아니거나 파싱 불가하면 [null, null].
     */
    private LocalDate[] parsePeriod(String aplyYmd, PeriodType periodType) {
        LocalDate[] empty = {null, null};
        if (periodType != PeriodType.FIXED || aplyYmd == null || aplyYmd.isBlank()) {
            return empty;
        }

        LocalDate minStart = null;
        LocalDate maxEnd = null;
        for (String segment : MULTI_PERIOD_DELIMITER.split(aplyYmd)) {
            int delimiterIndex = segment.indexOf(PERIOD_RANGE_DELIMITER);
            if (delimiterIndex < 0) {
                continue;
            }
            LocalDate start = parseDate(segment.substring(0, delimiterIndex));
            LocalDate end = parseDate(segment.substring(delimiterIndex + 1));
            if (start != null && (minStart == null || start.isBefore(minStart))) {
                minStart = start;
            }
            if (end != null && (maxEnd == null || end.isAfter(maxEnd))) {
                maxEnd = end;
            }
        }
        return new LocalDate[]{minStart, maxEnd};
    }

    private LocalDate parseDate(String raw) {
        String trimmed = raw == null ? "" : raw.trim();
        if (trimmed.length() != 8) { // 빈 문자열, 공백 8칸 등 방어
            return null;
        }
        try {
            return LocalDate.parse(trimmed, YMD);
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> parseZipCodes(String zipCd) {
        if (zipCd == null || zipCd.isBlank()) {
            return new ArrayList<>();
        }
        List<String> codes = new ArrayList<>();
        for (String code : Arrays.asList(zipCd.split(","))) {
            String trimmed = code.trim();
            if (!trimmed.isEmpty()) {
                codes.add(trimmed);
            }
        }
        return codes;
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
