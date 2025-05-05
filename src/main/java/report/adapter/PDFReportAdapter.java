package report.adapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import report.ReportGenerator;

public class PDFReportAdapter implements ReportGenerator {
    // Ссылка на файл шрифта с поддержкой кириллицы
    public static final String FONT = "/arialuni.ttf";
    @Override
    public void generateReport(String fileName, List<Map<String, String>> data) throws IOException {

        // Загрузка шрифта из ресурсов
        InputStream fontStream = PDFReportAdapter.class.getResourceAsStream(FONT);
        if (fontStream == null) {
            System.err.println("В папке ресурсов отсутствует шрифт: " + FONT);
            return;
        }

        // Прочитать InputStream в byte[]
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] dataByte = new byte[1024];
        while ((nRead = fontStream.read(dataByte, 0, dataByte.length)) != -1) {
            buffer.write(dataByte, 0, nRead);
        }
        byte[] fontBytes = buffer.toByteArray();

        /*
         * Метод createFont() ожидает строку, представляющую путь к файлу шрифта, но в используемой версии iText
         * он не имеет перегрузки, принимающей InputStream напрямую.
         * Чтобы решить эту проблему, нужно сначала прочитать данные из InputStream в byte[] (массив байтов),
         * а затем использовать FontProgramFactory.createFont(byte[]).
         */
        FontProgram fontProgram = FontProgramFactory.createFont(fontBytes);
        PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);
        fontStream.close();

        try {
            String reportName = fileName + ".pdf";

            PdfWriter writer = new PdfWriter(reportName);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);

            document.add(new Paragraph("Отработанные дни | Имя |").setFont(font));
            for (Map<String, String> dataRow : data) {
                StringBuilder rowText = new StringBuilder();
                for (String value : dataRow.values()) {
                    rowText.append(value).append(" | ");
                }
                document.add(new Paragraph(rowText.toString()).setFont(font));
            }

            document.close();

            System.out.println("Файл с отчетом успешно создан: " + reportName);

        } catch (FileNotFoundException e) {
            System.err.println("Ошибка создания отчета: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
