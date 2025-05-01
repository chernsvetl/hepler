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
import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.validators.ValidatorDataReport;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import ru.nsu.fit.chernyavtseva.assistant.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static normative_control.notifications.Errors.*;
import static normative_control.output.FileLogger.writeValidationLogs;
import static normative_control.utils.Constants.ANSI_BLACK;
import static normative_control.utils.Constants.ANSI_GREEN;
import static normative_control.utils.Constants.ANSI_RED;
import static normative_control.utils.Constants.SPACE;
import static normative_control.utils.Constants.formatter;
import static normative_control.utils.Files.PIIKN_8_REPORT_FILE;
import static normative_control.utils.ValidationPaths.MODEL_FILENAME;
import static normative_control.validation.validators.CommonValidator.containsSection;
import static normative_control.validation.validators.CommonValidator.containsSectionWithSimilarity;
import static normative_control.validation.validators.CommonValidator.extractThemeText;
import static normative_control.validation.validators.CommonValidator.getFontSize;
import static normative_control.validation.validators.CommonValidator.getFontStyle;
import static normative_control.validation.validators.CommonValidator.isValidTheme;
import static normative_control.validation.validators.CommonValidator.parseSizeRange;

public class ReportDocumentValidatorImpl implements ReportDocumentValidator {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

        writeValidationLogs(LocalDateTime.now().format(formatter) + "\n", PIIKN_8_REPORT_FILE);
        for (File file : dir.listFiles((d, name) -> name.toLowerCase().endsWith(".docx"))) {
            JsonObject documentInfo = new JsonObject();
            documentInfo.addProperty("fileName", file.getName());
            List<String> errors = new ArrayList<>();

            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {

                int totalChars = document.getParagraphs().stream()
                        .mapToInt(p -> p.getText().length())
                        .sum();
                int pageCount = (int) Math.ceil(totalChars / 1700.0);
                var fontSize = getFontSize(document);
                String fontStyle = getFontStyle(document);
                String themeText = extractThemeText(document);

                boolean valid = true;
                boolean sizeIsValid = Arrays.stream(parseSizeRange(data.sizeRange)).anyMatch(size -> size == fontSize);
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
                    errorMessage.append(FONT_SIZE_ERROR).append(data.sizeRange).append(", есть: ").append(fontSize).append("). \n");
                    loggerInfo.append(FONT_SIZE_ERROR).append(data.sizeRange).append(", есть: ").append(fontSize).append("). \n");
                    errors.add(String.format("Неверный размер шрифта (ожидалось: %s, есть: %.1f)", data.sizeRange, fontSize));
                }
                if (!fontStyle.equals(data.style)) {
                    valid = false;
                    errorMessage.append(STYLE_ERROR).append(data.style).append(", есть: ").append(fontStyle).append("). \n");
                    loggerInfo.append(STYLE_ERROR).append(data.style).append(", есть: ").append(fontStyle).append("). \n");
                    errors.add(String.format("Неверный стиль шрифта (ожидалось: %s, есть: %s)", data.style, fontStyle));
                }
                if (!isValidTheme(themeText)) {
                    valid = false;
                    errorMessage.append(THEME_ERROR);
                    loggerInfo.append(THEME_ERROR);
                    errors.add(JSON_THEME_ERROR);
                }
                if (containsSectionWithSimilarity(document, data.full_text)) {
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

                documentInfo.addProperty("isValid", valid);
                if (!errors.isEmpty()) {
                    documentInfo.add("errors", gson.toJsonTree(errors));
                }

                if (valid) {
                    System.out.println(ANSI_GREEN + file.getName() + DOCUMENT_SUCCESS);
                    writeValidationLogs(file.getName() + DOCUMENT_SUCCESS, PIIKN_8_REPORT_FILE);
                    writeValidationLogs(NEXT_LINE, PIIKN_8_REPORT_FILE);
                } else {
                    System.out.println(ANSI_BLACK + file.getName() + DOCUMENT_ERROR + ANSI_BLACK + ANSI_RED + errorMessage + NEXT_LINE);
                    writeValidationLogs(file.getName() + DOCUMENT_ERROR + loggerInfo + NEXT_LINE, PIIKN_8_REPORT_FILE);
                }
            } catch (IOException e) {
                documentInfo.addProperty("error", JSON_READ_ERROR + e.getMessage());
                System.err.println(READING_FILE_ERROR + file.getName() + ": " + e.getMessage());
            }
            documents.add(documentInfo);
            writeValidationLogs(SPACE, PIIKN_8_REPORT_FILE);
        }
        validationResult.add("documents", gson.toJsonTree(documents));
        validationResult.addProperty("validationEndTime", LocalDateTime.now().format(formatter));
        validationResult.addProperty("status", JSON_VALIDATION_END);

        try (FileWriter writer = new FileWriter(PIIKN_8_REPORT_FILE + ".json")) {
            gson.toJson(validationResult, writer);
        } catch (IOException e) {
            System.err.println(JSON_WRITE_ERROR + e.getMessage());
        }

        System.out.println(ANSI_BLACK + VALIDATION_END + ANSI_BLACK);
        writeValidationLogs(VALIDATION_END, PIIKN_8_REPORT_FILE);
        writeValidationLogs(NEXT_LINE, PIIKN_8_REPORT_FILE);
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
            while(results.hasNext()){
                QuerySolution solution = results.next();
                minPages = solution.getLiteral("Минимальное_количество_страниц_отчета").getInt();
                sizeRange = solution.getLiteral("Размер").getString();
                style = solution.getLiteral("Стиль").getString();
                full_text = solution.getLiteral("Основной_текст_отчета").getString();
            }
            qexec.close();

            return new ValidatorDataReport(minPages, font, sizeRange, style, full_text);
        } catch (Exception e) {
            System.err.println(SPARQL_ERROR + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
