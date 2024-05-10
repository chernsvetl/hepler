package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course2.mda;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.TaskMda;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.TaskTrps;

import java.util.HashMap;
import java.util.Map;

public record MasterTaskMda() implements TaskMda {
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
        return "Магистратура_Задание на ВКР_МАД.docx";
    }
}
