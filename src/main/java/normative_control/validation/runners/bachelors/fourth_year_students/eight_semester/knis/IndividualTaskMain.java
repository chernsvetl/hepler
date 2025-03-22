package normative_control.validation.runners.bachelors.fourth_year_students.eight_semester.knis;

import normative_control.validation.service.IndividualTaskDocumentValidator;
import normative_control.validation.service.impl.bachelors.knis.fouth_year_student.eight_semester.IndividualTaskDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataIndividualTask;

import static normative_control.notifications.Notifications.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT;
import static normative_control.utils.Paths.KNIS_8_INDIVIDUAL_TASK_PATH;

public class IndividualTaskMain {
    public static void main(String[] args) {
        IndividualTaskDocumentValidator individualTaskDocumentValidator = new IndividualTaskDocumentValidatorImpl();
        ValidatorDataIndividualTask data = individualTaskDocumentValidator.extractFromSparql(QUERY_REPORT);
        if (data != null) {
            individualTaskDocumentValidator.validateDocxFiles(KNIS_8_INDIVIDUAL_TASK_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
