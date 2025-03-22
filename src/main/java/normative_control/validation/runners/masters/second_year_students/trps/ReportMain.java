package normative_control.validation.runners.masters.second_year_students.trps;

import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.service.impl.masters.trps.second_year_student.fouth_semester.ReportDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReport;

import static normative_control.notifications.Notifications.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT;
import static normative_control.utils.Paths.TRPS_4_REPORT_PATH;

public class ReportMain {
    public static void main(String[] args) {
        ReportDocumentValidator reportDocumentValidator = new ReportDocumentValidatorImpl();
        ValidatorDataReport data = reportDocumentValidator.extractFromSparql(QUERY_REPORT);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(TRPS_4_REPORT_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
