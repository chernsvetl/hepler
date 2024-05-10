package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4.knis;

import com.github.petrovich4j.Case;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.AnnotationKnis;

import java.util.HashMap;
import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.fullName;
import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.simple;

public record BachelorAnnotationKnis() implements AnnotationKnis {
    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION;

    static {
        DOC_FIELD_TO_SOLUTION = new HashMap<>();
        DOC_FIELD_TO_SOLUTION.put("имяСтудентаР", fullName("фио_студента", Case.Genitive));
        DOC_FIELD_TO_SOLUTION.put("группаСтудента", simple("группа_студента"));

    }

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "Бакалавриат_Аннотация_КНИС.docx";
    }
}
