package normative_control.validation.service.impl.masters.iot.second_year_student.fouth_semester;

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
import java.io.IOException;
import java.io.InputStream;

import static normative_control.notifications.Notifications.DOCUMENT_ERROR;
import static normative_control.notifications.Notifications.DOCUMENT_SUCCESS;
import static normative_control.notifications.Notifications.PATH_ERROR;
import static normative_control.notifications.Notifications.READING_FILE_ERROR;
import static normative_control.notifications.Notifications.SPARQL_ERROR;
import static normative_control.notifications.Notifications.STYLE_ERROR;
import static normative_control.notifications.Notifications.VALIDATION_END;
import static normative_control.output.FileLogger.writeValidationLogs;
import static normative_control.utils.Constants.ANSI_BLACK;
import static normative_control.utils.Constants.ANSI_GREEN;
import static normative_control.utils.Constants.ANSI_RED;
import static normative_control.utils.Constants.SPACE;
import static normative_control.utils.Files.IOT_4_INDIVIDUAL_TASK_FILE;
import static normative_control.utils.Paths.MODEL_FILENAME;
import static normative_control.utils.TextSimilarity.areTextsSimilar;
import static normative_control.validation.validators.CommonValidator.getFontStyle;

public class IndividualTaskDocumentValidatorImpl implements IndividualTaskDocumentValidator {

    @Override
    public void validateDocxFiles(String directoryPath, ValidatorDataIndividualTask data) {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            System.err.println(PATH_ERROR);
            return;
        }
        for (File file : dir.listFiles((d, name) -> name.toLowerCase().endsWith(".docx"))) {
            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {

                String fontStyle = getFontStyle(document);

                boolean valid = true;
                var errorMessage = new StringBuilder();
                var loggerInfo = new StringBuilder();

                if (!fontStyle.equals(data.style)) {
                    valid = false;
                    errorMessage.append(STYLE_ERROR).append(data.style).append(", есть: ").append(fontStyle).append("). \n");
                    loggerInfo.append(STYLE_ERROR).append(data.style).append(", есть: ").append(fontStyle).append("). \n");
                }
                for (XWPFTable table : document.getTables()) {
                    boolean flag = false;
                    var row = table.getRow(1);
                    var cellValue = row.getCell(2).getText().trim();
                    if (cellValue.equals(data.orgStepEndDate.trim())) {
                        flag = true;
                    }
                    if (flag) {
                        valid = false;
                        errorMessage.append(data.orgStepEndDate).append(" не изменено в таблице. \n");
                        loggerInfo.append(data.orgStepEndDate).append(" не изменено в таблице. \n");
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
                            errorMessage.append(data.prepareAndDefendStepEndDate).append(" не изменено в таблице. \n");
                            loggerInfo.append(data.prepareAndDefendStepEndDate).append(" не изменено в таблице. \n");
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
                        errorMessage.append("Срок завершения этапа индивидуального задания пуст. \n");
                        loggerInfo.append("Срок завершения этапа индивидуального задания пуст. \n");
                    }
                }
                for (XWPFTable table : document.getTables()) {
                    boolean flag = false;
                    var row = table.getRow(2);
                    var cellValue = row.getCell(3).getText().trim();
                    if (areTextsSimilar(cellValue, data.individualStepContent)) {
                        flag = true;
                    }
                    if (flag) {
                        valid = false;
                        errorMessage.append("Содержание работы индивидуального этапа").append(" не изменено в таблице. \n");
                        loggerInfo.append("Содержание работы индивидуального этапа").append(" не изменено в таблице. \n");
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
                            errorMessage.append("Содержание работы этапа защиты материалов").append(" не изменено в таблице. \n");
                            loggerInfo.append("Содержание работы этапа защиты материалов").append(" не изменено в таблице. \n");
                            break;
                        }
                    }
                }
                if (valid) {
                    System.out.println(ANSI_GREEN + file.getName() + DOCUMENT_SUCCESS);
                    writeValidationLogs(file.getName() + DOCUMENT_SUCCESS, IOT_4_INDIVIDUAL_TASK_FILE);
                } else {
                    System.out.println(ANSI_BLACK + file.getName() + DOCUMENT_ERROR + ANSI_BLACK + ANSI_RED + errorMessage + "\n");
                    writeValidationLogs(file.getName() + DOCUMENT_ERROR + loggerInfo + "\n", IOT_4_INDIVIDUAL_TASK_FILE);
                }
            } catch (IOException e) {
                System.err.println(READING_FILE_ERROR + file.getName() + ": "  + "\n" + e.getMessage());
                writeValidationLogs(READING_FILE_ERROR + file.getName() + ": "  + "\n" + e.getMessage(), IOT_4_INDIVIDUAL_TASK_FILE);
            }
            writeValidationLogs(SPACE, IOT_4_INDIVIDUAL_TASK_FILE);
        }
        writeValidationLogs(VALIDATION_END, IOT_4_INDIVIDUAL_TASK_FILE);
        System.out.println(ANSI_BLACK + VALIDATION_END + ANSI_BLACK);
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
            while(results.hasNext()){
                QuerySolution solution = results.next();
                minPages = solution.getLiteral("Минимальное_количество_страниц_отчета").getInt();
                sizeRange = solution.getLiteral("Размер").getString();
                style = solution.getLiteral("Стиль").getString();
                orgStepEndDate = solution.getLiteral("Срок_завершения_организационного_этапа_из_iot_4").toString();
                prepareAndDefendStepEndDate = solution.getLiteral("Срок_завершения_этапа_подготовки_и_защиты_отчетных_материалов_из_iot_4").toString();
                prepareAndDefendStepContent = solution.getLiteral("Содержание_работы_подготовки_и_защиты_отчетных_материалов_из_iot_4").toString();
                orgStepContent = solution.getLiteral("Содержание_работы_организационного_этапа_из_iot_4").toString();
                individualStepContent = solution.getLiteral("Содержание_работы_выполнения_этапов_индивидуального_задания_из_iot_4").toString();
            }
            qexec.close();

            return new ValidatorDataIndividualTask(minPages, font, sizeRange, style, orgStepEndDate,
                    prepareAndDefendStepEndDate, prepareAndDefendStepContent, orgStepContent, individualStepContent);
        } catch (Exception e) {
            System.err.println(SPARQL_ERROR + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
