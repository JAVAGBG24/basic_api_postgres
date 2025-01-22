# Book shop API Postgres database

### Du kan använda samma postman collection som i exemplet med MongoDB men nu kommer id inte att vara string och kommer att vara 1, 2, 3 osv.

## Databas connection i application.properties
Du behöver se till att du har följande i din application.properties och inget annat
```
spring.application.name=bookshop
# name skapas automatiskt
spring.datasource.url=jdbc:postgresql://localhost:5432/bookshop
# se till att ha ditt namn på din databas och att du skapat databasen i pgAdmin innan du startar. bookshop är alltså mitt namn på min databas
spring.datasource.username=postgres
# här har jag använt min root user + password, du kan alltid skapa en ny user också i pgAdmin
spring.datasource.password=password
# dessa två raderna här under kopierar du och klistrar in, ändra inget
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

#### 1. Repository interface
Både repository klasserna är ändrade från att ha extendat ```MongoRepository<Author, String>```till ```JpaRepository<Author, Long>```
Lägg märket till att datatyper är bytt från ```String``` till ```Long```

#### 2. Datatyp på ID
Datatypen på id i modellerna är ändrat från String till Long för att matcha Postgres. Den enda förändringen som behövde göras i controllers är helt 
enkelt att byta datatyp på ```@Pathvariable String id``` till ```@PathVariable Long id```så fungerar det

#### 3. Validering i modellerna
Validering är tillagt i modellerna. det skiljer sig lite från MongoDB men är väldigt likt. Dock skiljer sig hur vi sätter referenser:
```
@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;
```
Det räcker inte att endast använda dessa annoteringar, då kommer vi får ett 500 fel när vi tex försöker hämta alla bäcker via Postman.
Vi måste även annotera hela model klassen med:
```
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
```
Det har att göra med hur Hibernate och Jackson samverkar vid serialisering av lazy-loaded relationer.
När du använder ```FetchType.LAZY``` skapar Hibernate en proxy-klass för Author-objektet. 
Istället för att ladda hela Author-objektet direkt när du hämtar en Book, skapar Hibernate en proxy som bara innehåller ID:t och laddar resten av datan först när den behövs.
Låt mig försöka förklara:
1. När du hämtar en Book, är author-fältet egentligen inte ett rent Author-objekt, utan en Hibernate proxy-klass (typ nåt sånt häe Author$HibernateProxy$...)
2. När Jackson försöker serialisera Book-objektet till JSON, stöter den på author-fältet
3. Utan ```@JsonIgnoreProperties``` på Author-klassen, försöker Jackson serialisera alla fält i proxy-objektet, inklusive Hibernates interna fält som "hibernateLazyInitializer" och "handler"
4. Det här orsakar problem eftersom dessa interna proxy-fält inte kan serialiseras på ett vettigt sätt

När du lägger till ```@JsonIgnoreProperties``` på Author-klassen talar du om för Jackson att den ska ignorera dessa interna proxy-fält när den serialiserar 
Author-objekt, oavsett om de är proxy-objekt eller inte.
Det är därför det är viktigt att ha ```@JsonIgnoreProperties``` på alla entiteter som kan bli lazy-loaded, inte bara på den klass som har relationen.
