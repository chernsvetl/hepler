package ru.nsu.fit.chernyavtseva.assistant.complete_documents.type;

import com.github.petrovich4j.Case;
import com.github.petrovich4j.Gender;
import com.github.petrovich4j.NameType;
import com.github.petrovich4j.Petrovich;
import com.hp.hpl.jena.query.QuerySolution;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Extracts data from solution and normalizes it if needed
 */
public sealed interface ReplacementCreator {

    static ReplacementCreator simple(String solutionVarName) {
        return new UriConverterReplacement(new Simple(solutionVarName));
    }

    static ReplacementCreator fullName(String solutionVarName, Case wordCase) {
        return new UriConverterReplacement(new FullNameReplacement(solutionVarName, wordCase));
    }

    static ReplacementCreator gender(String solutionVarName) {
        return new UriConverterReplacement(new GenderStudentReplacement(solutionVarName));

    }

    static ReplacementCreator genderFio(String solutionVarName) {
        return new UriConverterReplacement(new GenderStudentImReplacement(solutionVarName));

    }

    static ReplacementCreator genderForm(String solutionVarName) {
        return new UriConverterReplacement(new GenderFormStudentReplacement(solutionVarName));

    }

    static ReplacementCreator genderFormTwor(String solutionVarName) {
        return new UriConverterReplacement(new GenderStudentTworReplacement(solutionVarName));

    }

    static ReplacementCreator genderFormImStud(String solutionVarName) {
        return new UriConverterReplacement(new GenderStudentImStudReplacement(solutionVarName));

    }

    static ReplacementCreator genderFormDatStud(String solutionVarName) {
        return new UriConverterReplacement(new GenderDatFormStudentReplacement(solutionVarName));

    }

    String replacement(QuerySolution solution);
}

final class UriConverterReplacement implements ReplacementCreator {

    private static final char[] ENCODED_CHARS = {'(', ')', '"', '«', '»'};

    private final ReplacementCreator delegateTo;

    UriConverterReplacement(ReplacementCreator delegateTo) {
        this.delegateTo = delegateTo;
    }

    @Override
    public String replacement(QuerySolution solution) {
        String replacement = delegateTo.replacement(solution);
        if (replacement == null) {
            return replacement;
        }
        for (char c : ENCODED_CHARS) {
            String encoded = URLEncoder.encode(String.valueOf(c), StandardCharsets.UTF_8);
            replacement = replacement.replaceAll(encoded, String.valueOf(c));
        }
        return replacement;
    }

}

final class Simple implements ReplacementCreator {

    private final String varName;

    Simple(String varName) {
        this.varName = varName;
    }


    @Override
    public String replacement(QuerySolution solution) {
        if (solution.contains(varName)) {
            System.out.println();
            return solution.getLiteral(varName).getString();
        }
        return null;
    }

}

final class FullNameReplacement implements ReplacementCreator {

    private final String solutionVarName;
    private final Case wordCase;

    FullNameReplacement(String solutionVarName, Case wordCase) {
        this.solutionVarName = solutionVarName;
        this.wordCase = wordCase;
    }

    @Override
    public String replacement(QuerySolution solution) {
        if (!solution.contains(solutionVarName)) {
            return null;
        }
        String fullName = solution.getLiteral(solutionVarName).getString();
        String[] nameChunks = fullName.split(" ");
        if (nameChunks.length == 4) {
            return nameChunks[0] + 'а' + " " + nameChunks[1] + 'а' + " " + nameChunks[2] + " " + nameChunks[3];
        }
        else if (nameChunks.length == 2) {
            return nameChunks[0] + 'а' + " " + nameChunks[1] + 'а';
        }
        else if (nameChunks.length == 3) {
            Petrovich petrovich = new Petrovich();
            Gender gender = petrovich.gender(nameChunks[2], Gender.Both);
            String lastName = petrovich.say(nameChunks[0], NameType.LastName, gender, wordCase);
            String firstName = petrovich.say(nameChunks[1], NameType.FirstName, gender, wordCase);
            String patronymicName = petrovich.say(nameChunks[2], NameType.PatronymicName, gender, wordCase);
    // the best way because library does not support that case
            if (Objects.equals(nameChunks[0], "Кривошея")) {

                return String.format(lastName.replaceFirst("я","и") + " " + firstName + " " +patronymicName);
            } else
            return String.format(lastName + " " + firstName + " " + patronymicName);
        } else {
            throw new IllegalArgumentException("No full name in variable " + solutionVarName + "; value: " + fullName);
        }
    }
}

final class GenderStudentReplacement implements ReplacementCreator {

    private final String solutionVarName;

    GenderStudentReplacement(String solutionVarName) {
        this.solutionVarName = solutionVarName;
    }

    @Override
    public String replacement(QuerySolution solution) {
        if (!solution.contains(solutionVarName)) {
            return null;
        }
        String fullName = solution.getLiteral(solutionVarName).getString();
        String[] nameChunks = fullName.split(" ");
        if (nameChunks.length == 4) {
            return "Обучающегося";
        }
        if (nameChunks.length == 2) {
            return "Обучающегося";
        }
        else if (nameChunks.length == 3) {
            Petrovich petrovich = new Petrovich();
            Gender gender = petrovich.gender(nameChunks[2], Gender.Both);
            // the best way because library does not support that case
            switch (gender) {
                case Female -> {
                    return "Обучающейся";
                }
                default -> {
                    return "Обучающегося";
                }
            }
        } else {
            throw new IllegalArgumentException("No full name in variable " + solutionVarName + "; value: " + fullName);
        }
    }
}


final class GenderFormStudentReplacement implements ReplacementCreator {

    private final String solutionVarName;

    GenderFormStudentReplacement(String solutionVarName) {
        this.solutionVarName = solutionVarName;
    }

    @Override
    public String replacement(QuerySolution solution) {
        if (!solution.contains(solutionVarName)) {
            return null;
        }
        String fullName = solution.getLiteral(solutionVarName).getString();
        String[] nameChunks = fullName.split(" ");
        if (nameChunks.length == 4) {
            return "студенту";
        }
        if (nameChunks.length == 2) {
            return "студенту";
        }
        else if (nameChunks.length == 3) {
            Petrovich petrovich = new Petrovich();
            Gender gender = petrovich.gender(nameChunks[2], Gender.Both);
            // the best way because library does not support that case
            switch (gender) {
                case Female -> {
                    return "студентке";
                }
                default -> {
                    return "студенту";
                }
            }
        } else {
            throw new IllegalArgumentException("No full name in variable " + solutionVarName + "; value: " + fullName);
        }
    }
}

final class GenderStudentImReplacement implements ReplacementCreator {

    private final String solutionVarName;

    GenderStudentImReplacement(String solutionVarName) {
        this.solutionVarName = solutionVarName;
    }

    @Override
    public String replacement(QuerySolution solution) {
        if (!solution.contains(solutionVarName)) {
            return null;
        }
        String fullName = solution.getLiteral(solutionVarName).getString();
        String[] nameChunks = fullName.split(" ");
        if (nameChunks.length == 4) {
            return "Обучающийся";
        }
        if (nameChunks.length == 2) {
            return "Обучающийся";
        }
        else if (nameChunks.length == 3) {
            Petrovich petrovich = new Petrovich();
            Gender gender = petrovich.gender(nameChunks[2], Gender.Both);
            // the best way because library does not support that case
            switch (gender) {
                case Female -> {
                    return "Обучающаяся";
                }
                default -> {
                    return "Обучающийся";
                }
            }
        } else {
            throw new IllegalArgumentException("No full name in variable " + solutionVarName + "; value: " + fullName);
        }
    }
}


final class GenderStudentTworReplacement implements ReplacementCreator {

    private final String solutionVarName;

    GenderStudentTworReplacement(String solutionVarName) {
        this.solutionVarName = solutionVarName;
    }

    @Override
    public String replacement(QuerySolution solution) {
        if (!solution.contains(solutionVarName)) {
            return null;
        }
        String fullName = solution.getLiteral(solutionVarName).getString();
        String[] nameChunks = fullName.split(" ");
        if (nameChunks.length == 4) {
            return "студентом";
        }
        if (nameChunks.length == 2) {
            return "студентом";
        }
        else if (nameChunks.length == 3) {
            Petrovich petrovich = new Petrovich();
            Gender gender = petrovich.gender(nameChunks[2], Gender.Both);
            switch (gender) {
                case Female -> {
                    return "студенткой";
                }
                default -> {
                    return "студентом";
                }
            }
        } else {
            throw new IllegalArgumentException("No full name in variable " + solutionVarName + "; value: " + fullName);
        }
    }
}


final class GenderStudentImStudReplacement implements ReplacementCreator {

    private final String solutionVarName;

    GenderStudentImStudReplacement(String solutionVarName) {
        this.solutionVarName = solutionVarName;
    }

    @Override
    public String replacement(QuerySolution solution) {
        if (!solution.contains(solutionVarName)) {
            return null;
        }
        String fullName = solution.getLiteral(solutionVarName).getString();
        String[] nameChunks = fullName.split(" ");
        if (nameChunks.length == 4) {
            return "Студент";
        }
        if (nameChunks.length == 2) {
            return "Студент";
        }
        else if (nameChunks.length == 3) {
            Petrovich petrovich = new Petrovich();
            Gender gender = petrovich.gender(nameChunks[2], Gender.Both);
            switch (gender) {
                case Female -> {
                    return "Студентка";
                }
                default -> {
                    return "Студент";
                }
            }
        } else {
            throw new IllegalArgumentException("No full name in variable " + solutionVarName + "; value: " + fullName);
        }
    }
}



final class GenderDatFormStudentReplacement implements ReplacementCreator {

    private final String solutionVarName;

    GenderDatFormStudentReplacement(String solutionVarName) {
        this.solutionVarName = solutionVarName;
    }

    @Override
    public String replacement(QuerySolution solution) {
        if (!solution.contains(solutionVarName)) {
            return null;
        }
        String fullName = solution.getLiteral(solutionVarName).getString();
        String[] nameChunks = fullName.split(" ");
        if (nameChunks.length == 4) {
            return "Студенту";
        }
        if (nameChunks.length == 2) {
            return "Студенту";
        }
        else if (nameChunks.length == 3) {
            Petrovich petrovich = new Petrovich();
            Gender gender = petrovich.gender(nameChunks[2], Gender.Both);
            switch (gender) {
                case Female -> {
                    return "Студентке";
                }
                default -> {
                    return "Студенту";
                }
            }
        } else {
            throw new IllegalArgumentException("No full name in variable " + solutionVarName + "; value: " + fullName);
        }
    }
}

