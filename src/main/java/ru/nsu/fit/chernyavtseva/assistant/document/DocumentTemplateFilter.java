package ru.nsu.fit.chernyavtseva.assistant.document;

import ru.nsu.fit.chernyavtseva.assistant.document.type.core.DocumentTemplate;

import java.util.function.Predicate;

@FunctionalInterface
public interface DocumentTemplateFilter extends Predicate<DocumentTemplate> {
    DocumentTemplateFilter EMPTY = d -> true;
}
