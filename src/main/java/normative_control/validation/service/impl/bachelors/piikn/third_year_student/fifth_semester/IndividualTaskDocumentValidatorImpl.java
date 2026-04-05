package normative_control.validation.service.impl.bachelors.piikn.third_year_student.fifth_semester;

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
import normative_control.validation.service.IndividualTaskDocumentValidator;
import normative_control.validation.validators.ValidatorDataDatesIndividualTask;
import normative_control.validation.validators.ValidatorDataIndividualTask;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import ru.nsu.fit.chernyavtseva.assistant.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static normative_control.notifications.Errors.CONTENT_STEP_DEFEND_TASK_NOT_CHANGED;
import static normative_control.notifications.Errors.CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED;
import static normative_control.notifications.Errors.CONTENT_STEP_ORG_TASK_NOT_CHANGED;
import static normative_control.notifications.Errors.DATE_END_INDIVIDUAL_TASK_ERROR;
import static normative_control.notifications.Errors.DATE_STEP_DEFEND_TASK_NOT_CHANGED;
import static normative_control.notifications.Errors.DATE_STEP_ORG_INDIVIDUAL_TASK_NOT_CHANGED;
import static normative_control.notifications.Errors.DOCUMENT_ERROR;
import static normative_control.notifications.Errors.DOCUMENT_SUCCESS;
import static normative_control.notifications.Errors.JSON_DATE_END_INDIVIDUAL_TASK_IS_EMPTY_ERROR;
import static normative_control.notifications.Errors.JSON_READ_ERROR;
import static normative_control.notifications.Errors.JSON_THEME_ERROR;
import static normative_control.notifications.Errors.JSON_VALIDATION_END;
import static normative_control.notifications.Errors.JSON_WRITE_ERROR;
import static normative_control.notifications.Errors.NEXT_LINE;
import static normative_control.notifications.Errors.PATH_ERROR;
import static normative_control.notifications.Errors.READING_FILE_ERROR;
import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.notifications.Errors.STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED;
import static normative_control.notifications.Errors.STEP_INDIVIDUAL_TASK_NAME_IS_EMPTY;
import static normative_control.notifications.Errors.STEP_INDIVIDUAL_TASK_NUMBER_IS_EMPTY;
import static normative_control.notifications.Errors.STYLE_ERROR;
import static normative_control.notifications.Errors.THEME_ERROR;
import static normative_control.notifications.Errors.VALIDATION_END;
import static normative_control.output.FileLogger.writeValidationLogs;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_PIIKN_5;
import static normative_control.utils.Constants.ANSI_BLACK;
import static normative_control.utils.Constants.ANSI_GREEN;
import static normative_control.utils.Constants.ANSI_RED;
import static normative_control.utils.Constants.SPACE;
import static normative_control.utils.Constants.formatter;
import static normative_control.utils.Files.PIIKN_5_INDIVIDUAL_TASK_FILE;
import static normative_control.utils.TextSimilarity.areTextsSimilar;
import static normative_control.utils.ValidationPaths.MODEL_FILENAME;
import static normative_control.validation.validators.CommonValidator.extractThemeText;
import static normative_control.validation.validators.CommonValidator.getFontStyle;
import static normative_control.validation.validators.CommonValidator.isValidTheme;

public class IndividualTaskDocumentValidatorImpl implements IndividualTaskDocumentValidator {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void validateDocxFiles(String directoryPath, ValidatorDataIndividualTask data) {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            System.err.println(PATH_ERROR);
            return;
        }

        List<JsonObject> validationResults = new ArrayList<>();
        JsonObject validationSession = new JsonObject();
        validationSession.addProperty("validationStartTime", LocalDateTime.now().format(formatter));

        writeValidationLogs(LocalDateTime.now().format(formatter) + "\n", PIIKN_5_INDIVIDUAL_TASK_FILE);
        for (File file : dir.listFiles((d, name) -> name.toLowerCase().endsWith(".docx"))) {
            JsonObject documentResult = new JsonObject();
            documentResult.addProperty("fileName", file.getName());
            List<String> errors = new ArrayList<>();

            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {

                String fontStyle = getFontStyle(document);
                boolean valid = true;
                var errorMessage = new StringBuilder();
                var loggerInfo = new StringBuilder();
                String themeText = extractThemeText(document);

                if (!fontStyle.equals(data.style)) {
                    valid = false;
                    errorMessage.append(STYLE_ERROR).append(data.style).append("). \n");
                    loggerInfo.append(STYLE_ERROR).append(data.style).append("). \n");
                    errors.add(String.format("Ошибка стиля: ожидается '%s'", data.style));
                }

                if (!isValidTheme(themeText)) {
                    valid = false;
                    errorMessage.append(THEME_ERROR);
                    loggerInfo.append(THEME_ERROR);
                    errors.add(JSON_THEME_ERROR);
                }

                for (XWPFTable table : document.getTables()) {
                    for (int rowIndex = 1; rowIndex < table.getRows().size(); rowIndex++) {
                        var row = table.getRow(rowIndex);
                        int dateColumnIndex = 2;

                        if (row.getTableCells().size() > dateColumnIndex) {
                            var cell = row.getCell(dateColumnIndex);
                            var cellValue = cell.getText() != null ? cell.getText().trim() : "";

                            if (cellValue.isEmpty()) {
                                valid = false;
                                if (rowIndex == 1) {
                                    valid = false;
                                    String errorMsg = DATE_STEP_ORG_INDIVIDUAL_TASK_NOT_CHANGED + " пуст.";
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                } else if (rowIndex == (table.getRows().size() - 1)) {
                                    valid = false;
                                    String errorMsg = DATE_STEP_DEFEND_TASK_NOT_CHANGED + " пуст.";
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                } else {
                                    valid = false;
                                    String errorMsg = JSON_DATE_END_INDIVIDUAL_TASK_IS_EMPTY_ERROR + " в строке " + rowIndex
                                            + " и столбце " + dateColumnIndex + " пуст.";
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                }
                                continue;
                            }

                            var result  = extractFromSparqlDates(QUERY_REPORT_PIIKN_5);

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
                            try {
                                LocalDate startDate1 = LocalDate.parse(result.dateOrgStart1, formatter);
                                var startDate2 = startDate1.plusDays(2);

                                LocalDate dateDefendStart2 = LocalDate.parse(result.dateDefendStart2, formatter);
                                var dateDefendStart1 = dateDefendStart2.minusDays(10);

                                var dateStart1IndividualPlan = startDate1.plusDays(1);
                                var dateStart2IndividualPlan = dateDefendStart2.minusDays(1);

                                LocalDate cellDate = LocalDate.parse(cellValue, formatter);

                                if (rowIndex == 1 && (cellDate.isBefore(startDate1) || cellDate.isAfter(startDate2))) {
                                    valid = false;
                                    String errorMsg = DATE_STEP_ORG_INDIVIDUAL_TASK_NOT_CHANGED +
                                            " некорректен: дата должна быть в диапазоне от " + result.dateOrgStart1 + " до " + startDate2.format(formatter);
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                }

                                if (rowIndex == (table.getRows().size() - 1) && (cellDate.isBefore(dateDefendStart1) || cellDate.isAfter(dateDefendStart2))) {
                                    valid = false;
                                    String errorMsg = DATE_STEP_DEFEND_TASK_NOT_CHANGED +
                                            " некорректен: дата должна быть в диапазоне от " + dateDefendStart1.format(formatter) + " до " + result.dateDefendStart2;
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                }

                                if ((rowIndex != (table.getRows().size() - 1) && rowIndex != 1)
                                        && (cellDate.isBefore(dateStart1IndividualPlan) || cellDate.isAfter(dateStart2IndividualPlan))) {
                                    valid = false;
                                    String errorMsg = JSON_DATE_END_INDIVIDUAL_TASK_IS_EMPTY_ERROR + "в строке " + rowIndex + " и столбце " +
                                            dateColumnIndex + " некорректен: дата начала должна быть не раньше " + dateStart1IndividualPlan.format(formatter)
                                            + ", а последним максимальным днем данного этапа является дата " + dateStart2IndividualPlan.format(formatter);
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                }

                            } catch (DateTimeParseException e) {
                                if (rowIndex == 1) {
                                    valid = false;
                                    String errorMsg = DATE_STEP_ORG_INDIVIDUAL_TASK_NOT_CHANGED +
                                            " некорректен: не соответствует формату дд.мм.гг";
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                } else if (rowIndex == table.getRows().size() - 1){
                                    valid = false;
                                    String errorMsg = DATE_STEP_DEFEND_TASK_NOT_CHANGED +
                                            " некорректен: не соответствует формату дд.мм.гг";
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                }
                                else {
                                    valid = false;
                                    String errorMsg = DATE_END_INDIVIDUAL_TASK_ERROR + " в строке " + rowIndex + " и" + " столбце " + dateColumnIndex +
                                            " не соответствует формату дд.мм.гг.";
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                }
                            }
                        }
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    for (int rowIndex = 1; rowIndex < table.getRows().size(); rowIndex++) {
                        var row = table.getRow(rowIndex);
                        int dateColumnIndex = 3;

                        if (row.getTableCells().size() > dateColumnIndex) {
                            var cell = row.getCell(dateColumnIndex);
                            var cellValue = cell.getText() != null ? cell.getText().trim() : "";

                            if (cellValue.isEmpty()) {
                                valid = false;
                                if (rowIndex == 1) {
                                    valid = false;
                                    String errorMsg = CONTENT_STEP_ORG_TASK_NOT_CHANGED + " пусто.";
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                } else if (rowIndex == (table.getRows().size() - 1)) {
                                    valid = false;
                                    String errorMsg = CONTENT_STEP_DEFEND_TASK_NOT_CHANGED + " пусто.";
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                } else {
                                    valid = false;
                                    String errorMsg = CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED + " в строке " + rowIndex
                                            + " и столбце " + dateColumnIndex + " пусто.";
                                    errors.add(errorMsg);
                                    errorMessage.append(errorMsg).append("\n");
                                    loggerInfo.append(errorMsg).append("\n");
                                }
                            } else {
                                if (areTextsSimilar(cellValue, data.individualStepContent)) {
                                    valid = false;
                                    errors.add(CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED + " в строке " + rowIndex
                                            + " и столбце " + dateColumnIndex + " не изменено в таблице.");
                                    errorMessage.append(CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED).append(" в строке " + rowIndex
                                            + " и столбце " + dateColumnIndex + " не изменено в таблице. \n");
                                    loggerInfo.append(CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED).append(" в строке " + rowIndex
                                            + " и столбце " + dateColumnIndex + " не изменено в таблице. \n");
                                }
                            }
                        }
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    for (int rowIndex = 1; rowIndex < table.getRows().size(); rowIndex++) {
                        var row = table.getRow(rowIndex);
                        int dateColumnIndex = 4;

                        if (row.getTableCells().size() > dateColumnIndex) {
                            var cell = row.getCell(dateColumnIndex);
                            var cellValue = cell.getText() != null ? cell.getText().trim() : "";

                            if (cellValue.isEmpty()) {
                                valid = false;
                                String errorMsg = STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED + " в строке " + rowIndex +
                                        " и столбце " + dateColumnIndex + " пустая.";
                                errors.add(errorMsg);
                                errorMessage.append(errorMsg).append("\n");
                                loggerInfo.append(errorMsg).append("\n");
                            } else {
                                if (cellValue.contains(data.individualStepForm)) {
                                    valid = false;
                                    errors.add(STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED + " в строке " + rowIndex
                                            + " и столбце " + dateColumnIndex + " не изменена в таблице.");
                                    errorMessage.append(STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED).append(" в строке " + rowIndex
                                            + " и столбце " + dateColumnIndex + " не изменена в таблице. \n");
                                    loggerInfo.append(STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED).append(" в строке " + rowIndex
                                            + " и столбце " + dateColumnIndex + " не изменена в таблице. \n");
                                }
                            }
                        }
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    for (int rowIndex = 1; rowIndex < table.getRows().size(); rowIndex++) {
                        var row = table.getRow(rowIndex);
                        int dateColumnIndex = 1;

                        if (row.getTableCells().size() > dateColumnIndex) {
                            var cell = row.getCell(dateColumnIndex);
                            var cellValue = cell.getText() != null ? cell.getText().trim() : "";

                            if (cellValue.isEmpty()) {
                                valid = false;
                                String errorMsg = STEP_INDIVIDUAL_TASK_NAME_IS_EMPTY + " в строке " + rowIndex +
                                        " и столбце " + dateColumnIndex + " пустое.";
                                errors.add(errorMsg);
                                errorMessage.append(errorMsg).append("\n");
                                loggerInfo.append(errorMsg).append("\n");
                            }
                        }
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    for (int rowIndex = 2; rowIndex < table.getRows().size(); rowIndex++) {
                        var row = table.getRow(rowIndex);
                        int dateColumnIndex = 0;

                        if (row.getTableCells().size() > dateColumnIndex) {
                            var cell = row.getCell(dateColumnIndex);
                            var cellValue = cell.getText() != null ? cell.getText().trim() : "";

                            if (cellValue.isEmpty()) {
                                valid = false;
                                String errorMsg = STEP_INDIVIDUAL_TASK_NUMBER_IS_EMPTY + " в строке " + rowIndex + " пуст.";
                                errors.add(errorMsg);
                                errorMessage.append(errorMsg).append("\n");
                                loggerInfo.append(errorMsg).append("\n");
                            }
                        }
                    }
                }

                // проверка дат в акте и инструктаже
                StringBuilder fullText = new StringBuilder();
                for (XWPFParagraph paragraph : document.getParagraphs()) {
                    fullText.append(paragraph.getText()).append("\n");
                }
                String docText = fullText.toString();

                // проверка даты инструктажа: ищем фразу "с оформлением установленной документации"
                // и затем проверяем, что до "Руководитель практики назначе" нет шаблона с подчеркиваниями
                int instructionPos = docText.indexOf("с оформлением установленной документации");
                if (instructionPos != -1) {
                    int endSearch = Math.min(docText.length(), instructionPos + 100);
                    String afterInstruction = docText.substring(instructionPos, endSearch);
                    // паттерн -  кавычка (или её отсутствие), затем 1+ подчёркиваний, затем пробелы, 1+ подчёркиваний, 20, пробелы, 1+ подчёркиваний, "г" с точкой или без
                    Pattern placeholderPattern = Pattern.compile("[«\"]?_{1,}[»\"]?\\s*_{1,}\\s*20\\s*_{1,}\\s*г\\.?");
                    if (placeholderPattern.matcher(afterInstruction).find()) {
                        valid = false;
                        String errorMsg = "Дата инструктажа не заполнена.";
                        errors.add(errorMsg);
                        errorMessage.append(errorMsg).append("\n");
                        loggerInfo.append(errorMsg).append("\n");
                    }
                }

                // проверка распорядительного акта: ищем фразу "назначен распорядительным актом от"
                // и затем проверяем, что до "(Для обучающихся, направленных на практик" есть шаблон с подчеркиваниями
                Pattern actPattern = Pattern.compile(
                        "назначен распорядительным актом от(.*?)\\(Для обучающихся, направленных на практик",
                        Pattern.DOTALL);
                Matcher actMatcher = actPattern.matcher(docText);
                if (actMatcher.find()) {
                    String between = actMatcher.group(1);
                    // ищем шаблон вида «___» _________ 20__г. №__________.
                    Pattern placeholderPattern = Pattern.compile("[«\"]?___[»\"]?\\s*_________\\s*20__г\\.?\\s*№\\s*__________\\.?");
                    if (placeholderPattern.matcher(between).find()) {
                        valid = false;
                        String errorMsg = "Дата и номер распорядительного акта не заполнены.";
                        errors.add(errorMsg);
                        errorMessage.append(errorMsg).append("\n");
                        loggerInfo.append(errorMsg).append("\n");
                    }
                }

//                for (XWPFTable table : document.getTables()) {
//                    boolean flag = false;
//                    for (int rowIndex = 0; rowIndex < table.getRows().size(); rowIndex++) {
//                        var row = table.getRow(rowIndex);
//                        for (int cellIndex = 0; cellIndex < row.getTableCells().size(); cellIndex++) {
//                            var cellValue = row.getCell(cellIndex).getText().trim();
//                            if (cellValue.equals(data.prepareAndDefendStepContent.trim())) {
//                                flag = true;
//                                break;
//                            }
//                        }
//                        if (flag) {
//                            errors.add(CONTENT_STEP_DEFEND_TASK_NOT_CHANGED + " не изменено в таблице.");
//                            errorMessage.append(CONTENT_STEP_DEFEND_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
//                            loggerInfo.append(CONTENT_STEP_DEFEND_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
//                            break;
//                        }
//                    }
//                }

                documentResult.addProperty("isValid", valid);
                if (!errors.isEmpty()) {
                    documentResult.add("errors", gson.toJsonTree(errors));
                }

                if (valid) {
                    System.out.println(ANSI_GREEN + file.getName() + DOCUMENT_SUCCESS);
                    writeValidationLogs(file.getName() + DOCUMENT_SUCCESS, PIIKN_5_INDIVIDUAL_TASK_FILE);
                } else {
                    System.out.println(ANSI_BLACK + file.getName() + DOCUMENT_ERROR + ANSI_BLACK + ANSI_RED +
                            String.join("\n", errors) + "\n");
                    writeValidationLogs(file.getName() + DOCUMENT_ERROR + loggerInfo + "\n", PIIKN_5_INDIVIDUAL_TASK_FILE);
                }

            } catch (IOException e) {
                documentResult.addProperty("error", JSON_READ_ERROR + e.getMessage());
                System.err.println(READING_FILE_ERROR + file.getName() + ": " + "\n" + e.getMessage());
                writeValidationLogs(READING_FILE_ERROR + file.getName() + ": "  + "\n" + e.getMessage(), PIIKN_5_INDIVIDUAL_TASK_FILE);
            }
            writeValidationLogs(SPACE, PIIKN_5_INDIVIDUAL_TASK_FILE);
            validationResults.add(documentResult);
        }

        validationSession.add("documents", gson.toJsonTree(validationResults));
        validationSession.addProperty("validationEndTime", LocalDateTime.now().format(formatter));
        validationSession.addProperty("status", JSON_VALIDATION_END);

        try (FileWriter writer = new FileWriter(PIIKN_5_INDIVIDUAL_TASK_FILE + ".json")) {
            gson.toJson(validationSession, writer);
        } catch (IOException e) {
            System.err.println(JSON_WRITE_ERROR + e.getMessage());
        }
        writeValidationLogs(VALIDATION_END, PIIKN_5_INDIVIDUAL_TASK_FILE);
        System.out.println(ANSI_BLACK + VALIDATION_END + ANSI_BLACK);
        writeValidationLogs(NEXT_LINE, PIIKN_5_INDIVIDUAL_TASK_FILE);
    }

    @Override
    public ValidatorDataIndividualTask extractFromSparql(String sparqlQuery) {
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
            String orgStepEndDate = null;
            String prepareAndDefendStepEndDate = null;
            String prepareAndDefendStepContent = null;
            String orgStepContent = null;
            String individualStepContent = null;
            String individualStepDate = null;
            String individualStepForm = null;
            while(results.hasNext()){
                QuerySolution solution = results.next();
                minPages = solution.getLiteral("Минимальное_количество_страниц_отчета").getInt();
                sizeRange = solution.getLiteral("Размер").getString();
                style = solution.getLiteral("Стиль").getString();
                orgStepEndDate = solution.getLiteral("Срок_завершения_организационного_этапа_из_пиикн_5").toString();
                prepareAndDefendStepEndDate = solution.getLiteral("Срок_завершения_этапа_подготовки_и_защиты_отчетных_материалов_из_пиикн_5").toString();
                prepareAndDefendStepContent = solution.getLiteral("Содержание_работы_подготовки_и_защиты_отчетных_материалов_из_пиикн_5").toString();
                orgStepContent = solution.getLiteral("Содержание_работы_организационного_этапа_из_пиикн_5").toString();
                individualStepContent = solution.getLiteral("Содержание_работы_выполнения_этапов_индивидуального_задания_из_пиикн_5").toString();
                individualStepDate = solution.getLiteral("Срок_завершения_индивидуального_этапа_из_пиикн_5").toString();
                individualStepForm = solution.getLiteral("Форма_отчетности_индивидуального_этапа_из_пиикн_5").toString();
            }
            qexec.close();

            return new ValidatorDataIndividualTask(minPages, font, sizeRange, style, orgStepEndDate,
                    prepareAndDefendStepEndDate, prepareAndDefendStepContent, orgStepContent, individualStepContent,
                    individualStepDate, individualStepForm);
        } catch (Exception e) {
            System.err.println(SPARQL_ERROR + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ValidatorDataDatesIndividualTask extractFromSparqlDates(String sparqlQuery) {
        try {
            Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            try (InputStream in = Main.class.getResourceAsStream(MODEL_FILENAME)) {
                model.read(in, "RDF/XML");
            }
            Query query = QueryFactory.create(sparqlQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            ResultSet results = qexec.execSelect();
            String dateOrgStart1 = null;
            String dateDefendStart2 = null;

            while(results.hasNext()){
                QuerySolution solution = results.next();
                dateOrgStart1 = solution.getLiteral("Дата_начала_организационного_этапа_из_пиикн_5").toString();
                dateDefendStart2 = solution.getLiteral("Дата_окончания_этапа_защиты_работы_из_пиикн_5").toString();
            }
            qexec.close();

            return new ValidatorDataDatesIndividualTask(dateOrgStart1, dateDefendStart2);
        } catch (Exception e) {
            System.err.println(SPARQL_ERROR + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
