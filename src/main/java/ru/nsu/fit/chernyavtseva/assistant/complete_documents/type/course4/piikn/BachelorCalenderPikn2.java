package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4.piikn;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.CalenderPikn2;

import java.util.HashMap;
import java.util.Map;


public record BachelorCalenderPikn2() implements CalenderPikn2 {
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
        return "ПИиКН_Бакалавриат_Календарный_график_выполнения_ВКР_(с_соруководителем).docx";
    }
}
