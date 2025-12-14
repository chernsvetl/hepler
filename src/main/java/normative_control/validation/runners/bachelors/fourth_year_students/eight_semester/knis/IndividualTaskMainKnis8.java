package normative_control.validation.runners.bachelors.fourth_year_students.eight_semester.knis;

import normative_control.validation.service.IndividualTaskDocumentValidator;
import normative_control.validation.service.impl.bachelors.knis.fourth_year_students.eight_semester.IndividualTaskDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataIndividualTask;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_KNIS_8;
import static normative_control.utils.ValidationPaths.KNIS_8_INDIVIDUAL_TASK_PATH;

public class IndividualTaskMainKnis8 {
    public static void main(String[] args) {
        IndividualTaskDocumentValidator individualTaskDocumentValidator = new IndividualTaskDocumentValidatorImpl();
        ValidatorDataIndividualTask data = individualTaskDocumentValidator.extractFromSparql(QUERY_REPORT_KNIS_8);
        if (data != null) {
            individualTaskDocumentValidator.validateDocxFiles(KNIS_8_INDIVIDUAL_TASK_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
