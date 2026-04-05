package normative_control.validation.runners.bachelors.fourth_year_students.eight_semester.piikn;

import normative_control.validation.service.ReviewDocumentValidator;
import normative_control.validation.service.impl.bachelors.piikn.fourth_year_students.eight_semester.ReviewDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReview;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_PIIKN_8;
import static normative_control.utils.ValidationPaths.PIIKN_8_REVIEW_PATH;

public class ReviewMainPiikn8 {
    public static void main(String[] args) {
        ReviewDocumentValidator reportDocumentValidator = new ReviewDocumentValidatorImpl();
        ValidatorDataReview data = reportDocumentValidator.extractFromSparql(QUERY_REPORT_PIIKN_8);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(PIIKN_8_REVIEW_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
