package normative_control.validation.runners;

import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.service.impl.ReportDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReport;

import static normative_control.notifications.Notifications.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT;
import static normative_control.utils.Paths.REPORT_PIIKN8_PATH;

public class ReportMain {
    public static void main(String[] args) {
        ReportDocumentValidator reportDocumentValidator = new ReportDocumentValidatorImpl();
        ValidatorDataReport data = reportDocumentValidator.extractFromSparql(QUERY_REPORT);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(REPORT_PIIKN8_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
