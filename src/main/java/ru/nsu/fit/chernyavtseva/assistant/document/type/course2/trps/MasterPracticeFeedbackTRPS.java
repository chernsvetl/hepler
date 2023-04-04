package ru.nsu.fit.chernyavtseva.assistant.document.type.course2.trps;

import ru.nsu.fit.chernyavtseva.assistant.document.type.core.DocumentTemplate;
import ru.nsu.fit.chernyavtseva.assistant.document.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.document.type.core.PracticeFeedbackTRPS;

import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.document.type.ReplacementCreator.simple;

public record MasterPracticeFeedbackTRPS() implements PracticeFeedbackTRPS {

    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION = Map.of(
            "имяСтудентаИ", simple("фио_студента"),
            "группаСтудента", simple("группа_студента"),
            "местоПрактики", simple("место_практики"),
            "имяРуководителяОтОрганизации", simple("фио_орг_руководителя"),
            "должностьВОрганизации", simple("должность_орг_руководителя"),
            "темаВКР", simple("тема_вкр")
    );

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "Прил 3_Отзыв руководителя_Магистратура_ТРПС_4 сем.docx";
    }
}
