package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4.piikn;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.TaskPikn;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.TaskPikn2;

import java.util.HashMap;
import java.util.Map;

public record BachelorTaskPikn2() implements TaskPikn2 {
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
        return "Бакалавриат_Задание_на_ВКР_ПИиКН_(с_соруководителем).docx";
    }
}
