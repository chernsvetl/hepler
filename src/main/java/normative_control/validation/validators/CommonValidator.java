package normative_control.validation.validators;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static normative_control.notifications.Errors.FORMAT_CONVERTION_ERROR;
import static normative_control.notifications.Errors.NUMBER_CONVERTION_ERROR;
import static normative_control.notifications.Errors.PYTHON_CALL_ERROR;
import static normative_control.notifications.Errors.PYTHON_EXECUTION_ERROR;
import static normative_control.utils.Paths.PYTHON_SCRIPT;
import static normative_control.utils.TextSimilarity.areTextsSimilar;

public class CommonValidator {
    public static String getFontStyle(XWPFDocument document) {
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
    public static double getFontSize(XWPFDocument document) {
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
                .orElse(0.0);

        return mostPopularSize;
    }
    public static double[] parseSizeRange(String sizeRangeStr) {
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
    public static String extractThemeText(XWPFDocument document) {
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
    public static boolean isValidTheme(String themeText) {
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

    public static boolean containsSection(XWPFDocument document, String sectionTitle) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            if (paragraph.getText().contains(sectionTitle)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsSectionWithSimilarity(XWPFDocument document, String sectionTitle) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String paragraphText = paragraph.getText();
            if (areTextsSimilar(paragraphText, sectionTitle)) {
                return true;
            }
        }
        return false;
    }
}
