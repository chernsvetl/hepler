package normative_control.validation.runners.masters.second_year_students.iiids;

import normative_control.validation.service.ReviewDocumentValidator;
import normative_control.validation.service.impl.masters.iiids.second_year_student.fouth_semester.ReviewDocumentValidatorImpl;
import normative_control.validation.validators.ValidatorDataReview;

import static normative_control.notifications.Errors.SPARQL_ERROR;
import static normative_control.query_model.ValidatorQuery.QUERY_REPORT_IIDS_4;
import static normative_control.utils.ValidationPaths.IIIDS_4_REVIEW_PATH;

public class ReviewMainIiids4 {
    public static void main(String[] args) {
        ReviewDocumentValidator reportDocumentValidator = new ReviewDocumentValidatorImpl();
        ValidatorDataReview data = reportDocumentValidator.extractFromSparql(QUERY_REPORT_IIDS_4);
        if (data != null) {
            reportDocumentValidator.validateDocxFiles(IIIDS_4_REVIEW_PATH, data);
        } else {
            System.err.println(SPARQL_ERROR);
        }
    }
}
