# DziuraWdrodze.pl
###### Ratujemy koła od 2015r

>Nie wie jaki ból sprawia dziura w drodze, kto nie wjechał w nią Ferrari.

Aplikacja została stworzona w ramach projektu akademickiego. Miała powstać przy wykorzystaniu całej wiedzy zdobytej podczas wykładu. Służy do powiadamiania wybranego odbiorcy o dziurze w drodze. Projekt **DeepHole** został napisany z myślą o rozwijaniu go w kierunku aplikacji łączącej się z serwerem, który teraz reprezentowany jest przez lokalną bazę danych.

1. Zgłoszenie wysyłane mailowo składa się z:
  - Zdjęcia,
  - Opisu (opcjonalnie),
  - Lokalizacji,
  - Podpisu użytkownika.
2. Użytkownik może przeglądać dodane przez siebie dziury.
3. Aplikacja kończy swój udział w wysyłaniu zgłoszenia w momencie przekazania go do klienta poczty.

Zastosowane wzorce projektowe:
- Model-View-Controler,
- Tree Observer,
- Adapter,
- FactoryMethod,

```java
float grade = this.getRated();
System.out.println(grade);
>>5.5;
```