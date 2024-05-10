package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4.knis;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.LastPageKnis;

import java.util.HashMap;
import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.simple;

public record BachelorLastPageKnis() implements LastPageKnis {
    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION;

    static {
        DOC_FIELD_TO_SOLUTION = new HashMap<>();
        DOC_FIELD_TO_SOLUTION.put("имяСтудентаИ", simple("фио_студента"));
        DOC_FIELD_TO_SOLUTION.put("группаСтудента", simple("группа_студента"));

    }

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "Последняя страница_бакалавриат_КНИС.docx";
    }
}



