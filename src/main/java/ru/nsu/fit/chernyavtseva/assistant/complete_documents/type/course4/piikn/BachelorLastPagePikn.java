package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4.piikn;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.LastPagePikn;
import java.util.HashMap;
import java.util.Map;
import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.simple;

public record BachelorLastPagePikn() implements LastPagePikn {
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
        return "Последняя страница_бакалавриат_ПИиКН.docx";
    }
}



