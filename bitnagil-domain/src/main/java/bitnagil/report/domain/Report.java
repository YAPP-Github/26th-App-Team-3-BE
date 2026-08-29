package bitnagil.report.domain;

import bitnagil.utils.StringListConverter;
import bitnagil.report.domain.enums.ReportCategory;
import bitnagil.report.domain.enums.ReportStatus;
import bitnagil.user.domain.User;
import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@SQLDelete(sql = "UPDATE report SET deleted_at = NOW() WHERE report_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private ReportStatus reportStatus;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private ReportCategory reportCategory;

    @Convert(converter = StringListConverter.class)
    private List<String> reportImageUrls;

    private String reportTitle;

    private String reportContent;

    private String reportLocation;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Report(ReportStatus reportStatus, ReportCategory reportCategory, List<String> reportImageUrls,
        String reportTitle, String reportContent, String reportLocation, BigDecimal latitude, BigDecimal longitude,
        User user) {
        this.reportStatus = reportStatus;
        this.reportCategory = reportCategory;
        this.reportImageUrls = reportImageUrls;
        this.reportTitle = reportTitle;
        this.reportContent = reportContent;
        this.reportLocation = reportLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }

    public void updateReportImageUrls(List<String> urls) {
        this.reportImageUrls = urls;
    }
}
