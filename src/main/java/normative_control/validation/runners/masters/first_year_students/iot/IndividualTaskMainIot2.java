package normative_control.validation.runners.masters.first_year_students.iot;

import normative_control.validation.service.IndividualTaskDocumentValidator;
import normative_control.validation.validators.ValidatorDataIndividualTask;
import normative_control.validation.service.impl.masters.iot.first_year_student.second_semester.IndividualTaskDocumentValidatorImpl;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_IOT_2;
import static normative_control.utils.ValidationPaths.IOT_2_INDIVIDUAL_TASK_PATH;

public class IndividualTaskMainIot2 {
    public static void main(String[] args) {
        IndividualTaskDocumentValidator individualTaskDocumentValidator = new IndividualTaskDocumentValidatorImpl();
        ValidatorDataIndividualTask data = individualTaskDocumentValidator.extractFromSparql(QUERY_REPORT_IOT_2);
        if (data != null) {
            individualTaskDocumentValidator.validateDocxFiles(IOT_2_INDIVIDUAL_TASK_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
