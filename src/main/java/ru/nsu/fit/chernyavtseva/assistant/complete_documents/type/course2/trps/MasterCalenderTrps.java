package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course2.trps;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.CalenderTrps;

import java.util.HashMap;
import java.util.Map;

public record MasterCalenderTrps() implements CalenderTrps {
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
        return "Магистратура_Календарный_график_выполнения_ВКР_ТРПС.docx";
    }
}