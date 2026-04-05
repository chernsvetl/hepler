package normative_control.validation.service.impl.bachelors.piikn.third_year_student.sixth_semester;

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
import normative_control.validation.service.ReviewDocumentValidator;
import normative_control.validation.validators.ValidatorDataReview;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import ru.nsu.fit.chernyavtseva.assistant.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static normative_control.notifications.Errors.DOCUMENT_ERROR;
import static normative_control.notifications.Errors.DOCUMENT_SUCCESS;
import static normative_control.notifications.Errors.FONT_SIZE_ERROR;
import static normative_control.notifications.Errors.JSON_READ_ERROR;
import static normative_control.notifications.Errors.JSON_THEME_ERROR;
import static normative_control.notifications.Errors.JSON_VALIDATION_END;
import static normative_control.notifications.Errors.JSON_WRITE_ERROR;
import static normative_control.notifications.Errors.NEXT_LINE;
import static normative_control.notifications.Errors.PATH_ERROR;
import static normative_control.notifications.Errors.READING_FILE_ERROR;
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
import static normative_control.utils.Files.PIIKN_6_REVIEW_FILE;
import static normative_control.utils.ValidationPaths.MODEL_FILENAME;
import static normative_control.validation.validators.CommonValidator.extractThemeText;
import static normative_control.validation.validators.CommonValidator.getFontSize;
import static normative_control.validation.validators.CommonValidator.getFontStyle;
import static normative_control.validation.validators.CommonValidator.isValidTheme;
import static normative_control.validation.validators.CommonValidator.parseSizeRange;

public class ReviewDocumentValidatorImpl implements ReviewDocumentValidator {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void validateDocxFiles(String directoryPath, ValidatorDataReview data) {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            System.err.println(PATH_ERROR);
            return;
        }

        JsonObject validationResult = new JsonObject();
        validationResult.addProperty("validationStartTime", LocalDateTime.now().format(formatter));

        List<JsonObject> documents = new ArrayList<>();

        writeValidationLogs(LocalDateTime.now().format(formatter) + "\n", PIIKN_6_REVIEW_FILE);
        for (File file : dir.listFiles((d, name) -> name.toLowerCase().endsWith(".docx"))) {
            JsonObject documentInfo = new JsonObject();
            documentInfo.addProperty("fileName", file.getName());
            List<String> errors = new ArrayList<>();

            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {
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

                // проверка даты отзыва
                String practiceEndDateStr = data.dateFinishPractice;
                if (practiceEndDateStr != null && !practiceEndDateStr.isEmpty()) {
                    StringBuilder fullTextBuilder = new StringBuilder();
                    for (XWPFParagraph paragraph : document.getParagraphs()) {
                        fullTextBuilder.append(paragraph.getText()).append("\n");
                    }
                    String fullText = fullTextBuilder.toString();

                    Pattern markerPattern = Pattern.compile(
                            "\\(подпись\\*, расшифровка Ф\\.И\\.О\\.\\)\\s*(.*?)\\s*\\* Подпись руководителя практики",
                            Pattern.DOTALL);
                    Matcher markerMatcher = markerPattern.matcher(fullText);
                    if (markerMatcher.find()) {
                        String between = markerMatcher.group(1).trim();
                        if (between.isEmpty()) {
                            valid = false;
                            String errorMsg = "Дата отзыва не заполнена (пусто между маркерами).";
                            errors.add(errorMsg);
                            errorMessage.append(errorMsg).append("\n");
                            loggerInfo.append(errorMsg).append("\n");
                        } else if (between.matches("[«\"_\\s\\-–—]+") || between.contains("___") || between.contains("_______")) {
                            valid = false;
                            String errorMsg = "Дата отзыва не заполнена (обнаружен шаблон с подчёркиваниями).";
                            errors.add(errorMsg);
                            errorMessage.append(errorMsg).append("\n");
                            loggerInfo.append(errorMsg).append("\n");
                        } else {
                            // цифровой формат или текстовый с возможными кавычками вокруг дня
                            Pattern datePattern = Pattern.compile(
                                    "\\b(\\d{2})\\.(\\d{2})\\.(\\d{2,4})\\b|\\b[«\"]?(\\d{1,2})[»\"]?\\s+([а-яА-Я]+)\\s+(\\d{4})\\s*г?\\.?");
                            Matcher dateMatcher = datePattern.matcher(between);
                            if (dateMatcher.find()) {
                                LocalDate reviewDate = null;
                                try {
                                    if (dateMatcher.group(1) != null) {
                                        // цифровой формат
                                        String day = dateMatcher.group(1);
                                        String month = dateMatcher.group(2);
                                        String year = dateMatcher.group(3);
                                        String dateStr = String.format("%s.%s.%s", day, month, year);
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
                                        if (year.length() == 4) formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                        reviewDate = LocalDate.parse(dateStr, formatter);
                                    } else {
                                        // текстовый формат
                                        String day = dateMatcher.group(4);
                                        String monthRu = dateMatcher.group(5);
                                        String year = dateMatcher.group(6);
                                        // удаляем возможные кавычки из дня
                                        day = day.replaceAll("[«\"»]", "");
                                        DateTimeFormatter formatter = new java.time.format.DateTimeFormatterBuilder()
                                                .parseCaseInsensitive()
                                                .appendPattern("d MMMM yyyy")
                                                .toFormatter(new java.util.Locale("ru"));
                                        reviewDate = LocalDate.parse(day + " " + monthRu + " " + year, formatter);
                                    }
                                } catch (Exception e) {
                                    valid = false;
                                    String errorMsg = "Не удалось распознать дату отзыва (ожидается формат дд.мм.гг или дд месяц гггг). Найденный фрагмент: " + dateMatcher.group();
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                    reviewDate = null;
                                }
                                if (reviewDate != null) {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
                                    LocalDate practiceEndDate = LocalDate.parse(practiceEndDateStr, formatter);
                                    if (reviewDate.isAfter(practiceEndDate)) {
                                        valid = false;
                                        String errorMsg = "Дата отзыва не может быть позднее даты окончания практики (" + practiceEndDateStr + "). В отзыве указана дата: " + reviewDate.format(formatter);
                                        errors.add(errorMsg);
                                        errorMessage.append(errorMsg).append("\n");
                                        loggerInfo.append(errorMsg).append("\n");
                                    }
                                }
                            } else {
                                valid = false;
                                String errorMsg = "Дата отзыва не заполнена или имеет неверный формат. Ожидается дата в формате дд.мм.гг или дд месяц гггг.";
                                errors.add(errorMsg);
                                errorMessage.append(errorMsg).append("\n");
                                loggerInfo.append(errorMsg).append("\n");
                            }
                        }
                    }
                }

                documentInfo.addProperty("isValid", valid);
                if (!errors.isEmpty()) {
                    documentInfo.add("errors", gson.toJsonTree(errors));
                }

                if (valid) {
                    System.out.println(ANSI_GREEN + file.getName() + DOCUMENT_SUCCESS);
                    writeValidationLogs(file.getName() + DOCUMENT_SUCCESS, PIIKN_6_REVIEW_FILE);
                    writeValidationLogs(NEXT_LINE, PIIKN_6_REVIEW_FILE);
                } else {
                    System.out.println(ANSI_BLACK + file.getName() + DOCUMENT_ERROR + ANSI_BLACK + ANSI_RED + errorMessage + NEXT_LINE);
                    writeValidationLogs(file.getName() + DOCUMENT_ERROR + loggerInfo + NEXT_LINE, PIIKN_6_REVIEW_FILE);
                }
            } catch (IOException e) {
                documentInfo.addProperty("error", JSON_READ_ERROR + e.getMessage());
                System.err.println(READING_FILE_ERROR + file.getName() + ": " + e.getMessage());
            }
            documents.add(documentInfo);
            writeValidationLogs(SPACE, PIIKN_6_REVIEW_FILE);
        }
        validationResult.add("documents", gson.toJsonTree(documents));
        validationResult.addProperty("validationEndTime", LocalDateTime.now().format(formatter));
        validationResult.addProperty("status", JSON_VALIDATION_END);

        try (FileWriter writer = new FileWriter(PIIKN_6_REVIEW_FILE + ".json")) {
            gson.toJson(validationResult, writer);
        } catch (IOException e) {
            System.err.println(JSON_WRITE_ERROR + e.getMessage());
        }

        System.out.println(ANSI_BLACK + VALIDATION_END + ANSI_BLACK);
        writeValidationLogs(VALIDATION_END, PIIKN_6_REVIEW_FILE);
        writeValidationLogs(NEXT_LINE, PIIKN_6_REVIEW_FILE);
    }

    @Override
    public ValidatorDataReview extractFromSparql(String sparqlQuery) {
        try {
            Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            try (InputStream in = Main.class.getResourceAsStream(MODEL_FILENAME)) {
                model.read(in, "RDF/XML");
            }
            Query query = QueryFactory.create(sparqlQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            ResultSet results = qexec.execSelect();
            String sizeRange = null;
            String style = null;
            String dateFinishPractice = null;
            while(results.hasNext()){
                QuerySolution solution = results.next();
                sizeRange = solution.getLiteral("Размер").getString();
                style = solution.getLiteral("Стиль").getString();
                // дата окончания практики совпадает с этой так что верно
                dateFinishPractice = solution.getLiteral("Дата_окончания_этапа_защиты_работы_из_пиикн_6").getString();
            }
            qexec.close();

            return new ValidatorDataReview(sizeRange, style, dateFinishPractice);
        } catch (Exception e) {
            System.err.println(SPARQL_ERROR + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

