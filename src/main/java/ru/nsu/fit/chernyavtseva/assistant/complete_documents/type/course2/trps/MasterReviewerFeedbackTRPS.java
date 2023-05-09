package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course2.trps;

import com.github.petrovich4j.Case;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.ReviewerFeedbackTRPS;

import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.fullName;
import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.simple;

public record MasterReviewerFeedbackTRPS() implements ReviewerFeedbackTRPS {

    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION = Map.of(
            "имяСтудентаР", fullName("фио_студента", Case.Genitive),
            "имяСтудентаИ", simple("фио_студента"),
            "имяРуководителяВКР", simple("фио_руководителя"),
            "темаВКР", simple("тема_вкр"),
            "фиоРецензента", simple("фио_рецензента"),
            "должностьРецензента", simple("должность_рецензента")
    );

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "09.04.01_TRPS_VKR_recenziya.docx";
    }

//    @Override
//    public boolean generateFor(Map<String, String> studentReplacements) {
//        return studentReplacements.containsKey("фиоСоруководителяВКР");
//    }
}
