package normative_control.validation.service;


import normative_control.validation.validators.ValidatorDataReview;

public interface ReviewDocumentValidator {
    ValidatorDataReview extractFromSparql(String sparqlQuery);
    void validateDocxFiles(String directoryPath, ValidatorDataReview data);
}
