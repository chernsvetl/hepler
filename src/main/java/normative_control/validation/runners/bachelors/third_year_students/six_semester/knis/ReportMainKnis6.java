package normative_control.validation.runners.bachelors.third_year_students.six_semester.knis;

import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.service.impl.bachelors.knis.third_year_student.six_semester.ReportDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReport;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT;
import static normative_control.utils.Paths.KNIS_6_REPORT_PATH;

public class ReportMainKnis6 {
    public static void main(String[] args) {
        ReportDocumentValidator reportDocumentValidator = new ReportDocumentValidatorImpl();
        ValidatorDataReport data = reportDocumentValidator.extractFromSparql(QUERY_REPORT);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(KNIS_6_REPORT_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}