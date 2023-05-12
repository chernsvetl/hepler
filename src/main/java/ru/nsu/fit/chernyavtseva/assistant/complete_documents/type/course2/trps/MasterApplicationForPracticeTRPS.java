package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course2.trps;

import com.github.petrovich4j.Case;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.ApplicationPracticeTRPS;

import java.util.HashMap;
import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.*;

public record MasterApplicationForPracticeTRPS() implements ApplicationPracticeTRPS {
    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION;

    static {
        DOC_FIELD_TO_SOLUTION = new HashMap<>();
        DOC_FIELD_TO_SOLUTION.put("имяСтудентаР", fullName("фио_студента", Case.Genitive));
        DOC_FIELD_TO_SOLUTION.put("группаСтудента", simple("группа_студента"));
        DOC_FIELD_TO_SOLUTION.put("полноеНаименованиеМестаПрактики", simple("место_практики_полное_наименование"));
        DOC_FIELD_TO_SOLUTION.put("имяРуководителяВКР", simple("фио_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("должностьРуководителяКраткоВКР", simple("должность_руководителя_вкр_кратко"));
        DOC_FIELD_TO_SOLUTION.put("обучСтудОбрПадеж", gender("фио_студента"));


    }

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "Прил 4_Заявление на практику_Магистратура_ТРПС_4 сем.docx";
    }
}
