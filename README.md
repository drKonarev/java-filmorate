<h1 align="center">Filmorate app</h1>

<h2 align="center">
  
![scale_1200](https://free-png.ru/wp-content/uploads/2021/12/free-png.ru-170-340x227.png)
  
</h2>

## Описание
Этот проект представляет собой учебный аналог известного сервиса "Кинопоиск".

<h1 align="center">Filmorate ER-diagram</h1>
<h2 align="center">

![Filmorate scheme](https://github.com/drKonarev/java-filmorate/blob/main/Filmorate%20scheme.jpg)

</h2>




## Примеры команд на языке SQL
### Запрос на получение списка всех фильмов:

```sql
SELECT name
FROM film;
```
__________________________________________
### Запрос на получение конкретного фильма:

```sql
SELECT name
FROM film
WHERE film_id=2;
```
____________________________________________________
### Запрос на получение списка фильмов конкретного рейтинга:

```sql
SELECT name
FROM film AS f
INNER JOIN mpa
ON mpa.mpa_id=f.mpa_id
WHERE mpa.name='R17';
```
