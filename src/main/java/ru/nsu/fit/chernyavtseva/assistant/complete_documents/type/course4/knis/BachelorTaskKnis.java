package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4.knis;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.TaskKnis;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.TaskPikn;

import java.util.HashMap;
import java.util.Map;

public record BachelorTaskKnis() implements TaskKnis {
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
        return "Бакалавриат_Задание на ВКР_КНИС.docx";
    }
}
