# #320 — Raamatukogu laenutussüsteem

Väike Java käsurearakendus: teoste loend, pealkirjaotsing, saadaolevate teoste filter, raamatu/ajakirja lisamine, laenutamine ja tagastamine. Alguses on kaks raamatut ja üks ajakiri. Andmed asuvad ainult mälus ja lähtestuvad programmi sulgemisel. Andmebaasi ega välist teeki pole vaja.

AI koostatud lähtekood ja juhend; õpilase iseseisev arusaamine ning praktiline kaitsmine on veel tõendamata. Ära esita kaitsmist sooritatuna selle materjali põhjal.

## Käivitamine

Vaja on JDK 17 või uuemat; kontrollitud OpenJDK 21.0.13-ga.

Käivita projekti kaustas:

```sh
mkdir -p build
javac -encoding UTF-8 -d build *.java
java -cp build Main
```

Kontrollid:

```sh
javac -Xlint:all -encoding UTF-8 -d build *.java
java -cp build LibraryTest
```

## Ülesehitus ja nõuded

| Nõue | Näide |
|---|---|
| Vähemalt neli oma klassi | LibraryItem, Book, Magazine, Library, Main |
| Abstraktsioon | LibraryItem määrab ühise lepingu; otse luua ei saa |
| Pärilus | Book ja Magazine pärivad LibraryItem-ist |
| Kapseldamine | private väljad; laenutamise olekut muudavad kontrollitud meetodid |
| Polümorfism | LibraryItem viite loanDays() annab raamatu jaoks 21, ajakirja jaoks 7 |
| Kaks kollektsiooni | HashMap ID järgi leidmiseks; ArrayList otsingutulemuste kogumiseks ja sortimiseks |
| Andmetüübid | int ajakirjanumbriks/laenupäevadeks, boolean olekuks, String ID-ks ja tekstiks |
| Otsing/filter/sortimine | Pealkirja osaline otsing, saadavuse filter, pealkirja ja ID järgi sortimine |
| Vigane sisend | Tühjad väljad, korduv ID, vale number, puuduv ID ja vale laenutusolek annavad veateate |

HashMap lubab ID järgi tavaliselt keskmiselt konstantse ajaga otsingut; pealkirja osalise otsingu jaoks käiakse kõik teosed läbi. Tulemuste eraldi ArrayList-i muutmine ei kustuta kataloogi. ID on tõstutundlik; pealkirjaotsing ei ole. Laenutatakse eksemplari ID, mitte pealkirja järgi.

## Kontrollitud 12.11.2026

19 automaatset kontrolli läbisid: eri laenutähtajad, otsing, järjestus, saadavus, laenutamine/tagastus, korduvad tegevused, puuduv ID, duplikaadi tagasilükkamine ja vigased konstruktorisisendid. Konsoolitöövoo kontrolli tulemused on failis verification.txt. Kaitsmist need ei asenda.

Viis sisulist koostamisetappi salvestati tegelike kohalike commit'idena. Autor on `Codex <codex@localhost>`; see ei ole väide õpilase iseseisvast koodikirjutamisest. Ajalugu näeb käsuga `git log --oneline --reverse`.

Avaldamispakett on GitHubi jaoks ette valmistatud. Kriidi esitamine ja individuaalne kaitsmine on eraldi sammud. Kaitsmise harjutused: [DEFENSE.md](DEFENSE.md).

## Algne arendusajalugu

GitHubi veebi kaudu avaldamine loob eraldi avaldamiskommiti. Tegelik kohalik viie etapi ajalugu ja kontrollitõendid on history.bundle failis. Selle taastamiseks: `git clone history.bundle library-history`, seejärel `cd library-history` ja `git log --oneline --reverse`. Taastatud versioonis asub kood src/ ja test/ kaustades; selle README sisaldab vastavaid käske. Ajaloofaili ei esitata õpilase iseseisva töö tõendina.
