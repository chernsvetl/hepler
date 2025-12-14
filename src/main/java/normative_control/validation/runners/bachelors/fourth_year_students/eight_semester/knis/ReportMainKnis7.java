package normative_control.validation.runners.bachelors.fourth_year_students.eight_semester.knis;

import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.service.impl.bachelors.knis.fourth_year_students.seventh_semester.ReportDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReport;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_KNIS_7;
import static normative_control.utils.ValidationPaths.KNIS_7_REPORT_PATH;

public class ReportMainKnis7 {
    public static void main(String[] args) {
        ReportDocumentValidator reportDocumentValidator = new ReportDocumentValidatorImpl();
        ValidatorDataReport data = reportDocumentValidator.extractFromSparql(QUERY_REPORT_KNIS_7);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(KNIS_7_REPORT_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}

