package ru.nsu.fit.chernyavtseva.assistant;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.DocumentGenerator;
import ru.nsu.fit.chernyavtseva.assistant.complete_documents.DocumentTemplateFilter;
import ru.nsu.fit.chernyavtseva.assistant.query_model.QueryExecutor;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    private static final String MODEL_FILENAME = "/main_students_all.owl";
    private static final Degree[] DEGREES = Degree.all();

    public static void main(String[] args) throws IOException {
        generateTemplates(DocumentTemplateFilter.EMPTY);
    }

    public static void generateTemplates(DocumentTemplateFilter documentTemplateFilter) throws IOException {
        Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        try (InputStream in = Main.class.getResourceAsStream(MODEL_FILENAME)) {
            model.read(in, "RDF/XML");
        }

        QueryExecutor executor = new QueryExecutor(model);
        for (Degree degree : DEGREES) {
            executor.findStudents(degree.name(), degree.profile(),
                    solution -> DocumentGenerator.generate(degree, solution, documentTemplateFilter));
        }
    }
}
