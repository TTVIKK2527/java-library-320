# Kaitsmise harjutus — tee ise, ilma vastust ette vaatamata

1. Kompileeri rakendus ja käivita. Lisa teos, otsi seda, laenuta ning tagasta.
2. Ennusta tulemus: laenuta B1 kaks korda. Miks teine tegevus ebaõnnestub? Kus on seisundi kaitse?
3. Näita, kuidas LibraryItem-tüüpi viite kaudu jõutakse Book.loanDays() meetodini. Miks pole vaja instanceof-kontrolli?
4. Selgita, miks ID ja pealkiri on final, aga borrowed mitte. Miks puudub setBorrowed(boolean)?
5. Võrdle HashMap-i ja ArrayList-i rolli. Mis muutuks, kui sama ID-ga võiks olla mitu eksemplari?
6. Leia, milline meetod vastutab kasutajasisendi eest ja milline ärireeglite eest. Miks Library ei kasuta Scanner-it?
7. Proovi sisestada ajakirjanumbriks tekst või null. Näita veateadet ja selgita erindi teekonda.
8. Lisa ise uus alamklass, mille laenutähtaeg on 14 päeva. Kontrolli seda LibraryItem viite kaudu ja lisa menüüvalik.
9. Lisa ise filter, mis näitab ainult laenutatud teoseid. Enne muudatust kirjelda oodatav tulemus ja pärast kontrolli.
10. Selgita erinevust int/Integer/String ja ==/.equals() vahel. Leia oma koodist vastav näide.

Harjutuste 8–9 lahendusi pole ette tehtud. Õpetaja tegelik muudatus võib olla teine. Tähista endale küsimused, mida veel oma sõnadega selgitada ei oska.

## Lühikesed kontrollvastused pärast oma katset

- Ülekirjutatud meetod valitakse objekti tegeliku tüübi järgi. Viite tüüp määrab, millised meetodid on kompileerimisel kättesaadavad.
- int on primitiivne arv; Integer on objekt ja võib olla null; String esitab teksti. String-ide sisu võrdle equals()-iga.
- commit salvestab muudatuse kohalikku ajalukku; push saadab commit'id kaugserverisse.
- Abstraktne klass jagab siin nii seisundit kui käitumist. Liides sobib ühise käitumislepingu jaoks ka eri klassipuude vahel.
- Praegune rakendus ei pea arvestust lugejate, tegelike tagastamiskuupäevade ega püsisalvestuse üle; ära väida neid funktsioone olemasolevaks.
