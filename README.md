# #331 Java raamatukogurakenduse edasiarendus

See on varasema #320 Java projekti edasiarendus. Algne Git-ajalugu on säilinud.
Uued funktsioonid on autoripõhine otsing, laenutatud teoste eemaldamise keeld koos
saadaval teoste eemaldamisega ning kataloogi statistika. Kõik muudatused salvestatakse
versioonitud UTF-8 faili ja loetakse järgmisel käivitusel tagasi.

## Käivitamine

Vaja JDK 17+ ning testide jaoks Python 3 standardteeki. IDE-d ja andmebaasi pole vaja.

```sh
./run.sh
./run.sh /tmp/minu-kataloog.tsv
./test.sh
```

Vaikimisi fail on `data/library.tsv`. Esmakäivituse kolm näidisteost lisatakse ainult
siis, kui faili veel pole. Olemasolev tühi kataloog ei täitu uuesti näidisandmetega.
Vigase faili korral rakendus lõpetab ja faili üle ei kirjuta. Salvestusvea korral
näitab veateadet ning lõpetab, sest viimane mälus tehtud muudatus ei pruugi olla kettal.

## Arhitektuur

`LibraryItem` on abstraktsioon; `Book` ja `Magazine` pärivad sellest ning rakendavad
polümorfseid `loanDays` ja `details` meetodeid. Väljad on kapseldatud. `Library`
haldab teoseid HashMap-is ja tagastab sorteeritud otsingutulemused. `Main` küsib
sisendit ning kuvab tulemuse. `LibraryRepository` eraldab salvestuse programmist;
`FileLibraryRepository` loeb ja kirjutab faili. Sama liidest saaks hiljem rakendada
andmebaasi jaoks ilma menüü loogikat ümber kirjutamata.

Failisalvestus sobib väikese ühe kasutaja käsurearakenduse jaoks: ei vaja eraldi
teenust, on kaasaskantav ja testitav ajutise kaustaga. Tekstiväljad kodeeritakse
Base64-ga, et tabulaator ja reavahetus ei rikuks kirjeid. Base64 ei ole krüpteerimine.
Kirjutamine käib ajutisse faili ja võimaluse korral atomaarse failivahetusega;
failisüsteemi toe puudumisel kasutatakse tavalist failivahetust. See ei taga
samaaegsete eri protsesside kirjutuste kooskõla. Ära ava sama faili mitmes programmis.

## Nõuete kontroll

| Nõue | Teostus |
|---|---|
| Vähemalt kolm uut funktsiooni | Autoripõhine otsing, eemaldamine, statistika |
| Otsing/filter/sortimine | `searchByAuthor`, olemasolev pealkirjaotsing ja järjestus |
| Vigane sisend | Laenutatud teose eemaldamine, puuduv ID, vigane andmefail |
| Püsivus üle taaskäivituse | `FileLibraryRepository`, konsoolitest käivitab Java protsessi kaks korda |
| Vähemalt kuus olulist testi | Algse 19 kontrolli kõrval salvestus-, laiendus- ja neli konsoolitesti |
| Vähemalt kuus sisulist commit'i | Selle haru tegelikud salvestuse, otsingu, eemaldamise, statistika, menüü ning dokumentatsiooni/testimise etapid |

## Kontrollitud 16.11.2026

`./test.sh` kompileerib `javac -Xlint:all` kontrolliga ning käivitab kõik testid.
Algne 19 kontrolli läbib; PersistenceTest ja ExtensionTest läbivad; neli
päris Java alamprotsessidega konsoolitesti läbivad. Andmete säilimine ja vigase
faili puutumatus on kontrollitud, mitte ainult README-s kirjeldatud.

Git-ajalugu: `git log --oneline feature/331-persistence`. Commit'ide autor on Codex;
see ei tõenda õppija iseseisvat programmeerimist ega asenda individuaalset kaitsmist.
Lähteprojekt #320 jääb oma haru ajaloos alles. Avaldamine ja Kriiti esitamine on
omaette sammud; kaitsmisel tuleb osata koodi selgitada ja ootamatut muudatust teha.
