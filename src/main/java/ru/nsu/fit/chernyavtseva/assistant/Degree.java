package ru.nsu.fit.chernyavtseva.assistant;

import ru.nsu.fit.chernyavtseva.assistant.document.type.*;
import ru.nsu.fit.chernyavtseva.assistant.document.type.course2.*;
import ru.nsu.fit.chernyavtseva.assistant.document.type.course4.*;

sealed public interface Degree {

    Degree[] DEGREES = new Degree[]{new Bachelor(), new Master()};

    static Degree[] all() {
        return DEGREES;
    }

    String name();

    String dir();

    DocumentTemplate[] toGenerate();
}

final class Bachelor implements Degree {

    /**
     * Degree name, used inside query
     */
    @Override
    public String name() {
        return "Бакалавриат";
    }

    /**
     * Subdirectory where to search for tempates / where to dump generated docs
     */
    @Override
    public String dir() {
        return "bachelors/4th_course";
    }

    /**
     * Which templates should be generated
     */
    @Override
    public DocumentTemplate[] toGenerate() {
        return new DocumentTemplate[]{new SupervisorFeedback(),
                new ReviewerFeedback(), new IndividualTask(),
                new PracticeReport(), new PracticeFeedback()};
    }
}


final class Master implements Degree {

    /**
     * Degree name, used inside query
     */
    @Override
    public String name() {
        return "Магистратура";
    }

    /**
     * Subdirectory where to search for tempates / where to dump generated docs
     */
    @Override
    public String dir() {
        return "masters/2nd_course";
    }

    /**
     * Which templates should be generated
     */
    @Override
    public DocumentTemplate[] toGenerate() {

/*
по логике здесь нужно проверять профиль обучения у магистранта и формировать пакет доков для конкретного направления, но строки из
ReplacementCreator в функции replacement не видны а именно: */
        /* тут суть какая: например, у Дементьевой указан профиль - Моделирование и анализ данных, а доки у нее генерируются для ТРПО
        если делать доки и для тпрс и для мад, то у всех по 10 документов вместо 5 */

        /* can't genarate docs for define profile */

  /*    if(solution.get("профиль").asLiteral().getString().equals("Технология разработки программных систем")){
            new IndividualTaskTRPS();
            System.out.println(solution.get("фио_студента") + " " +  solution.get("профиль"));

        } else if(solution.get("профиль").asLiteral().getString().equals("Компьютерное моделирование и анализ данных")){
            new IndividualTaskMDA();
            System.out.println(solution.get("фио_студента") + " " +  solution.get("профиль"));
        }

 */
        return new DocumentTemplate[]{
                new SupervisorFeedbackTRPS(),
                new ReviewerFeedbackTRPS(), new IndividualTaskTRPS(),
                new PracticeReportTRPS(), new PracticeFeedbackTRPS()};

//        return new DocumentTemplate[]{new SupervisorFeedbackMDA(),
//                new ReviewerFeedbackMDA(), new IndividualTaskMDA(),
//                new PracticeReportMDA(), new PracticeFeedbackMDA()};
    }
}
