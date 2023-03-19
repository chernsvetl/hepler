package ru.nsu.fit.chernyavtseva.assistant.document.type;

import java.util.Map;

public interface DocumentTemplate {

    /**
     * template field -> replacer
     */
    Map<String, ReplacementCreator> replacements();

    /**
     * Original template file name.
     */
    String fileName();

    /**
     * Solution var name that will be used as prefix for generated document
     */
    default String prefixVarName() {
        return "фио_студента";
    }
}

