package normative_control.validation.runners.masters.first_year_students.iiids;

import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.validators.ValidatorDataReport;
import normative_control.validation.service.impl.masters.iiids.first_year_student.second_semester.ReportDocumentValidatorImpl;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_IIDS_2;
import static normative_control.utils.ValidationPaths.IIIDS_2_REPORT_PATH;

public class ReviewMainIiids2 {
    public static void main(String[] args) {
        ReportDocumentValidator reportDocumentValidator = new ReportDocumentValidatorImpl();
        ValidatorDataReport data = reportDocumentValidator.extractFromSparql(QUERY_REPORT_IIDS_2);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(IIIDS_2_REPORT_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
