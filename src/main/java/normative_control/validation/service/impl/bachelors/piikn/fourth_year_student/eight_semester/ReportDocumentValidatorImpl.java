package normative_control.validation.service.impl.bachelors.piikn.fourth_year_student.eight_semester;

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
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static normative_control.notifications.Notifications.*;
import static normative_control.output.FileLogger.writeValidationLogs;
import static normative_control.utils.Constants.ANSI_BLACK;
import static normative_control.utils.Constants.ANSI_GREEN;
import static normative_control.utils.Constants.ANSI_RED;
import static normative_control.utils.Constants.SPACE;
import static normative_control.utils.Files.PIIKN_8_REPORT_FILE;
import static normative_control.utils.Paths.MODEL_FILENAME;
import static normative_control.validation.validators.CommonValidator.extractThemeText;
import static normative_control.validation.validators.CommonValidator.getFontSize;
import static normative_control.validation.validators.CommonValidator.getFontStyle;
import static normative_control.validation.validators.CommonValidator.isValidTheme;
import static normative_control.validation.validators.CommonValidator.parseSizeRange;

public class ReportDocumentValidatorImpl implements ReportDocumentValidator {

    @Override
    public void validateDocxFiles(String directoryPath, ValidatorDataReport data) {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            System.err.println("Указанный путь не является директорией.");
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
                var loggerInfo = new StringBuilder();

                if (pageCount < data.minPages) {
                    valid = false;
                    errorMessage.append(PAGES_ERROR).append(data.minPages).append(", есть: ").append(pageCount).append("). \n");
                    loggerInfo.append(PAGES_ERROR).append(data.minPages).append(", есть: ").append(pageCount).append("). \n");
                }
                if (!sizeIsValid) {
                    valid = false;
                    errorMessage.append(FONT_SIZE_ERROR).append(data.sizeRange).append(", есть: ").append(fontSize).append("). \n");
                    loggerInfo.append(FONT_SIZE_ERROR).append(data.sizeRange).append(", есть: ").append(fontSize).append("). \n");
                }
                if (!fontStyle.equals(data.style)) {
                    valid = false;
                    errorMessage.append(STYLE_ERROR).append(data.style).append(", есть: ").append(fontStyle).append("). \n");
                    loggerInfo.append(STYLE_ERROR).append(data.style).append(", есть: ").append(fontStyle).append("). \n");
                }
                if (!isValidTheme(themeText)) {
                    valid = false;
                    errorMessage.append(THEME_ERROR);
                    loggerInfo.append(THEME_ERROR + "\n");
                }
                if (valid) {
                    System.out.println(ANSI_GREEN + file.getName() + DOCUMENT_SUCCESS);
                    writeValidationLogs(file.getName() + DOCUMENT_SUCCESS, PIIKN_8_REPORT_FILE);
                } else {
                    System.out.println(ANSI_BLACK + file.getName() + DOCUMENT_ERROR + ANSI_BLACK + ANSI_RED + errorMessage + "\n");
                    writeValidationLogs(file.getName() + DOCUMENT_ERROR + loggerInfo + "\n", PIIKN_8_REPORT_FILE);
                }
            } catch (IOException e) {
                System.err.println(READING_FILE_ERROR + file.getName() + ": " + e.getMessage());
            }
            writeValidationLogs(SPACE, PIIKN_8_REPORT_FILE);
        }
        System.out.println(ANSI_BLACK + VALIDATION_END + ANSI_BLACK);
        writeValidationLogs(VALIDATION_END, PIIKN_8_REPORT_FILE);
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
            while(results.hasNext()){
                QuerySolution solution = results.next();
                minPages = solution.getLiteral("Минимальное_количество_страниц_отчета").getInt();
                sizeRange = solution.getLiteral("Размер").getString();
                style = solution.getLiteral("Стиль").getString();
            }
            qexec.close();

            return new ValidatorDataReport(minPages, font, sizeRange, style);
        } catch (Exception e) {
            System.err.println(SPARQL_ERROR + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
