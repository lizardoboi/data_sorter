Java: 
17
Система сборки: 
Apache Maven 3.8.1
Сторонние библиотеки: 
Нет

Убедитесь, что у вас установлен JDK 17 и Maven.

Клонируйте или распакуйте проект.

Перейдите в корень проекта и выполните команду:

--- mvn clean package

В результате в папке target будет создан исполняемый JAR-файл:

--- data_sorter_util.jar

Для запуска утилиты введите команду:

java -jar target/data_sorter_util.jar [опции] <входные_файлы>

Опции:
-o <путь>
Путь к директории, в которую будут записаны выходные файлы. По умолчанию - текущая директория (.).

-p <префикс>	
Префикс для имён выходных файлов. Например, при -p result_ создаются файлы result_integers.txt, result_floats.txt, и т.д.

-a	
Включает режим добавления в существующие выходные файлы. Без этой опции файлы перезаписываются.

-s	
Вывод краткой статистики по каждому типу данных: количество элементов.

-f	
Вывод полной статистики по каждому типу данных:
      для чисел - min, max, сумма, среднее;
      для строк - длина самой короткой и самой длинной строки.

-<файл1> <файл2> ...Один или несколько входных файлов, содержащих данные для обработки.

Пример:
java -jar data_sorter_util.jar -f -a -p output_ -o ./output input1.txt input2.txt
