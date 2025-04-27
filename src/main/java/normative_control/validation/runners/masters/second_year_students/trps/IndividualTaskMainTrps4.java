package normative_control.validation.runners.masters.second_year_students.trps;

import normative_control.validation.service.IndividualTaskDocumentValidator;
import normative_control.validation.service.impl.masters.trps.second_year_student.fouth_semester.IndividualTaskDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataIndividualTask;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT;
import static normative_control.utils.ValidationPaths.TRPS_4_INDIVIDUAL_TASK_PATH;

public class IndividualTaskMainTrps4 {
    public static void main(String[] args) {
        IndividualTaskDocumentValidator individualTaskDocumentValidator = new IndividualTaskDocumentValidatorImpl();
        ValidatorDataIndividualTask data = individualTaskDocumentValidator.extractFromSparql(QUERY_REPORT);
        if (data != null) {
            individualTaskDocumentValidator.validateDocxFiles(TRPS_4_INDIVIDUAL_TASK_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
