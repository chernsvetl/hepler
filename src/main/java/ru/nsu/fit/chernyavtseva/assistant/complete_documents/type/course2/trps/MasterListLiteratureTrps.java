package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course2.trps;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.ListLiteratureMda;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.ListLiteratureTrps;

import java.util.HashMap;
import java.util.Map;

public record MasterListLiteratureTrps() implements ListLiteratureTrps {
    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION;

    static {
        DOC_FIELD_TO_SOLUTION = new HashMap<>();
    }

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "Список_использованных_источников_и_литературы_магистратура_ТРПС.docx";
    }
}