**Цель**
Получить практический опыт применения паттерна ```java Clock``` при реализации тестов.

**Задание**
Необходимо реализовать интерфейс ```java EventsStatistic```, который считает события, происходящие в системе.
Реализация должна хранить статистику ровно за последний час и подсчитывать, сколько событий каждого типа произошло в минуту.
Интерфейс ```java EventsStatistic```:
```java
public interface EventsStatistic {
    void incEvent(String name);
    ... getEventStatisticByName(String name);
    ... getAllEventStatistic();
    void printStatistic();
}
```
Описание методов:
* ```java incEvent(String name)``` -- инкрементит число событий name;
* ```java getEventStatisticByName(String name)``` -- выдает rpm (request per minute) события name за последний час;
* ```java getAllEventStatistic()``` -- выдает rpm всех произошедших событий за прошедший час;
* ```java printStatistic()``` -- выводит в консоль rpm всех произошедших событий;
Реализацию ```java EventsStatistic``` необходимо покрыть тестами, используя паттерн ```java Clock```, рассмотренный на лекции.
Тесты не должны использовать sleep'ы и должны выполняться быстро.
