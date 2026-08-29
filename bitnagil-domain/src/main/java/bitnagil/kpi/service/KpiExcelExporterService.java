package bitnagil.kpi.service;

import bitnagil.kpi.domain.MonthlyKpi;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Component
public class KpiExcelExporterService {

    public byte[] exportMonthlyKpi(MonthlyKpi kpi) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(kpi.getTargetMonth() + "월 KPI");

            int rowIdx = 0;
            Row header = sheet.createRow(rowIdx++);
            header.createCell(0).setCellValue("대상 월");
            header.createCell(1).setCellValue("가입 후 7일 이내 루틴 1회 이상 완료 비율");
            header.createCell(2).setCellValue("월간 1회 이상 루틴 완료 사용자 비율");
            header.createCell(3).setCellValue("루틴 1개 이상 등록 비율");
            header.createCell(4).setCellValue("나가봐요 루틴 1회 이상 완료 비율");
            header.createCell(5).setCellValue("긍정 감정 비율");

            Row row = sheet.createRow(rowIdx);
            row.createCell(0).setCellValue(kpi.getTargetMonth().toString());
            row.createCell(1).setCellValue(toNumber(kpi.getRoutineCompletionWithinSevenDaysRate()));
            row.createCell(2).setCellValue(toNumber(kpi.getMonthlyRoutineActiveUserRate()));
            row.createCell(3).setCellValue(toNumber(kpi.getRoutineRegistrationRate()));
            row.createCell(4).setCellValue(toNumber(kpi.getOutingRoutineCompletionRate()));
            row.createCell(5).setCellValue(toNumber(kpi.getPositiveEmotionRate()));

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("KPI 엑셀 생성 실패", e);
        }
    }

    private double toNumber(BigDecimal value) {
        return value == null ? 0.0 : value.doubleValue();
    }
}

