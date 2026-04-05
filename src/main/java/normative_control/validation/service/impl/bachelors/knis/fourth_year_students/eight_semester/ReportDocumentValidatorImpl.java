package normative_control.validation.service.impl.bachelors.knis.fourth_year_students.eight_semester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.validators.ValidatorDataReport;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import ru.nsu.fit.chernyavtseva.assistant.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static normative_control.notifications.Errors.CONSCLUSION_NAME;
import static normative_control.notifications.Errors.CONSCLUSION_NAME_NOT_EXIST;
import static normative_control.notifications.Errors.DOCUMENT_ERROR;
import static normative_control.notifications.Errors.DOCUMENT_SUCCESS;
import static normative_control.notifications.Errors.FONT_SIZE_ERROR;
import static normative_control.notifications.Errors.INTRODUCTION_NAME;
import static normative_control.notifications.Errors.INTRODUCTION_NAME_NOT_EXIST;
import static normative_control.notifications.Errors.JSON_CONSCLUSION_NAME_NOT_EXIST_ERROR;
import static normative_control.notifications.Errors.JSON_INTRODUCTION_NAME_NOT_EXIST_ERROR;
import static normative_control.notifications.Errors.JSON_LIBRARY_NAME_NOT_EXIST_ERROR;
import static normative_control.notifications.Errors.JSON_READ_ERROR;
import static normative_control.notifications.Errors.JSON_REPORT_TEXT_NOT_CHANGED_ERROR;
import static normative_control.notifications.Errors.JSON_THEME_ERROR;
import static normative_control.notifications.Errors.JSON_VALIDATION_END;
import static normative_control.notifications.Errors.JSON_WRITE_ERROR;
import static normative_control.notifications.Errors.LIBRARY_NAME;
import static normative_control.notifications.Errors.LIBRARY_NAME_NOT_EXIST;
import static normative_control.notifications.Errors.NEXT_LINE;
import static normative_control.notifications.Errors.PAGES_ERROR;
import static normative_control.notifications.Errors.PATH_ERROR;
import static normative_control.notifications.Errors.READING_FILE_ERROR;
import static normative_control.notifications.Errors.REPORT_TEXT_NOT_CHANGED;
import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.notifications.Errors.STYLE_ERROR;
import static normative_control.notifications.Errors.THEME_ERROR;
import static normative_control.notifications.Errors.VALIDATION_END;
import static normative_control.output.FileLogger.writeValidationLogs;
import static normative_control.utils.Constants.ANSI_BLACK;
import static normative_control.utils.Constants.ANSI_GREEN;
import static normative_control.utils.Constants.ANSI_RED;
import static normative_control.utils.Constants.SPACE;
import static normative_control.utils.Constants.formatter;
import static normative_control.utils.Files.KNIS_8_REPORT_FILE;
import static normative_control.utils.ValidationPaths.MODEL_FILENAME;
import static normative_control.validation.service.utils.Constants.*;
import static normative_control.validation.validators.CommonValidator.containsSection;
import static normative_control.validation.validators.CommonValidator.extractThemeText;
import static normative_control.validation.validators.CommonValidator.getFontSize;
import static normative_control.validation.validators.CommonValidator.getFontStyle;
import static normative_control.validation.validators.CommonValidator.isValidTheme;
import static normative_control.validation.validators.CommonValidator.parseSizeRange;

public class ReportDocumentValidatorImpl implements ReportDocumentValidator {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Определяет индекс первого параграфа основного текста (после титульного листа)
     */
    private int findFirstContentParagraphIndex(List<XWPFParagraph> paragraphs) {
        int titleStartIndex = -1;
        int titleEndIndex = -1;

        // Ищем начало титульника
        for (int i = 0; i < paragraphs.size(); i++) {
            String text = paragraphs.get(i).getText().trim();
            if (text.contains(TITLE_START)) {
                titleStartIndex = i;
                break;
            }
        }

        // Если нашли начало, ищем конец титульника (строку с городом и годом)
        if (titleStartIndex != -1) {
            for (int i = titleStartIndex; i < paragraphs.size(); i++) {
                String text = paragraphs.get(i).getText().trim();
                if (CITY_YEAR_PATTERN.matcher(text).matches()) {
                    titleEndIndex = i;
                    break;
                }
            }
        }

        // Если нашли и начало, и конец, возвращаем индекс следующего параграфа
        if (titleStartIndex != -1 && titleEndIndex != -1) {
            return titleEndIndex + 1;
        }

        // Если не нашли границы титульника, ищем по ключевым словам "Введение", "Оглавление", "Содержание"
        for (int i = 0; i < paragraphs.size(); i++) {
            String text = paragraphs.get(i).getText().trim();
            if (text.contains("Введение") || text.contains("Оглавление") || text.contains("Содержание")) {
                return i;
            }
        }

        // Запасной вариант - начинаем со второго параграфа
        return paragraphs.size() > 1 ? 1 : 0;
    }

    @Override
    public void validateDocxFiles(String directoryPath, ValidatorDataReport data) {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            System.err.println(PATH_ERROR);
            return;
        }

        JsonObject validationResult = new JsonObject();
        validationResult.addProperty("validationStartTime", LocalDateTime.now().format(formatter));

        List<JsonObject> documents = new ArrayList<>();

        writeValidationLogs(LocalDateTime.now().format(formatter) + "\n", KNIS_8_REPORT_FILE);
        for (File file : dir.listFiles((d, name) -> name.toLowerCase().endsWith(".docx"))) {
            JsonObject documentInfo = new JsonObject();
            documentInfo.addProperty("fileName", file.getName());
            List<String> errors = new ArrayList<>();

            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {

                int totalChars = document.getParagraphs().stream()
                        .mapToInt(p -> p.getText().length())
                        .sum();
                int pageCount = (int) Math.ceil(totalChars / 1600.0);

                var fontSize = getFontSize(document);
                String fontStyle = getFontStyle(document);
                String themeText = extractThemeText(document);

                boolean valid = true;
                boolean sizeIsValid = false;
                double[] range = parseSizeRange(data.sizeRange);
                if (range != null && range.length == 2) {
                    sizeIsValid = (fontSize >= range[0] && fontSize <= range[1]);
                }
                StringBuilder errorMessage = new StringBuilder();
                var loggerInfo = new StringBuilder();

                if (pageCount < data.minPages) {
                    valid = false;
                    errorMessage.append(PAGES_ERROR).append(data.minPages).append(", есть: ").append(pageCount).append("). \n");
                    loggerInfo.append(PAGES_ERROR).append(data.minPages).append(", есть: ").append(pageCount).append("). \n");
                    errors.add(String.format("Недостаточно страниц (ожидалось: %d, есть: %d)", data.minPages, pageCount));
                }
                if (!sizeIsValid) {
                    valid = false;
                    errorMessage.append(FONT_SIZE_ERROR).append(data.sizeRange).append("). \n");
                    loggerInfo.append(FONT_SIZE_ERROR).append(data.sizeRange).append("). \n");
                    errors.add(String.format("Неверный размер шрифта (ожидалось: %s)", data.sizeRange));
                }
                if (!fontStyle.equals(data.style)) {
                    valid = false;
                    errorMessage.append(STYLE_ERROR).append(data.style).append("). \n");
                    loggerInfo.append(STYLE_ERROR).append(data.style).append("). \n");
                    errors.add(String.format("Неверный стиль шрифта (ожидалось: %s)", data.style));
                }
                if (!isValidTheme(themeText)) {
                    valid = false;
                    errorMessage.append(THEME_ERROR);
                    loggerInfo.append(THEME_ERROR);
                    errors.add(JSON_THEME_ERROR);
                }
                if (containsSection(document, data.full_text)) {
                    valid = false;
                    errorMessage.append(REPORT_TEXT_NOT_CHANGED);
                    loggerInfo.append(REPORT_TEXT_NOT_CHANGED);
                    errors.add(JSON_REPORT_TEXT_NOT_CHANGED_ERROR);
                }
                if (containsSection(document, data.full_text2)) {
                    valid = false;
                    errorMessage.append(REPORT_TEXT_NOT_CHANGED);
                    loggerInfo.append(REPORT_TEXT_NOT_CHANGED);
                    errors.add(JSON_REPORT_TEXT_NOT_CHANGED_ERROR);
                }
                if (!containsSection(document, INTRODUCTION_NAME)) {
                    valid = false;
                    errorMessage.append(INTRODUCTION_NAME_NOT_EXIST);
                    loggerInfo.append(INTRODUCTION_NAME_NOT_EXIST);
                    errors.add(JSON_INTRODUCTION_NAME_NOT_EXIST_ERROR);
                }
                if (!containsSection(document, CONSCLUSION_NAME)) {
                    valid = false;
                    errorMessage.append(CONSCLUSION_NAME_NOT_EXIST);
                    loggerInfo.append(CONSCLUSION_NAME_NOT_EXIST);
                    errors.add(JSON_CONSCLUSION_NAME_NOT_EXIST_ERROR);
                }
                if (!containsSection(document, LIBRARY_NAME)) {
                    valid = false;
                    errorMessage.append(LIBRARY_NAME_NOT_EXIST);
                    loggerInfo.append(LIBRARY_NAME_NOT_EXIST);
                    errors.add(JSON_LIBRARY_NAME_NOT_EXIST_ERROR);
                }

                List<XWPFParagraph> paragraphs = document.getParagraphs();

                // проверка на то, есть ли оценка на титульнике
                boolean hasGradeOnTitlePage = false;
                String gradeErrorMsg = null;

                int maxCheck = Math.min(50, paragraphs.size());
                int startIdx = -1;
                int endIdx = -1;

                for (int i = 0; i < maxCheck; i++) {
                    if (paragraphs.get(i).getText().contains("Оценка по итогам защиты отчета")) {
                        startIdx = i;
                        break;
                    }
                }
                if (startIdx != -1) {
                    for (int i = startIdx; i < maxCheck; i++) {
                        if (paragraphs.get(i).getText().contains("Отчет заслушан на заседании кафедры")) {
                            endIdx = i;
                            break;
                        }
                    }
                }

                if (startIdx != -1 && endIdx != -1) {
                    StringBuilder betweenText = new StringBuilder();
                    for (int i = startIdx; i <= endIdx; i++) {
                        betweenText.append(paragraphs.get(i).getText()).append("\n");
                    }
                    String content = betweenText.toString();
                    content = content.replaceFirst("(?s).*Оценка по итогам защиты отчета\\s*:", "")
                            .replaceFirst("Отчет заслушан на заседании кафедры.*", "");
                    String hintPattern = "\\(\\s*неудовлетворительно\\s*[,]?\\s*удовлетворительно\\s*[,]?\\s*хорошо\\s*[,]?\\s*отлично\\s*\\)";
                    content = content.replaceAll("(?i)" + hintPattern, "");
                    String cleaned = content.replaceAll("[_\\-\\.,\\s\\n\\r\\t]+", "");

                    if (cleaned.matches(".*[а-яА-Яa-zA-Z0-9].*")) {
                        hasGradeOnTitlePage = true;
                        gradeErrorMsg = "Оценка по итогам защиты отчета на титульнике не должна быть выставлена. Найден лишний текст: " + cleaned;
                    }
                }

                if (hasGradeOnTitlePage) {
                    valid = false;
                    errorMessage.append(gradeErrorMsg).append("\n");
                    loggerInfo.append(gradeErrorMsg).append("\n");
                    errors.add(gradeErrorMsg);
                }

                // валидация на форматирование абзацев
                int contentStartIndex = findFirstContentParagraphIndex(paragraphs);

                boolean lineSpacingValid = true;
                boolean paragraphSpacingValid = true;
                boolean indentationValid = true;
                boolean alignmentValid = true;

                for (int i = contentStartIndex; i < paragraphs.size(); i++) {
                    XWPFParagraph paragraph = paragraphs.get(i);
                    CTP ctp = paragraph.getCTP();
                    CTPPr pPr = ctp.getPPr();

                    if (pPr != null) {
                        CTSpacing spacing = pPr.getSpacing();
                        if (spacing != null) {
                            Object lineObj = spacing.getLine();
                            if (lineObj != null) {
                                double lineSpacingValue = 1.0;
                                if (lineObj instanceof BigInteger) {
                                    lineSpacingValue = ((BigInteger) lineObj).doubleValue() / LINE_TO_TWIPS;
                                } else if (lineObj instanceof String) {
                                    try {
                                        lineSpacingValue = Double.parseDouble((String) lineObj) / LINE_TO_TWIPS;
                                    } catch (NumberFormatException e) {
                                        lineSpacingValue = 1.0;
                                    }
                                } else if (lineObj instanceof Number) {
                                    lineSpacingValue = ((Number) lineObj).doubleValue() / LINE_TO_TWIPS;
                                }
                                if (Math.abs(lineSpacingValue - EXPECTED_LINE_SPACING) > 0.1) {
                                    lineSpacingValid = false;
                                }
                            } else {
                                lineSpacingValid = false;
                            }

                            Object beforeObj = spacing.getBefore();
                            Object afterObj = spacing.getAfter();
                            int beforeValue = 0, afterValue = 0;
                            if (beforeObj instanceof BigInteger) {
                                beforeValue = ((BigInteger) beforeObj).intValue();
                            } else if (beforeObj instanceof String) {
                                try {
                                    beforeValue = Integer.parseInt((String) beforeObj);
                                } catch (NumberFormatException e) {}
                            } else if (beforeObj instanceof Number) {
                                beforeValue = ((Number) beforeObj).intValue();
                            }
                            if (afterObj instanceof BigInteger) {
                                afterValue = ((BigInteger) afterObj).intValue();
                            } else if (afterObj instanceof String) {
                                try {
                                    afterValue = Integer.parseInt((String) afterObj);
                                } catch (NumberFormatException e) {}
                            } else if (afterObj instanceof Number) {
                                afterValue = ((Number) afterObj).intValue();
                            }
                            if (beforeValue > 0 || afterValue > 0) {
                                paragraphSpacingValid = false;
                            }
                        } else {
                            lineSpacingValid = false;
                        }

                        CTInd indentation = pPr.getInd();
                        if (indentation != null) {
                            Object firstLineObj = indentation.getFirstLine();
                            if (firstLineObj != null) {
                                double indentCm = 0;
                                if (firstLineObj instanceof BigInteger) {
                                    indentCm = ((BigInteger) firstLineObj).doubleValue() / CM_TO_TWIPS;
                                } else if (firstLineObj instanceof String) {
                                    try {
                                        indentCm = Double.parseDouble((String) firstLineObj) / CM_TO_TWIPS;
                                    } catch (NumberFormatException e) {}
                                } else if (firstLineObj instanceof Number) {
                                    indentCm = ((Number) firstLineObj).doubleValue() / CM_TO_TWIPS;
                                }
                                if (Math.abs(indentCm - EXPECTED_INDENTATION_CM) > 0.1) {
                                    indentationValid = false;
                                }
                            } else {
                                indentationValid = false;
                            }
                        } else {
                            indentationValid = false;
                        }
                    } else {
                        lineSpacingValid = false;
                        indentationValid = false;
                    }

                    if (paragraph.getAlignment() != ParagraphAlignment.BOTH) {
                        alignmentValid = false;
                    }
                }

                if (!lineSpacingValid) {
                    valid = false;
                    String errorMsg = "Межстрочный интервал должен быть одинарным";
                    errorMessage.append(errorMsg).append("\n");
                    loggerInfo.append(errorMsg).append("\n");
                    errors.add(errorMsg);
                }
                if (!paragraphSpacingValid) {
                    valid = false;
                    String errorMsg = "Между абзацами не должно быть дополнительных интервалов";
                    errorMessage.append(errorMsg).append("\n");
                    loggerInfo.append(errorMsg).append("\n");
                    errors.add(errorMsg);
                }
                if (!indentationValid) {
                    valid = false;
                    String errorMsg = "Абзацный отступ должен быть 1,25 см";
                    errorMessage.append(errorMsg).append("\n");
                    loggerInfo.append(errorMsg).append("\n");
                    errors.add(errorMsg);
                }
                if (!alignmentValid) {
                    valid = false;
                    String errorMsg = "Выравнивание текста должно быть по ширине";
                    errorMessage.append(errorMsg).append("\n");
                    loggerInfo.append(errorMsg).append("\n");
                    errors.add(errorMsg);
                }

                documentInfo.addProperty("isValid", valid);
                if (!errors.isEmpty()) {
                    documentInfo.add("errors", gson.toJsonTree(errors));
                }

                if (valid) {
                    System.out.println(ANSI_GREEN + file.getName() + DOCUMENT_SUCCESS);
                    writeValidationLogs(file.getName() + DOCUMENT_SUCCESS, KNIS_8_REPORT_FILE);
                    writeValidationLogs(NEXT_LINE, KNIS_8_REPORT_FILE);
                } else {
                    System.out.println(ANSI_BLACK + file.getName() + DOCUMENT_ERROR + ANSI_BLACK + ANSI_RED + errorMessage + NEXT_LINE);
                    writeValidationLogs(file.getName() + DOCUMENT_ERROR + loggerInfo + NEXT_LINE, KNIS_8_REPORT_FILE);
                }
            } catch (IOException e) {
                documentInfo.addProperty("error", JSON_READ_ERROR + e.getMessage());
                System.err.println(READING_FILE_ERROR + file.getName() + ": " + e.getMessage());
            }
            documents.add(documentInfo);
            writeValidationLogs(SPACE, KNIS_8_REPORT_FILE);
        }
        validationResult.add("documents", gson.toJsonTree(documents));
        validationResult.addProperty("validationEndTime", LocalDateTime.now().format(formatter));
        validationResult.addProperty("status", JSON_VALIDATION_END);

        try (FileWriter writer = new FileWriter(KNIS_8_REPORT_FILE + ".json")) {
            gson.toJson(validationResult, writer);
        } catch (IOException e) {
            System.err.println(JSON_WRITE_ERROR + e.getMessage());
        }

        System.out.println(ANSI_BLACK + VALIDATION_END + ANSI_BLACK);
        writeValidationLogs(VALIDATION_END, KNIS_8_REPORT_FILE);
        writeValidationLogs(NEXT_LINE, KNIS_8_REPORT_FILE);
    }

    @Override
    public ValidatorDataReport extractFromSparql(String sparqlQuery) {
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
            String full_text = null;
            String full_text2 = null;
            while(results.hasNext()){
                QuerySolution solution = results.next();
                minPages = solution.getLiteral("Минимальное_количество_страниц_отчета").getInt();
                sizeRange = solution.getLiteral("Размер").getString();
                style = solution.getLiteral("Стиль").getString();
                full_text = solution.getLiteral("Основной_текст_отчета").getString();
                full_text2 = solution.getLiteral("Основной_текст_отчета2").getString();
            }
            qexec.close();

            return new ValidatorDataReport(minPages, font, sizeRange, style, full_text, full_text2);
        } catch (Exception e) {
            System.err.println(SPARQL_ERROR + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
