package normative_control.validation.service.utils;

import java.util.regex.Pattern;

public class Constants {
    // Константы для проверки форматирования
    public static final double EXPECTED_LINE_SPACING = 1.0;
    public static final double EXPECTED_INDENTATION_CM = 1.25;
    public static final int CM_TO_TWIPS = 567;
    public static final int LINE_TO_TWIPS = 240;

    // Регулярное выражение для определения строки с городом и годом
    public static final Pattern CITY_YEAR_PATTERN = Pattern.compile(".*(Новосибирск|Москва|Санкт-Петербург|Екатеринбург|Казань|Нижний Новгород|Красноярск|Челябинск|Омск|Самара|Ростов-на-Дону|Уфа|Воронеж|Пермь|Волгоград|Краснодар|Саратов|Тюмень|Томск|Иркутск|Хабаровск|Ярославль|Кемерово|Барнаул|Новокузнецк|Рязань|Астрахань|Пенза|Липецк|Киров|Тула|Чебоксары|Калининград|Брянск|Иваново|Магнитогорск|Курск|Тверь|Ставрополь|Ульяновск)[\\s]*[-\\.]?[\\s]*(19|20)\\d{2}.*");

    // Начало титульника
    public static final String TITLE_START = "МИНИСТЕРСТВО НАУКИ И ВЫСШЕГО ОБРАЗОВАНИЯ";
}
