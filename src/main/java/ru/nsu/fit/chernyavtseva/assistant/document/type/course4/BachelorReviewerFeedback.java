package ru.nsu.fit.chernyavtseva.assistant.document.type.course4;

import com.github.petrovich4j.Case;
import ru.nsu.fit.chernyavtseva.assistant.document.type.core.DocumentTemplate;
import ru.nsu.fit.chernyavtseva.assistant.document.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.document.type.core.ReviewerFeedback;

import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.document.type.ReplacementCreator.fullName;
import static ru.nsu.fit.chernyavtseva.assistant.document.type.ReplacementCreator.simple;

public record BachelorReviewerFeedback() implements ReviewerFeedback {

    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION = Map.of(
            "имяСтудентаР", fullName("фио_студента", Case.Genitive),
            "имяСтудентаИ", simple("фио_студента"),
            "имяРуководителяВКР", simple("фио_руководителя"),
            "должностьРуководителяВКР", simple("должность_руководителя"),
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
        return "09.03.01_PIiKN_VKR_recenziya.docx";
    }
}
