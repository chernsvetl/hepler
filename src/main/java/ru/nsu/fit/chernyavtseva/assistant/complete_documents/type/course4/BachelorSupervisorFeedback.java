package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4;

import com.github.petrovich4j.Case;
import org.apache.commons.collections15.map.MultiKeyMap;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.SupervisorFeedback;

import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.fullName;
import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.simple;

public record BachelorSupervisorFeedback() implements SupervisorFeedback {
    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION = Map.of(
            "имяСтудентаР", fullName("фио_студента", Case.Genitive),
            "имяСтудентаИ", simple("фио_студента"),
            "имяРуководителяВКР", simple("фио_руководителя"),
            "УчСтепРукВКР", simple("ученая_степень_руководителя_ВКР"),
            "должностьРуководителяВКР", simple("должность_руководителя_вкр"),
            "фиоСоруководителяВКР", simple("фио_соруководителя_вкр"),
            "темаВКР", simple("тема_вкр"),
            "должностьСоруководителяВКР", simple("должность_соруководителя_вкр")
    );

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "09.03.01_PliKN_VKR_otzyv.docx";
    }

    @Override
    public boolean generateFor(Map<String, String> studentReplacements) {
        return (!studentReplacements.containsKey("фиоСоруководителяВКР") && !studentReplacements.containsKey("должностьСоруководителяВКР"));
    }
}
