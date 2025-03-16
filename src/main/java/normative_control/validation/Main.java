package normative_control.validation;

import static normative_control.notifications.Notifications.SPARQL_ERROR;
import static normative_control.validation.DocumentValidator.extractFromSparql;
import static normative_control.validation.DocumentValidator.validateDocxFiles;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT;

public class Main {
    public static void main(String[] args) {
        String docxDirectoryPath = "C:\\Users\\User\\Desktop\\hepler\\hepler\\filled_docs\\report";

        DocumentValidator.ValidatorData data = extractFromSparql(QUERY_REPORT);
        if (data != null) {
            validateDocxFiles(docxDirectoryPath, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
