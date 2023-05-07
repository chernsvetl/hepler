package ru.nsu.fit.chernyavtseva.assistant.query_model;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;

import java.util.Iterator;
import java.util.function.Consumer;

public class QueryExecutor {

    private final Model model;

    public QueryExecutor(Model model) {
        this.model = model;
    }

    public void findStudents(String degree, String profile, Consumer<Iterator<QuerySolution>> consumer) {
        String queryText = new StudentsQuery(degree, profile).create();
        Query query = QueryFactory.create(queryText);
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            consumer.accept(qe.execSelect());
        }
    }
}
