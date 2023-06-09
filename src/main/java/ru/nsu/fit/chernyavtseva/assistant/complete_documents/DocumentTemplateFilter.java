package ru.nsu.fit.chernyavtseva.assistant.complete_documents;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.DocumentTemplate;

import java.util.function.Predicate;

@FunctionalInterface
public interface DocumentTemplateFilter extends Predicate<DocumentTemplate> {
    DocumentTemplateFilter EMPTY = d -> true;
}
