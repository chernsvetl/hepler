package normative_control.validation.runners.masters.first_year_students.iot;

import normative_control.validation.service.ReportDocumentValidator;
import normative_control.validation.validators.ValidatorDataReport;
import normative_control.validation.service.impl.masters.iot.first_year_student.second_semester.ReportDocumentValidatorImpl;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_IOT_2;
import static normative_control.utils.ValidationPaths.IOT_2_REPORT_PATH;

public class ReportMainIot2 {
    public static void main(String[] args) {
        ReportDocumentValidator reportDocumentValidator = new ReportDocumentValidatorImpl();
        ValidatorDataReport data = reportDocumentValidator.extractFromSparql(QUERY_REPORT_IOT_2);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(IOT_2_REPORT_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
