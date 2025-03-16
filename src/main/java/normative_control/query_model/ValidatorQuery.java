package normative_control.query_model;

public record ValidatorQuery() {
    public static final String QUERY_REPORT = """
            PREFIX my: <http://www.semanticweb.org/oleyn/ontologies/2022/4/кафедра#>
            SELECT * WHERE {
            ?Отчет_ПИиКН_8 my:Минимальное_количество_страниц_отчета ?Минимальное_количество_страниц_отчета .     
            ?Отчет_ПИиКН_8 my:Стиль ?Стиль .
            ?Отчет_ПИиКН_8 my:Размер ?Размер .                                 
            }""";
}
