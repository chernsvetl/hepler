package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4.knis;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.TaskKnis2;

import java.util.HashMap;
import java.util.Map;

public record BachelorTaskKnis2() implements TaskKnis2 {
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
        return "Бакалавриат_Задание_на_ВКР_КНИС(с_соруководителем).docx";
    }
}
