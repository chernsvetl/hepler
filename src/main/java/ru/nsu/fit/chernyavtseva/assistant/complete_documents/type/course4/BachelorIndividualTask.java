package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.course4;

import com.github.petrovich4j.Case;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.core.IndividualTask;

import java.util.HashMap;
import java.util.Map;

import static ru.nsu.fit.chernyavtseva.assistant.complete_documents.type.ReplacementCreator.*;

public record BachelorIndividualTask() implements IndividualTask {
    private static final Map<String, ReplacementCreator> DOC_FIELD_TO_SOLUTION;

    static {
        DOC_FIELD_TO_SOLUTION = new HashMap<>();
        DOC_FIELD_TO_SOLUTION.put("имяСтудентаР", fullName("фио_студента", Case.Genitive));
        DOC_FIELD_TO_SOLUTION.put("имяСтудентаИ", simple("фио_студента"));
        DOC_FIELD_TO_SOLUTION.put("группаСтудента", simple("группа_студента"));
        DOC_FIELD_TO_SOLUTION.put("местоПрактики", simple("место_практики"));
        DOC_FIELD_TO_SOLUTION.put("полноеНаименованиеМестаПрактики", simple("место_практики_полное_наименование"));
        DOC_FIELD_TO_SOLUTION.put("приказПрактика", simple("приказ_практика"));
        DOC_FIELD_TO_SOLUTION.put("имяРуководителяОтНГУ", simple("фио_НГУ_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("должностьВНГУ", simple("должность_НГУ_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("имяРуководителяОтОрганизации", simple("фио_орг_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("должностьВОрганизации", simple("должность_орг_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("имяРуководителяВКР", simple("фио_руководителя"));
        DOC_FIELD_TO_SOLUTION.put("темаВКР", simple("тема_вкр"));
        DOC_FIELD_TO_SOLUTION.put("должностьРуководителяВКР", simple("должность_руководителя_вкр"));
        DOC_FIELD_TO_SOLUTION.put("должностьРуководителяКраткоВКР", simple("должность_руководителя_вкр_кратко"));
        DOC_FIELD_TO_SOLUTION.put("имяДляПодписи", simple("фио_подпись"));
        DOC_FIELD_TO_SOLUTION.put("обучСтудОбрПадеж", gender("фио_студента"));
        DOC_FIELD_TO_SOLUTION.put("бакДатаРук", simple("бак_дата_рук"));

    }

    @Override
    public Map<String, ReplacementCreator> replacements() {
        return DOC_FIELD_TO_SOLUTION;
    }

    @Override
    public String fileName() {
        return "Прил 1_ИЗ на практику_Бакалавриат_ПИиКН_8 семестр.docx";
    }
}
