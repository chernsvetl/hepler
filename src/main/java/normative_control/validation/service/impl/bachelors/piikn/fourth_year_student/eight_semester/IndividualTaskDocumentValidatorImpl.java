package normative_control.validation.service.impl.bachelors.piikn.fourth_year_student.eight_semester;

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
import normative_control.validation.validators.ValidatorDataIndividualTask;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import ru.nsu.fit.chernyavtseva.assistant.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static normative_control.notifications.Errors.*;
import static normative_control.output.FileLogger.writeValidationLogs;
import static normative_control.utils.Constants.ANSI_BLACK;
import static normative_control.utils.Constants.ANSI_GREEN;
import static normative_control.utils.Constants.ANSI_RED;
import static normative_control.utils.Constants.SPACE;
import static normative_control.utils.Constants.formatter;
import static normative_control.utils.Files.PIIKN_8_INDIVIDUAL_TASK_FILE;
import static normative_control.utils.ValidationPaths.MODEL_FILENAME;
import static normative_control.validation.validators.CommonValidator.extractThemeText;
import static normative_control.validation.validators.CommonValidator.getFontStyle;
import static normative_control.utils.TextSimilarity.areTextsSimilar;
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

        writeValidationLogs(LocalDateTime.now().format(formatter) + "\n", PIIKN_8_INDIVIDUAL_TASK_FILE);
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
                    errorMessage.append(STYLE_ERROR).append(data.style).append(", есть: ").append(fontStyle).append("). \n");
                    loggerInfo.append(STYLE_ERROR).append(data.style).append(", есть: ").append(fontStyle).append("). \n");
                    errors.add(String.format("Ошибка стиля: ожидается '%s', фактически '%s'", data.style, fontStyle));
                }

                if (!isValidTheme(themeText)) {
                    valid = false;
                    errorMessage.append(THEME_ERROR);
                    loggerInfo.append(THEME_ERROR);
                    errors.add(JSON_THEME_ERROR);
                }

                for (XWPFTable table : document.getTables()) {
                    var row = table.getRow(1);
                    var cellValue = row.getCell(2).getText().trim();
                    if (cellValue.equals(data.orgStepEndDate.trim())) {
                        valid = false;
                        errors.add(DATE_STEP_INDIVIDUAL_TASK_NOT_CHANGED + " не изменено в таблице.");
                        errorMessage.append(DATE_STEP_INDIVIDUAL_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
                        loggerInfo.append(DATE_STEP_INDIVIDUAL_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    boolean flag = false;
                    var row = table.getRow(1);
                    var cellValue = row.getCell(2).getText().trim();
                    if (cellValue.isEmpty()) {
                        flag = true;
                    }
                    if (flag) {
                        valid = false;
                        errors.add(DATE_STEP_INDIVIDUAL_TASK_NOT_CHANGED + "пуст.");
                        errorMessage.append(DATE_STEP_INDIVIDUAL_TASK_NOT_CHANGED + "пуст. \n");
                        loggerInfo.append(DATE_STEP_INDIVIDUAL_TASK_NOT_CHANGED + "пуст. \n");
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    boolean flag = false;
                    for (int rowIndex = 0; rowIndex < table.getRows().size(); rowIndex++) {
                        var row = table.getRow(rowIndex);
                        for (int cellIndex = 0; cellIndex < row.getTableCells().size(); cellIndex++) {
                            var cellValue = row.getCell(cellIndex).getText().trim();
                            if (cellValue.equals(data.prepareAndDefendStepEndDate.trim())) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            errors.add(DATE_STEP_DEFEND_TASK_NOT_CHANGED + " не изменено в таблице.");
                            errorMessage.append(DATE_STEP_DEFEND_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
                            loggerInfo.append(DATE_STEP_DEFEND_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
                            break;
                        }
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    boolean flag = false;
                    var row = table.getRow(2);
                    var cellValue = row.getCell(2).getText().trim();
                    if (cellValue.isEmpty()) {
                        flag = true;
                    }
                    if (flag) {
                        valid = false;
                        errors.add(JSON_DATE_END_INDIVIDUAL_TASK_IS_EMPTY_ERROR);
                        errorMessage.append(DATE_STEP_INDIVIDUAL_TASK_IS_EMPTY);
                        loggerInfo.append(DATE_STEP_INDIVIDUAL_TASK_IS_EMPTY);
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    var row = table.getRow(2);
                    boolean flag = false;
                    var cellValue = row.getCell(2).getText().trim();
                    if (areTextsSimilar(cellValue, data.individualStepDate)) {
                        flag = true;
                    } if (flag) {
                        valid = false;
                        errors.add(JSON_STEP_INDIVIDUAL_TASK_DATE_NOT_CHANGED);
                        errorMessage.append(DATE_STEP_INDIVIDUAL_TASK_IS_NOT_CHANGED);
                        loggerInfo.append(DATE_STEP_INDIVIDUAL_TASK_IS_NOT_CHANGED);
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    var row = table.getRow(2);
                    boolean flag = false;
                    var cellValue = row.getCell(3).getText().trim();
                    if (areTextsSimilar(cellValue, data.individualStepContent)) {
                        flag = true;
                    } if (flag) {
                        valid = false;
                        errors.add(CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED + " не изменено в таблице.");
                        errorMessage.append(CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
                        loggerInfo.append(CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    boolean flag = false;
                    var row = table.getRow(2);
                    var cellValue = row.getCell(3).getText().trim();
                    if (cellValue.isEmpty()) {
                        flag = true;
                    }
                    if (flag) {
                        valid = false;
                        errors.add(CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED + "пусто.");
                        errorMessage.append(CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED + "пусто. \n");
                        loggerInfo.append(CONTENT_STEP_INDIVIDUAL_TASK_NOT_CHANGED + "пусто. \n");
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    var row = table.getRow(2);
                    boolean flag = false;
                    var cellValue = row.getCell(4).getText().trim();
                    if (areTextsSimilar(cellValue, data.individualStepForm)) {
                        flag = true;
                    } if (flag) {
                        valid = false;
                        errors.add(STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED + " не изменена в таблице.");
                        errorMessage.append(STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED).append(" не изменена в таблице. \n");
                        loggerInfo.append(STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED).append(" не изменена в таблице. \n");
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    boolean flag = false;
                    var row = table.getRow(2);
                    var cellValue = row.getCell(4).getText().trim();
                    if (cellValue.isEmpty()) {
                        flag = true;
                    }
                    if (flag) {
                        valid = false;
                        errors.add(STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED + "пустая.");
                        errorMessage.append(STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED + "пустая. \n");
                        loggerInfo.append(STEP_INDIVIDUAL_TASK_FORM_NOT_CHANGED + "пустая. \n");
                    }
                }

                for (XWPFTable table : document.getTables()) {
                    boolean flag = false;
                    for (int rowIndex = 0; rowIndex < table.getRows().size(); rowIndex++) {
                        var row = table.getRow(rowIndex);
                        for (int cellIndex = 0; cellIndex < row.getTableCells().size(); cellIndex++) {
                            var cellValue = row.getCell(cellIndex).getText().trim();
                            if (cellValue.equals(data.prepareAndDefendStepContent.trim())) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            errors.add(CONTENT_STEP_DEFEND_TASK_NOT_CHANGED + " не изменено в таблице.");
                            errorMessage.append(CONTENT_STEP_DEFEND_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
                            loggerInfo.append(CONTENT_STEP_DEFEND_TASK_NOT_CHANGED).append(" не изменено в таблице. \n");
                            break;
                        }
                    }
                }

                documentResult.addProperty("isValid", valid);
                if (!errors.isEmpty()) {
                    documentResult.add("errors", gson.toJsonTree(errors));
                }

                if (valid) {
                    System.out.println(ANSI_GREEN + file.getName() + DOCUMENT_SUCCESS);
                    writeValidationLogs(file.getName() + DOCUMENT_SUCCESS, PIIKN_8_INDIVIDUAL_TASK_FILE);
                } else {
                    System.out.println(ANSI_BLACK + file.getName() + DOCUMENT_ERROR + ANSI_BLACK + ANSI_RED +
                            String.join("\n", errors) + "\n");
                    writeValidationLogs(file.getName() + DOCUMENT_ERROR + loggerInfo + "\n", PIIKN_8_INDIVIDUAL_TASK_FILE);
                }

            } catch (IOException e) {
                documentResult.addProperty("error", JSON_READ_ERROR + e.getMessage());
                System.err.println(READING_FILE_ERROR + file.getName() + ": " + "\n" + e.getMessage());
                writeValidationLogs(READING_FILE_ERROR + file.getName() + ": "  + "\n" + e.getMessage(), PIIKN_8_INDIVIDUAL_TASK_FILE);
            }
            writeValidationLogs(SPACE, PIIKN_8_INDIVIDUAL_TASK_FILE);
            validationResults.add(documentResult);
        }

        validationSession.add("documents", gson.toJsonTree(validationResults));
        validationSession.addProperty("validationEndTime", LocalDateTime.now().format(formatter));
        validationSession.addProperty("status", JSON_VALIDATION_END);

        try (FileWriter writer = new FileWriter(PIIKN_8_INDIVIDUAL_TASK_FILE + ".json")) {
            gson.toJson(validationSession, writer);
        } catch (IOException e) {
            System.err.println(JSON_WRITE_ERROR + e.getMessage());
        }
        writeValidationLogs(VALIDATION_END, PIIKN_8_INDIVIDUAL_TASK_FILE);
        System.out.println(ANSI_BLACK + VALIDATION_END + ANSI_BLACK);
        writeValidationLogs(NEXT_LINE, PIIKN_8_INDIVIDUAL_TASK_FILE);
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
                orgStepEndDate = solution.getLiteral("Срок_завершения_организационного_этапа_из_пиикн_8").toString();
                prepareAndDefendStepEndDate = solution.getLiteral("Срок_завершения_этапа_подготовки_и_защиты_отчетных_материалов_из_пиикн_8").toString();
                prepareAndDefendStepContent = solution.getLiteral("Содержание_работы_подготовки_и_защиты_отчетных_материалов_из_пиикн_8").toString();
                orgStepContent = solution.getLiteral("Содержание_работы_организационного_этапа_из_пиикн_8").toString();
                individualStepContent = solution.getLiteral("Содержание_работы_выполнения_этапов_индивидуального_задания_из_пиикн_8").toString();
                individualStepDate = solution.getLiteral("Срок_завершения_индивидуального_этапа_из_пиикн_8").toString();
                individualStepForm = solution.getLiteral("Форма_отчетности_индивидуального_этапа_из_пиикн_8").toString();
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
}
