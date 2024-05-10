package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course2.mda;

import com.github.petrovich4j.Case;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.AnnotationMda;
import java.util.HashMap;
import java.util.Map;
import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.*;

public record MasterAnnotationMda() implements AnnotationMda {
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
        return "Магистратура_Аннотация_МДА.docx";
    }
}
