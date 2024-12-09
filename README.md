# Cleaner
### Technologie obiektowe - część projektowa

***

#### Autorzy:
* Maksymilian Katolik
* Mateusz Wala
* Mikołaj Nietupski
* Marcin Walendzik

***

## ETAP I

Do zrealizowania na ten etap wyznaczono poszczególne funkcjonalności:
* Operacje na plikach (katalog *effect*)
* Podstawowe operacje na bazie danych
* Wyszukiwanie plików o zadanym atrybucie
* Algorytmy porównania plików
* Testy dotychczasowego oprogramowania
* Elementy dokumentacji (diagramy klas, schemat bazy)

***

### Klasy porównywania plików
![](/resources/comparator.png)

### Operacje na plikach
![](/resources/effect.png)

### Wyszukiwarki
![](/resources/finders.png)

### Warstwa persystencji
![](/resources/persistence.png)

### Schemat bazy danych

Wykorzystana technologia: **Hibernate**

Aktualnie baza danych programu składa się z jednej tablicy, 
stworzonej na podstawie klasy FileInfo. Przechowuje ona dane
reprezentacji plików w programie.

![](/resources/database-scheme.png)