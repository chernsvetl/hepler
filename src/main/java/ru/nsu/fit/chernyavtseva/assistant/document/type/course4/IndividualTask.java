package ru.nsu.fit.chernyavtseva.assistant.document.type.course4;

import com.github.petrovich4j.Case;
import ru.nsu.fit.chernyavtseva.assistant.document.type.DocumentTemplate;
import ru.nsu.fit.chernyavtseva.assistant.document.type.ReplacementCreator;

import java.util.HashMap;
import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.document.type.ReplacementCreator.fullName;
import static ru.nsu.fit.chernyavtseva.assistant.document.type.ReplacementCreator.simple;

public record IndividualTask() implements DocumentTemplate {
    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION;

    static {
        DOC_FIELD_TO_SOLUTION = new HashMap<>();
        DOC_FIELD_TO_SOLUTION.put("имяСтудентаР", fullName("фио_студента", Case.Genitive));
        DOC_FIELD_TO_SOLUTION.put("имяСтудентаИ", simple("фио_студента"));
        DOC_FIELD_TO_SOLUTION.put("группаСтудента", simple("группа_студента"));
        DOC_FIELD_TO_SOLUTION.put("местоПрактики", simple("место_практики"));
        DOC_FIELD_TO_SOLUTION.put("приказПрактика", simple("приказ_практика"));
        DOC_FIELD_TO_SOLUTION.put("имяРуководителяОтНГУ", simple("фио_НГУ_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("должностьВНГУ", simple("должность_НГУ_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("имяРуководителяОтОрганизации", simple("фио_орг_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("должностьВОрганизации", simple("должность_орг_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("имяРуководителяВКР", simple("фио_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("темаВКР", simple("тема_вкр"));
        DOC_FIELD_TO_SOLUTION.put("должностьРуководителяВКР", simple("должность_руководителя"));
    }

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "individual_task.docx";
    }
}
