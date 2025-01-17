# Cleaner
### Technologie obiektowe - część projektowa

***

#### Autorzy:
* Maksymilian Katolik
* Mateusz Wala
* Mikołaj Nietupski
* Marcin Walendzik

***

### Sposób uruchomienia projektu
Do poprawnego uruchomienia serwera do embeddingu wymagane jest zinstalowanie pakietów z pliku `requirements.txt`.

Aby uruchomić GUI należy uruchomić plik `Main.java` przy pomocy IntelliJ.

***

### ETAP I

Do zrealizowania na ten etap wyznaczono poszczególne funkcjonalności:
* Operacje na plikach (katalog *effect*)
* Podstawowe operacje na bazie danych
* Wyszukiwanie plików o zadanym atrybucie
* Algorytmy porównania plików
* Testy dotychczasowego oprogramowania
* Elementy dokumentacji (diagramy klas, schemat bazy)

***

### ETAP II
Funkcjonalności do zrealizowania na ten etap:
* Podstawowe GUI
* Zapisywanie logów z akcji
* Usprawnienie działania Archive - możliwość zmiany nazwy kolidujących plików
* Dodanie `Rename`
* Usunięcie DAO na rzecz Repository
* Sortowanie plików po rozmiarze
* Ekstrakcja keywordów (embedding zawartości plików)

***

### Klasy porównywania plików
![](/resources/comparator.png)

### Operacje na plikach
![](/resources/effect.png)

### Wyszukiwarki
![](/resources/finders.png)

### Warstwa persystencji
![](/resources/persistence.png)

### Warstwa wyświetleniowa
![](/resources/graphic-user-interface.png)

### Schemat bazy danych

Wykorzystana technologia: **Hibernate**

Aktualnie baza danych programu składa się z jednej tablicy, 
stworzonej na podstawie klasy FileInfo. Przechowuje ona dane
reprezentacji plików w programie.

![](/resources/database-scheme.png)

### Wykorzystywane biblioteki
- [AtlantaFX](https://github.com/mkpaz/atlantafx) -Projekt jest dostępny na licencji MIT. Szczegóły znajdują się w pliku [LICENSE](LICENSE)