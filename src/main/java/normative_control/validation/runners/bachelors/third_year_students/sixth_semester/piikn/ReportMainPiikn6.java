package normative_control.validation.runners.bachelors.third_year_students.sixth_semester.piikn;

import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.service.impl.bachelors.piikn.third_year_student.sixth_semester.ReportDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReport;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_PIIKN_6;
import static normative_control.utils.ValidationPaths.PIIKN_6_REPORT_PATH;

public class ReportMainPiikn6 {
    public static void main(String[] args) {
        ReportDocumentValidator reportDocumentValidator = new ReportDocumentValidatorImpl();
        ValidatorDataReport data = reportDocumentValidator.extractFromSparql(QUERY_REPORT_PIIKN_6);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(PIIKN_6_REPORT_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}

