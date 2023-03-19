package ru.nsu.fit.chernyavtseva.assistant.document.type;

import com.github.petrovich4j.Case;
import com.github.petrovich4j.Gender;
import com.github.petrovich4j.NameType;
import com.github.petrovich4j.Petrovich;
import com.hp.hpl.jena.query.QuerySolution;

/**
 * Extracts data from solution and normalizes it if needed
 */
public sealed interface ReplacementCreator {

    static ReplacementCreator simple(String solutionVarName) {
        return new Simple(solutionVarName);
    }

    static ReplacementCreator fullName(String solutionVarName, Case wordCase) {
        return new FullNameReplacement(solutionVarName, wordCase);
    }

    String replacement(QuerySolution solution);
}

final class Simple implements ReplacementCreator {

    private final String varName;

    Simple(String varName) {
        this.varName = varName;
    }


    @Override
    public String replacement(QuerySolution solution) {
        return solution.getLiteral(varName).getString();
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
        String fullName = solution.getLiteral(solutionVarName).getString();
        String[] nameChunks = fullName.split(" ");

//        if (nameChunks.length != 3) {
//            // TODO: just log?
//            throw new IllegalArgumentException("No full name in variable " + solutionVarName + "; value: " + fullName);
//        }


        if (nameChunks.length == 4) {
//            Petrovich petrovich = new Petrovich();
//            Gender gender = petrovich.gender(nameChunks[2], Gender.Both);
//            String lastName = petrovich.say(nameChunks[0], NameType.LastName, gender, wordCase);
//            String firstName = petrovich.say(nameChunks[1], NameType.FirstName, gender, wordCase);
//            String patronymicName = petrovich.say(nameChunks[2], NameType.PatronymicName, gender, wordCase);
//            return firstName + lastName + patronymicName + nameChunks[3];
            return nameChunks[0] + 'а' + " " + nameChunks[1] + 'а' + " " + nameChunks[2] + " " + nameChunks[3];
        }
//            String result = Arrays.toString(Arrays.toString(nameChunks).toCharArray())
//                    .replace("[", "")
//                    .replace(",", "")
//                    .replace(" ", "")
//                    .replace("]", "");

        else {
            Petrovich petrovich = new Petrovich();
            Gender gender = petrovich.gender(nameChunks[2], Gender.Both);
            String lastName = petrovich.say(nameChunks[0], NameType.LastName, gender, wordCase);
            String firstName = petrovich.say(nameChunks[1], NameType.FirstName, gender, wordCase);
            String patronymicName = petrovich.say(nameChunks[2], NameType.PatronymicName, gender, wordCase);
            return String.format(lastName + " " + firstName + " " + patronymicName);
        }
    }
}