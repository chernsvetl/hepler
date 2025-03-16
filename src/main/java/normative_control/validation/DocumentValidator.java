package normative_control.validation;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import ru.nsu.fit.chernyavtseva.assistant.Main;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DocumentValidator {
    private static final String MODEL_FILENAME = "/normocontrol.owl";
    public static ValidatorData extractFromSparql(String sparqlQuery) {
        try {
            Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            try (InputStream in = Main.class.getResourceAsStream(MODEL_FILENAME)) {
                model.read(in, "RDF/XML");
            }
            Query query = QueryFactory.create(sparqlQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            ResultSet results = qexec.execSelect();
            int minPages = 0;
            String font = null;
            String size = null;
            String style = null;
            while(results.hasNext()){
                QuerySolution solution = results.next();
                minPages = solution.getLiteral("Минимальное_количество_страниц_отчета").getInt();
                size = solution.getLiteral("Размер").getString();
                style = solution.getLiteral("Стиль").getString();
            }
            qexec.close();

            return new ValidatorData(minPages, font, size, style);
        } catch (Exception e) {
            System.err.println("Ошибка выполнения SPARQL запроса: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static void validateDocxFiles(String directoryPath, ValidatorData data) {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            System.err.println("Указанный путь не является директорией.");
            return;
        }
        for (File file : dir.listFiles((d, name) -> name.toLowerCase().endsWith(".docx"))) {
            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {

                int pageCount = document.getParagraphs().size() / 30;
                String fontSize = getFontSize(document);
                String fontStyle = getFontStyle(document);

                boolean valid = true;
                StringBuilder errorMessage = new StringBuilder();

                if (pageCount < data.minPages) {
                    valid = false;
                    errorMessage.append("Недостаточно страниц (ожидалось: ").append(data.minPages).append(", есть: ").append(pageCount).append("). ");
                }
                if (!fontSize.equals(data.size)) {
                    valid = false;
                    errorMessage.append("Неверный размер шрифта (ожидалось: ").append(data.size).append(", есть: ").append(fontSize).append("). ");
                }
                if (!fontStyle.equals(data.style)) {
                    valid = false;
                    errorMessage.append("Неверный стиль (ожидалось: ").append(data.style).append(", есть: ").append(fontStyle).append("). ");
                }
                if (valid) {
                    System.out.println("Документ " + file.getName() + " корректен.");
                } else {
                    System.err.println("Документ " + file.getName() + " не корректен: " + errorMessage);
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла " + file.getName() + ": " + e.getMessage());
            }
        }
    }
    private static String getFontStyle(XWPFDocument document) {
        Map<String, Integer> fontCounts = new HashMap<>();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String font = run.getFontFamily();
                fontCounts.put(font, fontCounts.getOrDefault(font, 0) + 1);
            }
        }
        String mostPopularFont = fontCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Стиль документа отсутствует, так как документ пуст");

        return mostPopularFont;
    }
    private static String getFontSize(XWPFDocument document) {
        java.util.Map<Integer, Integer> sizeCounts = new java.util.HashMap<>();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                int size = run.getFontSize();
                sizeCounts.put(size, sizeCounts.getOrDefault(size, 0) + 1);
            }
        }
        return sizeCounts.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .map(String::valueOf)
                .orElse("Размер документа отсутствует, так как документ пуст");
    }

    public static class ValidatorData {
        public int minPages;
        public String font;
        public String size;
        public String style;

        public ValidatorData(int minPages, String font, String size, String style) {
            this.minPages = minPages;
            this.font = font;
            this.size = size;
            this.style = style;
        }
    }
}