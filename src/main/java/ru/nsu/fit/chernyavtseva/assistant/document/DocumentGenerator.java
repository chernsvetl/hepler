package ru.nsu.fit.chernyavtseva.assistant.document;

import com.hp.hpl.jena.query.QuerySolution;
import org.apache.poi.xwpf.usermodel.*;
import ru.nsu.fit.chernyavtseva.assistant.Degree;
import ru.nsu.fit.chernyavtseva.assistant.document.type.DocumentTemplate;
import ru.nsu.fit.chernyavtseva.assistant.document.type.ReplacementCreator;
import ru.nsu.fit.chernyavtseva.assistant.util.PathUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class DocumentGenerator {

    private static final String TEMPLATES_DIR = "/templates";
    private static final String DOCS_DIR = "documents";

    public static void generate(Degree degree, Iterator<QuerySolution> solutions) {
        Path outDirPath = PathUtil.newResourceDir(DOCS_DIR, degree.dir());

        while (solutions.hasNext()) {
            QuerySolution solution = solutions.next();
            for (DocumentTemplate template : degree.toGenerate()) {
                Path inPath = PathUtil.resource(TEMPLATES_DIR, degree.dir(), template.fileName());
                generateOne(inPath, outDirPath, template, solution);
            }
        }
    }

    private static void generateOne(Path inPath, Path outDirPath, DocumentTemplate toGenerate, QuerySolution s) {
        Map<String, String> studentReplacements = studentReplacements(toGenerate.replacements(), s);
        try (XWPFDocument doc = new XWPFDocument(Files.newInputStream(inPath))) {
            // replace in paragraphs
            for (XWPFParagraph xwpfParagraph : doc.getParagraphs()) {
                for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
                    replaceInRun(xwpfRun, studentReplacements);
                }
            }

            // replace in tables
            for (XWPFTable tbl : doc.getTables()) {
                for (XWPFTableRow row : tbl.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            for (XWPFRun r : p.getRuns()) {
                                replaceInRun(r, studentReplacements);
                            }
                        }
                    }
                }
            }

            String prefix = s.getLiteral(toGenerate.prefixVarName()).getString().replace(' ', '_');
            Path outPath = outDirPath.resolve(prefix + "_" + toGenerate.fileName());
            try (OutputStream out = Files.newOutputStream(outPath)) {
                doc.write(out);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Map<String, String> studentReplacements(Map<String, ReplacementCreator> solutionVarNames, QuerySolution s) {
        return solutionVarNames.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().replacement(s)
                ));
    }

    private static void replaceInRun(XWPFRun xwpfRun, Map<String, String> replacements) {
        int nChunks = xwpfRun.getCTR().getTArray().length;
        for (int i = 0; i < nChunks; i++) {
            String text = xwpfRun.getText(i);
            if (text == null) {
                continue;
            }
            xwpfRun.setText(replaced(text, replacements), i);
        }
    }

    private static String replaced(String original, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String toReplace = entry.getKey();
            String replaceWith = entry.getValue();
            if (replaceWith == null) {
                // TODO: default value or log
                continue;
            }
            original = original.replace(toReplace, replaceWith);
        }
        return original;
    }
}
