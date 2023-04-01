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


        return new DocumentTemplate[]{
                new SupervisorFeedbackTRPS(),
                new ReviewerFeedbackTRPS(), new IndividualTaskTRPS(),
                new PracticeReportTRPS(), new PracticeFeedbackTRPS()};

//        return new DocumentTemplate[]{new SupervisorFeedbackMDA(),
//                new ReviewerFeedbackMDA(), new IndividualTaskMDA(),
//                new PracticeReportMDA(), new PracticeFeedbackMDA()};
    }
}
