# GreenDataTest

### Описание
Приложение позволяет, поднять в контейнере базу данных на postgres, а затем взаимодейстовать с такими сущностями как:
- Клиенты
- Банки
- Депозиты

### Запуск
К сожалению, обернуть все приложение в Docker-контейнер не получилось (не успел). Потому запуск осуществляется следующим образом:
- Запуск базы данных: терминал, главная папка проекта, "docker-compose up"  в терминале
- Запуск апи: запускаем файл по адресу "src/main/java/com/example/testtask/TesttaskApplication.java" и запускаем его
- Готово, теперь можно отправлять команды (например через Postman)

### Клиенты
Пользователь может создавать, редактировать и удалять клиентов, а также выводить их список с сортировкой и/или фильтрацией (или без них).

- **Добавление клиента**
  
  Вызов: **post** http://localhost:8080/api/v1/client
  
  raw: {"name":"test56", "shortName":"test56", "address":"test56", "form":"C"}
  - name - ненулевое, не пустое, строка от 5 до 255 символов
  - short_name - ненулевое, не пустое, строка от 5 до 15 символов
  - address - ненулевое, не пустое, строка от 5 до 50 символов
  - form - ненулевое, строка, может быть только одним из вариантов ("A", "B", "C")  

- **Обновление клиента**
  
  Вызов: **put** http://localhost:8080/api/v1/client/1 (id клиента)
  
  raw: {"name":"test56", "shortName":"test56", "address":"test56", "form":"C"}
  - name - ненулевое, не пустое, строка от 5 до 255 символов
  - shortName - ненулевое, не пустое, строка от 5 до 15 символов
  - address - ненулевое, не пустое, строка от 5 до 50 символов
  - form - ненулевое, строка, может быть только одним из вариантов ("A", "B", "C")

- **Удаление клиента**
  
  Вызов: **delete** http://localhost:8080/api/v1/client/1 (id клиента)

- **Вывод списка клиентов**

  Вызов: **get** http://localhost:8080/api/v1/client
  
  Вызов (сортировка + фильтрация): **get** http://localhost:8080/api/v1/client?sort_by=form&filter_by=name&filter_value=test
  - Параметр sort_by - название столбца по котором сортируем
  - Параметр filter_by - название столбца по котором фильтруем
  - Параметр filter_value - значение по которому фильтруем

### Банки
Пользователь может создавать, редактировать и удалять банки, а также выводить их список с сортировкой и/или фильтрацией (или без них).

- **Добавление банка**
  
  Вызов: **post** http://localhost:8080/api/v1/bank
  
  raw: {"name":"asdsdsd", "bankIdentificationCode":12312321}
  - name - ненулевое, не пустое, строка от 5 до 255 символов
  - bankIdentificationCode - ненулевое, Long 

- **Обновление банка**
  
  Вызов: **put** http://localhost:8080/api/v1/bank/1 (id банка)
  
  raw: {"name":"asdsdsd", "bankIdentificationCode":12312321}
  - name - ненулевое, не пустое, строка от 5 до 255 символов
  - bankIdentificationCode - ненулевое, Long 

- **Удаление банка**
  
  Вызов: **delete** http://localhost:8080/api/v1/bank/1 (id банка)

- **Вывод списка банков**

  Вызов: **get** http://localhost:8080/api/v1/bank
  
  Вызов (сортировка + фильтрация): **get** http://localhost:8080/api/v1/bank?sort_by=name&filter_by=bic&filter_value=12312321
  - Параметр sort_by - название столбца по котором сортируем
  - Параметр filter_by - название столбца по котором фильтруем
  - Параметр filter_value - значение по которому фильтруем

### Депозиты
Пользователь может создавать, редактировать и удалять депозиты, а также выводить их список с сортировкой и/или фильтрацией (или без них).

- **Добавление депозита**
  
  Вызов: **post** http://localhost:8080/api/v1/deposit?client_id=1&bank_id=1
  
  raw: {"percent": 1, "dateOpen": "2024-28-09", "monthPeriod": 1}
  - percent - ненулевое, int от 1 до 50
  - dateOpen - ненулевое, Date
  - monthPeriod - ненулевое, int от 1 до 20
  - Параметр client_id - id привязанного клиента
  - Параметр bank_id - id привязанного банка

- **Обновление депозита**
  
  Вызов: **put** http://localhost:8080/api/v1/deposit/1?client_id=1&bank_id=1 (id депозита)
  
  raw: {"name":"test56", "shortName":"test56", "address":"test56", "form":"C"}
  - percent - ненулевое, int от 1 до 50
  - dateOpen - ненулевое, Date
  - monthPeriod - ненулевое, int от 1 до 20
  - Параметр client_id - id привязанного клиента
  - Параметр bank_id - id привязанного банка

- **Удаление депозита**
  
  Вызов: **delete** http://localhost:8080/api/v1/deposit/1 (id депозита)

- **Вывод списка депозитов**

  Вызов: **get** http://localhost:8080/api/v1/deposit
  
  Вызов (сортировка + фильтрация): **get** http://localhost:8080/api/v1/deposit?sort_by=dateOpen&filter_by=monthPeriod&filter_value=1
  - Параметр sort_by - название столбца по котором сортируем
  - Параметр filter_by - название столбца по котором фильтруем
  - Параметр filter_value - значение по которому фильтруем

### Тесты
Тесты находятся по адресу "src/test/java/com/example/testtask"

Имеются тесты для каждого типа таблиц.

ВАЖНО! Не забудьте указать перед запуском корректные данные (Id таблиц и т.д.), вынесенные в отдельные переменные.
