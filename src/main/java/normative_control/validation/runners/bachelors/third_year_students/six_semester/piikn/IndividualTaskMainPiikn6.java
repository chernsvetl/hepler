package normative_control.validation.runners.bachelors.third_year_students.six_semester.piikn;

import normative_control.validation.service.IndividualTaskDocumentValidator;
import normative_control.validation.service.impl.bachelors.piikn.third_year_students.six_semester.IndividualTaskDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataIndividualTask;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT;
import static normative_control.utils.Paths.PIIKN_6_INDIVIDUAL_TASK_PATH;

public class IndividualTaskMainPiikn6 {
    public static void main(String[] args) {
        IndividualTaskDocumentValidator individualTaskDocumentValidator = new IndividualTaskDocumentValidatorImpl();
        ValidatorDataIndividualTask data = individualTaskDocumentValidator.extractFromSparql(QUERY_REPORT);
        if (data != null) {
            individualTaskDocumentValidator.validateDocxFiles(PIIKN_6_INDIVIDUAL_TASK_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
