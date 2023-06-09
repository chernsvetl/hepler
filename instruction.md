# Для того, чтобы сгенерировать данные о студентах в Protege, необходимо выполнить следующие шаги
### 1. Установка и использование Protege
 1.1. Для того, чтобы скачать Protege, нужно перейти по ссылке: _https://protege.stanford.edu/products.php_
 
 1.2. После успешной установки, необходимо заменить в папке плагина cellfie для Protege .jar файл на тот, что расположен в проекте (cellfie/cellfie-2.1.1.jar)


_**Для подробной информации приведены скриншоты ниже**_

1. Переходим в папку plugins

![img.png](img.png)

2. Подменяем выделенный файл

![img_1.png](img_1.png) 

3. На тот, что расположен в проекте

![img_2.png](img_2.png)

_Данное действие было сделано с целью использования символов кавычек и скобок при импортировании таблиц Excel._
 
1.3. Запускаем Protege и работаем с онтологиями

### 2. Открыть файл _rules.owl_ в Protege
_Данный файл расположен в корне проекта._
### 3. Загрузить таблицу с данными о научных руководителях 
_(см. пункт **"Таблицы и правила"**)._
1. Во вкладке Tools, расположенной в верхнем меню, выбрать пункт _**Create axioms from Excel workbook**_ _(см. скриншот)_

   ![img.png](src/main/resources/img.png)

2. Выбираем нужную таблицу и нажимаем _**Open**_ _(см. скриншот)_

   ![img_5.png](src/main/resources/img_5.png)

3. Выбираем _**Load Rules**_ и загружаем правила для этой таблицы, после нажимаем _**Open**_ _(см. скриншот)_
   
   ![img_6.png](src/main/resources/img_6.png)

4. После того как загрузили правила, нужно сгенерировать аксиомы 

Для этого нажимаем _**Generate Axioms**_ _(см. скриншот)._

   ![img_7.png](src/main/resources/img_7.png)

5. Если ошибок при генерации нет, появится окошко _(см. скриншот)_, где нужно выбрать _**Add to current onlotogy**_
   
   ![img_8.png](src/main/resources/img_8.png)

### 4. Повторяем действия из пункта 3 для второй таблицы
### 5. Сохраняем файл в формате rdf и помещаем его в корень проекта с именем _main_students_all.owl_


## Таблицы и правила

 1) Сначала выгрузите таблицу Студенты 2022-2023 уч.год.xlsx и загрузите правила, согласно описанию ниже.
 2) После - Студенты 2022-2023 уч.год_2.xlsx и правила для нее.


Файлы, описанные в таблице, расположены по пути _**src/main/resources/rules**_.

| _Наименование таблицы_           | _Файлы с правилами_                                                                                 |
|----------------------------------|-----------------------------------------------------------------------------------------------------|
| Студенты 2022-2023 уч.год.xlsx   | students_rules_main.json - для бакалавров <br/> students_rules_masters_main.json - для магистрантов             |
| Студенты 2022-2023 уч.год_2.xlsx | koi_rules_main.json - для бакалавров<br/> koi_rules_masters_main.json - для магистрантов|


### 6. Генерация пакета документов  
На интерфейсе нужно выбрать документы, которые хотите заполнить (_*см.скриншот*_)
![img_3.png](img_3.png)
   



 