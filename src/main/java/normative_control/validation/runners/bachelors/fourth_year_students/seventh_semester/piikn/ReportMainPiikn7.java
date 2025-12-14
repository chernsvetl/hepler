package normative_control.validation.runners.bachelors.fourth_year_students.seventh_semester.piikn;

import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.service.impl.bachelors.piikn.fourth_year_students.seventh_semester.ReportDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReport;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_PIIKN_7;
import static normative_control.utils.ValidationPaths.PIIKN_7_REPORT_PATH;

public class ReportMainPiikn7 {
    public static void main(String[] args) {
        ReportDocumentValidator reportDocumentValidator = new ReportDocumentValidatorImpl();
        ValidatorDataReport data = reportDocumentValidator.extractFromSparql(QUERY_REPORT_PIIKN_7);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(PIIKN_7_REPORT_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}

