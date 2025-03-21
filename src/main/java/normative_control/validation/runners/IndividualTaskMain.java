package normative_control.validation.runners;

import normative_control.validation.service.IndividualTaskDocumentValidator;
import normative_control.validation.service.impl.IndividualTaskDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataIndividualTask;

import static normative_control.notifications.Notifications.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT;
import static normative_control.utils.Paths.INDIVIDUAL_TASK_PIIKN8_PATH;

public class IndividualTaskMain {
    public static void main(String[] args) {
        IndividualTaskDocumentValidator individualTaskDocumentValidator = new IndividualTaskDocumentValidatorImpl();
        ValidatorDataIndividualTask data = individualTaskDocumentValidator.extractFromSparql(QUERY_REPORT);
        if (data != null) {
            individualTaskDocumentValidator.validateDocxFiles(INDIVIDUAL_TASK_PIIKN8_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
