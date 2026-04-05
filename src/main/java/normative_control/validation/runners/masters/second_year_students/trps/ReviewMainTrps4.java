package normative_control.validation.runners.masters.second_year_students.trps;

import normative_control.validation.service.ReviewDocumentValidator;
import normative_control.validation.service.impl.masters.trps.second_year_student.fouth_semester.ReviewDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReview;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_TRPS_4;
import static normative_control.utils.ValidationPaths.TRPS_4_REVIEW_PATH;

public class ReviewMainTrps4 {
    public static void main(String[] args) {
        ReviewDocumentValidator reportDocumentValidator = new ReviewDocumentValidatorImpl();
        ValidatorDataReview data = reportDocumentValidator.extractFromSparql(QUERY_REPORT_TRPS_4);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(TRPS_4_REVIEW_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
