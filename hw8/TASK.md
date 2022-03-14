### ����
�������� ������������ ���� ���������� �������� ```Clock``` ��� ���������� ������.

### �������
���������� ����������� ��������� ```EventsStatistic```, ������� ������� �������, ������������ � �������.
���������� ������ ������� ���������� ����� �� ��������� ��� � ������������, ������� ������� ������� ���� ��������� � ������.
��������� ```EventsStatistic```:
```java
public interface EventsStatistic {
    void incEvent(String name);
    ... getEventStatisticByName(String name);
    ... getAllEventStatistic();
    void printStatistic();
}
```
�������� �������:
* ```incEvent(String name)``` -- ����������� ����� ������� name;
* ```getEventStatisticByName(String name)``` -- ������ rpm (request per minute) ������� name �� ��������� ���;
* ```getAllEventStatistic()``` -- ������ rpm ���� ������������ ������� �� ��������� ���;
* ```printStatistic()``` -- ������� � ������� rpm ���� ������������ �������;
���������� ```EventsStatistic``` ���������� ������� �������, ��������� ������� ```Clock```, ������������� �� ������.
����� �� ������ ������������ sleep'� � ������ ����������� ������.
