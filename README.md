# Mailing system REST API
REST API для отслеживания почтовых отправлений  
автор Михаил Болотов  

Задача:  
В системе должны регистрироваться почтовые отправления - письма, посылки - их передвижение между почтовыми отделениями, а также должна быть реализована возможность получения информации и всей истории передвижения конкретного почтового отправления.

Пояснения к решению:  
Сервис реализован в виде JSON при помощи Spring. Работа с базой через Spring Data JPA с Hibernate. Для сборки используется Maven.  
В качестве СУБД используется PostgreSQL, для работы сервиса должен быть на localhost:5432 с созданной базой MailingDb, либо изменить настройки в application.properties  

Для запуска и проверки можно из Intellij IDEA просто запустить метод StartApp.run(), сервис будет развернут на встроенном Tomcat сервере (http://localhost:8080).
Также можно собрать war архив при помощи Maven и развернуть на другом сервере приложений.  
После запуска доступность сервиса можно проверить браузером зайдя на адрес /mailing должен вернуться пустой JSON массив []  
При первом запуске нужно инициализировать список/справочник почтовых отделений, для этого браузером зайти на адрес /mailing/initialize будут созданы отделения с индексами '344000', '344010', '344020', '344030', '344040'  
В дальнейшем их можно использовать для проверки других методов REST API.

Методы контроллера:  
/mailing/create  POST  
/mailing/send    PUT  
/mailing/accept  PUT  
/mailing/deliver PUT  
/mailing/history GET  
/mailing GET - список всех отправлений  
/mailing/initialize GET - в пустой базе создает и выводит список почтовых отделений  

Создание / регистрация нового отправления /mailing/create POST  
Для проверки API можно запускать JS скрипты в консоли браузера Chrome:  
fetch('/mailing/create', { method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({ mailingType: "LETTER", startOfficeZip: "344000",
 recipientOfficeZip: "344010", recipientAddress: "Ростов-на-Дону", recipientName: "Василий Петрович" })}).then(console.log)    
либо отправить структуру вида:  
{  
  "mailingType": "LETTER",  
  "startOfficeZip": "344000",  
  "recipientOfficeZip": "344010",  
  "recipientAddress": "Ростов-на-Дону",  
  "recipientName": "Василий Петрович"  
}  
допустимые mailingType: LETTER, PACKAGE, BANDEROLE, POSTCARD (письмо, посылка, бандероль, открытка)  
В ответе возвращается структура созданного отправления:  
{  
  "id": 10,  
  "mailingType": "LETTER",  
  "startOffice": {  
    "id": 1,  
    "zipCode": "344000",  
    "name": "Центральное отделение",  
    "address": "Ростов-на-Дону"  
  },  
  "recipientOffice": {  
    "id": 2,  
    "zipCode": "344010",  
    "name": "10-е отделение",  
    "address": "Ворошиловский пр-кт, Ростов-на-Дону"  
  },  
  "recipientAddress": "Ростов-на-Дону",  
  "recipientName": "Василий Петрович"  
}  

Убытие из отделения  /mailing/send    PUT  
Прибытие в отделение /mailing/accept  PUT  
Выдача адресату      /mailing/deliver PUT  
JS скрипт:  
fetch('/mailing/send', { method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({ mailingId: 10, currentOfficeZip: "344000",
 nextOfficeZip: "344010" })}).then(console.log)  
либо отправить структуру вида (параметр nextOfficeZip обязателен только для операции "убытия" /mailing/send)  
{  
  "mailingId": 10,  
  "currentOfficeZip": "344000",  
  "nextOfficeZip": "344010"  
}  
В ответе возвращается структура созданной записи передвижения отправления:  
{  
  "id": 12,  
  "postOffice": {  
    "id": 1,  
    "zipCode": "344000",  
    "name": "Центральное отделение",  
    "address": "Ростов-на-Дону"  
  },  
  "nextOffice": {  
    "id": 2,  
    "zipCode": "344010",  
    "name": "10-е отделение",  
    "address": "Ворошиловский пр-кт, Ростов-на-Дону"  
  },  
  "operationDate": "2020-11-02T13:14:53.243",  
  "operation": "DEPARTURE"  
}  

Запрос истории /mailing/history GET  
например, /mailing/history?id=10  где id - идентификатора почтового отправления  
Ответ в виде структуры (отправление, его статус и записи истории передвижения):  
{  
  "mailing": {  
    "id": 10,  
    "mailingType": "LETTER",  
    "startOffice": {  
      "id": 1,  
      "zipCode": "344000",  
      "name": "Центральное отделение",  
      "address": "Ростов-на-Дону"  
    },  
    "recipientOffice": {  
      "id": 2,  
      "zipCode": "344010",  
      "name": "10-е отделение",  
      "address": "Ворошиловский пр-кт, Ростов-на-Дону"  
    },  
    "recipientAddress": "Ростов-на-Дону",  
    "recipientName": "Василий Петрович"  
  },  
  "status": "IN_TRANSIT",  
  "items": [  
    {  
      "id": 12,  
      "postOffice": {  
        "id": 1,  
        "zipCode": "344000",  
        "name": "Центральное отделение",  
        "address": "Ростов-на-Дону"  
      },  
      "nextOffice": {  
        "id": 2,  
        "zipCode": "344010",  
        "name": "10-е отделение",  
        "address": "Ворошиловский пр-кт, Ростов-на-Дону"  
      },  
      "operationDate": "2020-11-02T13:14:53.243",  
      "operation": "DEPARTURE"  
    },  
    {  
      "id": 11,  
      "postOffice": {  
        "id": 1,  
        "zipCode": "344000",  
        "name": "Центральное отделение",  
        "address": "Ростов-на-Дону"  
      },  
      "nextOffice": null,  
      "operationDate": "2020-11-02T12:55:12.239",  
      "operation": "REGISTRATION"  
    }  
  ]  
}  
Возможные операции передвижения отправлений REGISTRATION (регистрация), DEPARTURE (убытие), ARRIVAL (прибытие), DELIVERY (вручение)  
Возможные статусы почтового отправления NOT_FOUND (не найдено), CREATED (создано), IN_TRANSIT (в пути), READY_TO_ISSUE (готов к выдаче), DELIVERED(вручен)  
