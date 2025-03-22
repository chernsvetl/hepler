package normative_control.validation.runners.bachelors.fourth_year_students.eight_semester.knis;

import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.service.impl.bachelors.knis.fouth_year_student.eight_semester.ReportDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReport;

import static normative_control.notifications.Notifications.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT;
import static normative_control.utils.Paths.KNIS_8_REPORT_PATH;

public class ReportMain {
    public static void main(String[] args) {
        ReportDocumentValidator reportDocumentValidator = new ReportDocumentValidatorImpl();
        ValidatorDataReport data = reportDocumentValidator.extractFromSparql(QUERY_REPORT);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(KNIS_8_REPORT_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
