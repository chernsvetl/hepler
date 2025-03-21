package normative_control.utils;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

public class TextSimilarity {

    public static boolean areTextsSimilar(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return false;
        }

        JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
        double similarityScore = similarity.apply(text1, text2);

        return similarityScore >= 0.8;
    }
}

