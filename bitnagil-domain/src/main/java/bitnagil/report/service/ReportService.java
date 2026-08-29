package bitnagil.report.service;

import bitnagil.badge.domain.enums.BadgeTriggerAction;
import bitnagil.badge.event.BadgeTriggerEvent;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.report.domain.Report;
import bitnagil.report.domain.enums.ReportStatus;
import bitnagil.report.repository.ReportRepository;
import bitnagil.report.dto.request.ReportRegisterRequest;
import bitnagil.report.dto.response.ReportDetailInfoResponse;
import bitnagil.report.dto.response.ReportInfo;
import bitnagil.report.dto.response.ReportInfoResponse;
import bitnagil.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 제보 등록
    @Transactional
    public long registerReport(User user, ReportRegisterRequest request) {
        Report report = Report.builder()
            .reportStatus(ReportStatus.PENDING)
            .reportCategory(request.getReportCategory())
            .reportImageUrls(request.getReportImageUrls())
            .reportContent(request.getReportContent())
            .reportLocation(request.getReportLocation())
            .reportTitle(request.getReportTitle())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .user(user)
            .build();

        reportRepository.save(report);

        eventPublisher.publishEvent(
            new BadgeTriggerEvent(user.getUserId(), BadgeTriggerAction.REPORT_REGISTER, YearMonth.now()));

        return report.getReportId();
    }

    // 제보 전체 목록 조회
    @Transactional(readOnly = true)
    public ReportInfoResponse getAllReportInfo(User user) {
        List<Report> reports = reportRepository.findByUserOrderByUpdatedAtDesc(user);

        Map<LocalDate, List<ReportInfo>> reportInfoMap = new HashMap<>();

        for (Report report : reports) {
            LocalDate reportDate = report.getCreatedAt().toLocalDate();

            ReportInfo reportInfo = ReportInfo.builder()
                .reportId(report.getReportId())
                .reportStatus(report.getReportStatus())
                .reportTitle(report.getReportTitle())
                .reportCategory(report.getReportCategory())
                .reportLocation(report.getReportLocation())
                .reportImageUrl(report.getReportImageUrls().get(0)) // 첫 번째 사진을 썸네일로 임의 설정
                .build();

            List<ReportInfo> reportInfos;
            if (reportInfoMap.containsKey(reportDate)) { // 제보 날짜에 대한 이미 제보 기록이 있는 경우
                reportInfos = reportInfoMap.get(reportDate); // 기존 제보 기록 정의

            } else { // 제보 날짜에 대한 제보 기록이 없는 경우
                reportInfos = new ArrayList<>();
            }

            reportInfos.add(reportInfo); // reportDate에 대한 ReportInfo를 추가
            reportInfoMap.put(reportDate, reportInfos);
        }

        return new ReportInfoResponse(reportInfoMap);
    }

    @Transactional(readOnly = true)
    public ReportDetailInfoResponse getReportDetailInfo(User user, Long reportId) {
        Report report = reportRepository.findByReportIdAndUser(reportId, user).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_REPORT));

        return ReportDetailInfoResponse.builder()
            .reportDate(report.getCreatedAt().toLocalDate())
            .reportStatus(report.getReportStatus())
            .reportTitle(report.getReportTitle())
            .reportContent(report.getReportContent())
            .reportCategory(report.getReportCategory())
            .reportLocation(report.getReportLocation())
            .reportImageUrls(report.getReportImageUrls())
            .build();
    }

    /* 추후에 변경을 고려해서 소스만 남겨놓음
    @Transactional
    public void updateImages(Long reportId, List<String> urls) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REPORT));

        report.updateReportImageUrls(urls);
    }
    */
}

