package normative_control.validation.service;

import normative_control.validation.validators.ValidatorDataReport;

public interface ReportDocumentValidator {
    ValidatorDataReport extractFromSparql(String sparqlQuery);
    void validateDocxFiles(String directoryPath, ValidatorDataReport data);
}
