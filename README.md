# Cleaner
### Technologie obiektowe - część projektowa

***

#### Autorzy:
* Maksymilian Katolik
* Mikołaj Nietupski
* Mateusz Wala
* Marcin Walendzik

***

### Sposób uruchomienia projektu
Do poprawnego uruchomienia serwera do embeddingu wymagane jest zinstalowanie pakietów z pliku `requirements.txt`.

Przed pierwszym uruchomieniem projektu, należy wykorzystać `./gradlew buildFrontend` aby przygotować pliki frontendowe.

Aby uruchomić serwer należy wykorzystać komendę `./gradlew run`.

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

## ETAP II
Funkcjonalności do zrealizowania na ten etap:
* Podstawowe GUI
* Zapisywanie logów z akcji
* Usprawnienie działania Archive - możliwość zmiany nazwy kolidujących plików
* Dodanie `Rename`
* Usunięcie DAO na rzecz Repository
* Sortowanie plików po rozmiarze
* Ekstrakcja keywordów (embedding zawartości plików)

***

## ETAP III
Funkcjonalności do zrealizowania na ten etap:
* Zrealizowanie warstwy frontendowej
* Połączenie backendu z frontendem
* Wykorzystanie loggera do funkcjonalności aplikacji
* Przeniesienie projektu na framework SpringBoot
* Aktualizacja `README.md`

***

### Klasy porównywania plików
![](/resources/comparator.png)

### Operacje na plikach
![](/resources/effect.png)

### Wyszukiwarki
![](/resources/finders.png)

### Warstwa persystencji
![](/resources/persistence.png)

### Frontend 
Wykorzystane technologie:
- **ReactJS**
- **TypeScript**
- **Vite**

Uruchomienie frontendu powinno być poprzedzone opisanymi wyżej komendami zdefiniowanymi w `build.gradle` i opisanymi punkcie ze sposobem uruchomienia projektu. 
W przypadku nie utworzenia automatycznie w wyniku `./gradlew buildFrontend` pakietu node_modules w pakiecie frontend należy wykonać w folderze frontend komendę 
`npm install`.

### Schemat bazy danych

Wykorzystana technologia: **Hibernate**

Aktualnie baza danych programu składa się z jednej tabeli, 
stworzonej na podstawie klasy FileInfo. Przechowuje ona dane
reprezentacji plików w programie.

![](/resources/database-scheme.png)

### Wersje modułów
Poniżej znajdują się informacje na temat wersji ważniejszych narzędzi wykorzystywanych przy tym projekcie. Dla poniższych wersji projekt został przetestowany pomyślnie:
* Java: 17.0
* NodeJS: 20.14.0
* Npm: 10.8.1
* Python: 3.10
* Hibernate: 6.6.0
* SpringBoot: 3.4.1
