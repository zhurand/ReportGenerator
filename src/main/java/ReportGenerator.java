import java.util.List;
import java.util.Map;
import java.io.IOException;

public interface ReportGenerator {
    void generateReport(String fileName, List<Map<String, String>> data) throws IOException;
}
