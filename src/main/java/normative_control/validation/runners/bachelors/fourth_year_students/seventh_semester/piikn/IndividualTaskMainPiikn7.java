package normative_control.validation.runners.bachelors.fourth_year_students.seventh_semester.piikn;

import normative_control.validation.service.IndividualTaskDocumentValidator;
import normative_control.validation.service.impl.bachelors.piikn.fourth_year_students.seventh_semester.IndividualTaskDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataIndividualTask;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_PIIKN_7;
import static normative_control.utils.ValidationPaths.PIIKN_7_INDIVIDUAL_TASK_PATH;

public class IndividualTaskMainPiikn7 {
    public static void main(String[] args) {
        IndividualTaskDocumentValidator individualTaskDocumentValidator = new IndividualTaskDocumentValidatorImpl();
        ValidatorDataIndividualTask data = individualTaskDocumentValidator.extractFromSparql(QUERY_REPORT_PIIKN_7);
        if (data != null) {
            individualTaskDocumentValidator.validateDocxFiles(PIIKN_7_INDIVIDUAL_TASK_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
