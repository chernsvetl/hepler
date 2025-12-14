package normative_control.validation.runners.bachelors.third_year_students.fifth_semester.piikn;

import normative_control.validation.service.IndividualTaskDocumentValidator;
import normative_control.validation.service.impl.bachelors.piikn.third_year_student.fifth_semester.IndividualTaskDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataIndividualTask;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_PIIKN_5;
import static normative_control.utils.ValidationPaths.PIIKN_5_INDIVIDUAL_TASK_PATH;

public class IndividualTaskMainPiikn5 {
    public static void main(String[] args) {
        IndividualTaskDocumentValidator individualTaskDocumentValidator = new IndividualTaskDocumentValidatorImpl();
        ValidatorDataIndividualTask data = individualTaskDocumentValidator.extractFromSparql(QUERY_REPORT_PIIKN_5);
        if (data != null) {
            individualTaskDocumentValidator.validateDocxFiles(PIIKN_5_INDIVIDUAL_TASK_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
