package bitnagil.report.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import bitnagil.report.domain.Report;
import bitnagil.user.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUserOrderByUpdatedAtDesc(User user);

    Optional<Report> findByReportIdAndUser(Long reportId, User user);

    // 해당 유저가 특정 기간(연·월)에 등록한 제보 수 (뱃지 월별 발급 판정용)
    long countByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);
}
