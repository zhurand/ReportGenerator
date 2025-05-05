import report.ReportGenerator;
import report.adapter.ExcelReportAdapter;
import report.adapter.PDFReportAdapter;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Map<String, String>> reportData = new ArrayList<>();

        boolean continueAdding = true;

        System.out.println("ОТЧЕТ");

        while (continueAdding) {

            Scanner scanner = new Scanner(System.in);

            Map<String, String> personData = new HashMap<>();

            System.out.print("Введите имя сотрудника: ");
            personData.put("Name", scanner.nextLine());

            System.out.print("Введите количество отработанных сотрудником дней: ");
            personData.put("workingDays", scanner.nextLine());

            reportData.add(personData);

            System.out.print("Добавить в отчет еще сотрудника? (1 - да/любые другие символы - нет): ");
            String answer = scanner.nextLine();

            if (!answer.equalsIgnoreCase("1")) {
                continueAdding = false;
            }
        }

        System.out.println("Выберите формат отчета:");
        System.out.println("1 : EXCEL");
        System.out.println("2 : PDF");

        Scanner scanner = new Scanner(System.in);

        int reportFormat = scanner.nextInt();
        scanner.nextLine();

        ReportGenerator reportGenerator;
        switch (reportFormat) {
            case 1:
                reportGenerator = new ExcelReportAdapter();
                break;
            case 2:
                reportGenerator = new PDFReportAdapter();
                break;
            default:
                System.out.println("Указан неверный формат. Очет будет создан в формате по умолчанию");
                reportGenerator = new ExcelReportAdapter();
        }

        String reportFileName = "report";
        try {
            reportGenerator.generateReport(reportFileName, reportData);
        } catch (IOException e) {
            System.err.println("Ошибка создания отчета: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
