package normative_control.validation.service;

import normative_control.validation.validators.ValidatorDataDatesIndividualTask;
import normative_control.validation.validators.ValidatorDataIndividualTask;

public interface IndividualTaskDocumentValidator {
    ValidatorDataIndividualTask extractFromSparql(String sparqlQuery);

    ValidatorDataDatesIndividualTask extractFromSparqlDates(String sparqlQuery);
    void validateDocxFiles(String directoryPath, ValidatorDataIndividualTask data);
}
