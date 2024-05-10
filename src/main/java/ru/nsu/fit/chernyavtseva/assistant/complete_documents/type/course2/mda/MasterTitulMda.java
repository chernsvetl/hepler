package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course2.mda;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.TitulMda;
import java.util.HashMap;
import java.util.Map;

public record MasterTitulMda() implements TitulMda {
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
        return "09.04.01_KMiAD_VKR_titul.docx";
    }
}
