# EventManager
Napisati Android aplikaciju koja omogućava organizaciju i planiranje različitih
aktivnosti. Aktivnosti mogu biti vezane za posao, slobodno vrijeme, putovanja i sl. Svaka
aktivnost minimalno treba da sadrži naziv, vrijeme, opis i lokaciju. [10 bodova]
Slobodne aktivnosti mogu da sadrže i nekoliko slika, koje je moguće upload-ovati sa
uređaja, napraviti pomoću kamere uređaja, ili preuzeti sa udaljene lokacije. [10 bodova]
Za aktivnosti koje su vezane za putovanja potrebno je omogućiti prikaz mape, kako bi
se precizno vidjela lokacija (grad) aktivnosti. [10 bodova]
Potrebno je omogućiti kalendarski prikaz aktivnosti, radi lakšeg praćenja. Osim toga,
omogućiti prikaz aktivnosti u vidu liste, sa hronološki poredanim stavkama, pri čemu se lista
može pretraživati po nazivu aktivnosti. [15 bodova]
Prilikom pokretanja aplikacije, potrebno je prikazati notifikaciju/podsjetnik za
aktivnosti koje će se desiti u bližoj budućnosti. Period se definiše kroz podešavanja (na
primjer, notifikacija prikazuje aktivnosti koje su planirane za naredni dan). [10 bodova]
U okviru posebne stranice sa podešavanjima, omogućiti izbor jezika i podešavanje
notifikacija (uključene/isključene, vrijeme obavještavanja: sat ranije, dan ranije, ili sedam
dana ranije). Potrebno je podržati lokalizaciju/internacionalizaciju, pri čemu je potrebno
omogućiti da aplikacija radi minimalno na srpskom i engleskom jeziku. [5 bodova]
Sve aktivnosti koje bi mogle uticati na blokiranje glavne niti aplikacije, potrebno je
realizovati asinhrono. Za ovu namjenu potrebno je koristiti klasu AsyncTask, ili neku od
biblioteka koje pružaju ovu funkcionalnost.
Potrebno je generisati grafičke elemente tako da se pokrije minimum pet različitih
aktuelnih gustina ekrana kao i različitih dimenzija ekrana. Za generisanje grafičkih resursa,
dozvoljeno je korištenje nekog od alata kao što je AndroidAssetStudio. Prilikom izgradnje
grafičkih dijelova aplikacije (Layout Manager-i, View-ovi), potrebno je voditi računa o
performansama aplikacije. Obezbijediti da aplikacija optimalno radi na oba tipa orjentacije
ekrana (portrait i landscape).
Aplikaciju je potrebno testirati na različitim tipovima emulatora koji predstavljaju
uređaje sa različitom gustinom ekrana i različitom veličinom ekrana (telefon i tablet).
Sve detalje zadatka koji nisu precizno specifikovani realizovati na proizvoljan način.
Detalji dizajna će se ocjenjivati sa aspekta poštovanja osnovnih smjernica kada je u pitanju
izgradnja korisničkog interfejsa, pri čemu je GUI potrebno implementirati u skladu sa
posljednjim trendovima i preporukama (Material Design i sl.). Potrebno je voditi računa da
GUI bude minimalistički i da se izabere odgovarajući skup srodnih boja kako bi se postigao
što bolji korisnički doživljaj (UX). Sve stilske elemente potrebno je definisati kroz odvojene
stilove i teme kako bi se postigla što veća fleksibilnost i modularnost koda
