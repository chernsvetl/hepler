package normative_control.validation.runners.bachelors.fourth_year_students.seventh_semester.knis;

import normative_control.validation.service.ReviewDocumentValidator;
import normative_control.validation.service.impl.bachelors.knis.fourth_year_students.seventh_semester.ReviewDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReview;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_KNIS_7;
import static normative_control.utils.ValidationPaths.KNIS_7_REVIEW_PATH;

public class ReviewMainKnis7 {
    public static void main(String[] args) {
        ReviewDocumentValidator reportDocumentValidator = new ReviewDocumentValidatorImpl();
        ValidatorDataReview data = reportDocumentValidator.extractFromSparql(QUERY_REPORT_KNIS_7);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(KNIS_7_REVIEW_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
