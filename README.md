**Техническое задание**  

Необходимо реализовать сервис для социальной сети.

**Основные функции сервиса:**

* регистрация нового пользователя;
* авторизация существующего пользователя;
* пользователь может искать других пользователей;
* пользователь может добавлять других пользователей в «друзья»;
* пользователь может удалять других пользователей из «друзей»;
* пользователь может получить список своих друзей;

**Технологический стек:**

* java 11+;
* база данных для хранения данных пользователей(любая на выбор);
* UI реализовывать не требуется (можно использовать swagger);
* unit-tests (опционально);*

**Критерии завершения:**

* Приложение написано и загружено в гит-репозиторий на проверку.
* Необходимо также подготовить инструкцию по запуску (README file).
* Опционально можно развернуть приложение на облачном сервисе (не обязательно).*

**Как пользоваться приложением**

В качестве базы данных я использовал H2, то есть ничего устанавливать не нужно для запуска приложения, достаточно просто открыть ссылку на репозиторий в среде программирования (я делал в IntelliJ IDEA CE), загрузить и запустит проект. При запуске класс S7TaskApplication добавит в БД четыре тестовых пользователя. У авторизованых пользователей есть две роли: **ROLE_ADMIN** и **ROLE_USER**, а так же для всех неавторизованных пользователей доступна **регистрация** и **авторизация**.

Список тестовых пользователей:
> username: "KanyeWest"  
> password: "god"  
> role: ROLE_ADMIN  
>
> username: "WillSmith"  
> password: "1234"  
> role: ROLE_USER  
>
> username: "JohnSnow"  
> password: "1234"  
> role: ROLE_USER  
>
> username: "JohnTravolta"  
> password: "vincent"  
> role: ROLE_USER

Для работы с приложением нужен любой **HTTP-клиент** для работы с **REST API**. Я использовал **Postman**.

# REST API

## Регистрация: ALL_USERS

Чтобы зарегестрироваться в приложении, нужно отправить на адрес   
`localhost:8092/api`  
**POST** запрос с телом (username и password должны состоять как минимум из 3 символов и 
содержать только цифры и латинские буквы):

>{  
&emsp;&emsp;"username": "username",  
&emsp;&emsp;"password": "password"  
}

## Авторизация: ALL_USERS

Чтобы авторизоваться в приложении, нужно отправить на адрес   
`localhost:8092/api/login`  
**POST** запрос с телом:

>{  
&emsp;&emsp;"username": "username",  
&emsp;&emsp;"password": "password"  
}

В ответ придёт **access token** в формате:  

>{  
&emsp;&emsp;"access_token": "access_token"  
}

в последующих запросах этот самый **access token** следует отправлять в **headers** в формате:  
>Authorization:access_token

## Получение списка имён всех пользователей: ROLE_USER  

Чтобы получить список имён всех пользователей приложения, нужно отправить на адрес   
`localhost:8092/api/users`  
**GET** запрос с **access token** пользователя, добавленным в **headers**, как показано выше. 
Если всё успешно, то вернётся JSON файл со списком тестовых пользователей.

>[  
> &emsp;&emsp;"KanyeWest",  
> &emsp;&emsp;"WillSmith",  
> &emsp;&emsp;"JohnSnow",  
> &emsp;&emsp;"JohnTravolta"  
> ]

## Получение списка имён пользователей по строке: ROLE_USER  

Чтобы получить список имён всех пользователей приложения, содержащих определённую строку, нужно отправить на адрес   
`localhost:8092/api/users/yourString`, где yourString - ваша строка, **GET** запрос с **access token** пользователя, 
добавленным в headers. Например запрос `localhost:8092/api/users/john` выдаст:  
>[  
> &emsp;&emsp;"JohnSnow",   
> &emsp;&emsp;"JohnTravolta"  
> ]

## Добавление пользователя в друзья: ROLE_USER  

Чтобы добавить пользователя в друзья, нужно отправить **PUT** запрос с **access token** в **headers** по адресу  
`localhost:8092/api/user/friends/friendUsername`, где `friendUsername` - это `username` пользователя, которого нужно добавить в друзья.
Если ответ сервера `true` - то друг добавлен в список, `false` - не добавлен.

## Удаление пользователя из друзей: ROLE_USER  

Чтобы удалить пользователя из друзей, нужно отправить **DELETE** запрос с **access token** в **headers** по адресу  
`localhost:8092/api/user/friends/notFriendAnymoreUsername`, где `notFriendAnymoreUsername` - это `username` пользователя, которого нужно удалить из друзей.
Если ответ сервера `true` - то друг добавлен в список, `false` - не добавлен.

## Получение списка своих друзей: ROLE_USER  

Чтобы получить список друзей пользователя, нужно отправить **GET** запрос с **access token** в **headers** по адресу  
`localhost:8092/api/user/friends`.

## Получение списка пользователей со всей информацией: ROLE_ADMIN  

Чтобы получить список всех пользователей, нужно отправить **GET** запрос с его **access token** в **headers** по адресу  
`localhost:8092/api/admin/users` и, соответственно, иметь ROLE_ADMIN.

## Редактирование имени и пароля пользователя: ROLE_ADMIN  

Чтобы редактировать имя или/и пароль пользователя, нужно отправить **PUT** запрос **access token** в **headers** по адресу  
`localhost:8092/api/admin/user/{id}`, где `id` - это `id` пользователя, которого надо поменять, и телом:

>{  
&emsp;&emsp;"username": "newUsername",  
&emsp;&emsp;"password": "newPassword"  
}

## Удаление пользователя : ROLE_ADMIN  

Чтобы удалить пользователя, нужно отправить **DELETE** запрос **access token** в headers по адресу  
`localhost:8092/api/admin/user/{id}`, где `id` - это `id` пользователя, которого надо удалить:
