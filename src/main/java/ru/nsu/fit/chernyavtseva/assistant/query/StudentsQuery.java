package ru.nsu.fit.chernyavtseva.assistant.query;

public record StudentsQuery(String degree) {

    /*
    for generating docs for specific degree you need to exchange response in line 22 on response bellow and in Degree.java choose needed array
            FILTER regex(?профиль, "Компьютерное моделирование и анализ данных")
            FILTER regex(?профиль, "Программная инженерия и компьютерные науки")
            FILTER regex(?профиль, "Технология разработки программных систем")
     */

    public static final String QUERY_TEMPLATE = """
            PREFIX my: <http://www.semanticweb.org/oleyn/ontologies/2022/4/кафедра#>
            SELECT * WHERE {
            ?студент a my:$degree .
            ?студент my:защищает ?вкр .
            ?студент my:ФИО ?фио_студента .
            ?студент my:группа ?группа_студента .
            ?студент my:Место_прохождения_практики ?место_практики .
            ?студент my:Приказ_на_прохождение_практики ?приказ_практика .
            ?студент my:Профиль_обучения ?профиль .
            FILTER regex(?профиль, "Технология разработки программных систем")

                              
            ?студент my:на_НГУ_практике_у ?руководитель_от_НГУ .
            ?руководитель_от_НГУ my:ФИО ?фио_НГУ_руководителя .
            ?руководитель_от_НГУ my:Должность_в_НГУ ?должность_НГУ_руководителя .
                        
            ?студент my:на_орг_практике_у ?руководитель_от_организации .
            ?руководитель_от_организации my:ФИО ?фио_орг_руководителя .
            ?руководитель_от_организации my:Должность_в_организации ?должность_орг_руководителя .
                        
            ?руководитель my:согласовывает ?вкр .
            ?руководитель my:ФИО ?фио_руководителя .
            ?руководитель my:Должность ?должность_руководителя .
                        
            ?вкр my:Тема ?тема_вкр .
                        
                        
            }
            """;


    String create() {
        return QUERY_TEMPLATE.replace("$degree", degree);
    }
}
