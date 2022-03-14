**����**
�������� ������������ ���� ���������� �������� ```java Clock``` ��� ���������� ������.

**�������**
���������� ����������� ��������� ```java EventsStatistic```, ������� ������� �������, ������������ � �������.
���������� ������ ������� ���������� ����� �� ��������� ��� � ������������, ������� ������� ������� ���� ��������� � ������.
��������� ```java EventsStatistic```:
```java
public interface EventsStatistic {
    void incEvent(String name);
    ... getEventStatisticByName(String name);
    ... getAllEventStatistic();
    void printStatistic();
}
```
�������� �������:
* ```java incEvent(String name)``` -- ����������� ����� ������� name;
* ```java getEventStatisticByName(String name)``` -- ������ rpm (request per minute) ������� name �� ��������� ���;
* ```java getAllEventStatistic()``` -- ������ rpm ���� ������������ ������� �� ��������� ���;
* ```java printStatistic()``` -- ������� � ������� rpm ���� ������������ �������;
���������� ```java EventsStatistic``` ���������� ������� �������, ��������� ������� ```java Clock```, ������������� �� ������.
����� �� ������ ������������ sleep'� � ������ ����������� ������.
