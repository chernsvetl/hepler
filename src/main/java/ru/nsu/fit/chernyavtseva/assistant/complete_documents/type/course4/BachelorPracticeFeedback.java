package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4;

import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.PracticeFeedback;

import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.genderFio;
import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.simple;

public record BachelorPracticeFeedback() implements PracticeFeedback {

    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION = Map.of(
            "имяСтудентаИ", simple("фио_студента"),
            "группаСтудента", simple("группа_студента"),
            "местоПрактики", simple("место_практики"),
            "наименованиеОрганизации", simple("наименование_организации"),
            "имяРуководителяОтОрганизации", simple("фио_орг_руководителя"),
            "должностьВОрганизации", simple("должность_орг_руководителя"),
            "темаВКР", simple("тема_вкр"),
            "имяДляПодписи", simple("фио_подпись"),
            "обучФиоИм", genderFio("фио_студента")
    );

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "Прил 3_Отзыв руководителя практики_Бакалавриат_ПИиКН_8 семестр.docx";
    }
}
