package report.adapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import report.ReportGenerator;

public class ExcelReportAdapter implements ReportGenerator {
    @Override
    public void generateReport(String fileName, List<Map<String, String>> data) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Отчет по работнику");

        //Создание строки
        Row headerRow = sheet.createRow(0);

        // Создание ячеек в первой строке (заголовки столбцов)
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("Отработанные дни");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Имя");

        // Извлечение данных
        int rowNum = 1;
        for (Map<String, String> dataRow : data) {
            Row row = sheet.createRow(rowNum);
            rowNum++;
            int colNum = 0;
            for (String value : dataRow.values()) {
                Cell cell = row.createCell(colNum);
                cell.setCellValue(value);
                colNum++;
            }
        }

        String filePath = "report.xlsx";

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println("Файл с отчетом успешно создан: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
