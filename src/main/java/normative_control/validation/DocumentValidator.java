package normative_control.validation;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import ru.nsu.fit.chernyavtseva.assistant.Main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static normative_control.notifications.Notifications.ANSI_BLACK;
import static normative_control.notifications.Notifications.ANSI_GREEN;
import static normative_control.notifications.Notifications.ANSI_RED;
import static normative_control.notifications.Notifications.DOCUMENT_ERROR;
import static normative_control.notifications.Notifications.DOCUMENT_SUCCESS;
import static normative_control.notifications.Notifications.FONT_SIZE_ERROR;
import static normative_control.notifications.Notifications.FORMAT_CONVERTION_ERROR;
import static normative_control.notifications.Notifications.NUMBER_CONVERTION_ERROR;
import static normative_control.notifications.Notifications.PAGES_ERROR;
import static normative_control.notifications.Notifications.PATH_ERROR;
import static normative_control.notifications.Notifications.PYTHON_CALL_ERROR;
import static normative_control.notifications.Notifications.PYTHON_EXECUTION_ERROR;
import static normative_control.notifications.Notifications.READING_FILE_ERROR;
import static normative_control.notifications.Notifications.SPARQL_ERROR;
import static normative_control.notifications.Notifications.STYLE_ERROR;
import static normative_control.notifications.Notifications.THEME_ERROR;
import static normative_control.notifications.Notifications.VALIDATION_END;

public class DocumentValidator {
    private static final String MODEL_FILENAME = "/normocontrol.owl";
    private static final String PYTHON_SCRIPT = "C:\\Users\\User\\Desktop\\hepler\\hepler\\python_scripts\\theme_validator.py";
    public static ValidatorData extractFromSparql(String sparqlQuery) {
        try {
            Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            try (InputStream in = Main.class.getResourceAsStream(MODEL_FILENAME)) {
                model.read(in, "RDF/XML");
            }
            Query query = QueryFactory.create(sparqlQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            ResultSet results = qexec.execSelect();
            int minPages = 0;
            String font = null;
            String sizeRange = null;
            String style = null;
            while(results.hasNext()){
                QuerySolution solution = results.next();
                minPages = solution.getLiteral("Минимальное_количество_страниц_отчета").getInt();
                sizeRange = solution.getLiteral("Размер").getString();
                style = solution.getLiteral("Стиль").getString();
            }
            qexec.close();

            return new ValidatorData(minPages, font, sizeRange, style);
        } catch (Exception e) {
            System.err.println(SPARQL_ERROR + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static void validateDocxFiles(String directoryPath, ValidatorData data) {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            System.err.println(PATH_ERROR);
            return;
        }
        for (File file : dir.listFiles((d, name) -> name.toLowerCase().endsWith(".docx"))) {
            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {

                int pageCount = document.getParagraphs().size() / 30;
                var fontSize = getFontSize(document);
                String fontStyle = getFontStyle(document);
                String themeText = extractThemeText(document);

                boolean valid = true;
                boolean sizeIsValid = Arrays.stream(parseSizeRange(data.sizeRange)).anyMatch(size -> size == fontSize);
                StringBuilder errorMessage = new StringBuilder();

                if (pageCount < data.minPages) {
                    valid = false;
                    errorMessage.append(PAGES_ERROR).append(data.minPages).append(", есть: ").append(pageCount).append("). ");
                }
                if (!sizeIsValid) {
                    valid = false;
                    errorMessage.append(FONT_SIZE_ERROR).append(data.sizeRange).append(", есть: ").append(fontSize).append("). ");
                }
                if (!fontStyle.equals(data.style)) {
                    valid = false;
                    errorMessage.append(STYLE_ERROR).append(data.style).append(", есть: ").append(fontStyle).append("). ");
                }
                if (!isValidTheme(themeText)) {
                    valid = false;
                    errorMessage.append(THEME_ERROR);
                }
                if (valid) {
                    System.out.println(ANSI_GREEN + file.getName() + DOCUMENT_SUCCESS);
                } else {
                    System.out.println(ANSI_RED + file.getName() + DOCUMENT_ERROR + errorMessage);
                }
            } catch (IOException e) {
                System.err.println(READING_FILE_ERROR + file.getName() + ": " + e.getMessage());
            }
        }
        System.out.println(ANSI_BLACK + VALIDATION_END);
    }
    private static String getFontStyle(XWPFDocument document) {
        Map<String, Integer> fontCounts = new HashMap<>();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String font = run.getFontFamily();
                fontCounts.put(font, fontCounts.getOrDefault(font, 0) + 1);
            }
        }
        String mostPopularFont = fontCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return mostPopularFont;
    }
    private static double getFontSize(XWPFDocument document) {
        Map<Double, Integer> fontCounts = new HashMap<>();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                var size = run.getFontSizeAsDouble();
                fontCounts.put(size, fontCounts.getOrDefault(size, 0) + 1);
            }
        }
        double mostPopularSize = fontCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return mostPopularSize;
    }
    private static double[] parseSizeRange(String sizeRangeStr) {
        if (sizeRangeStr == null || sizeRangeStr.trim().isEmpty()) {
            return new double[0];
        }
        String[] parts = sizeRangeStr.split("-");
        if (parts.length != 2) {
            System.err.println(FORMAT_CONVERTION_ERROR + sizeRangeStr);
            return null;
        }
        try {
            double start = Integer.parseInt(parts[0].trim());
            double end = Integer.parseInt(parts[1].trim());
            return DoubleStream.of(start, end).toArray();
        } catch (NumberFormatException e) {
            System.err.println(NUMBER_CONVERTION_ERROR + e.getMessage());
            return null;
        }
    }
    private static String extractThemeText(XWPFDocument document) {
        String text = document.getParagraphs().stream()
                .map(XWPFParagraph::getText)
                .collect(Collectors.joining(" "));

        Pattern pattern = Pattern.compile("Тема задания:(.*?)Место прохождения практики:");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return "";
        }
    }
    private static boolean isValidTheme(String themeText) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", PYTHON_SCRIPT, themeText);
            processBuilder.environment().put("PYTHONIOENCODING", "UTF-8");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            String result = reader.readLine();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println(PYTHON_EXECUTION_ERROR + exitCode + ")");
            }
            return result != null && result.equals("valid");
        } catch (IOException | InterruptedException e) {
            System.err.println(PYTHON_CALL_ERROR + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static class ValidatorData {
        public int minPages;
        public String font;
        public String sizeRange;
        public String style;

        public ValidatorData(int minPages, String font, String sizeRange, String style) {
            this.minPages = minPages;
            this.font = font;
            this.sizeRange = sizeRange;
            this.style = style;
        }
    }
}