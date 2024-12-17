# Uwaga! Demo! 
Projekt jest demem kwalifikacyjnym stworzonym w mniej niż 16h roboczych i nigdy **nie powinien być wykorzystywany**, w tej formie, **na produkcji**

# Uruchamianie Środowiska Developerskiego (Nieprodukcyjnego)

### **1. Ustawienie Zmiennych Środowiskowych**
Przed uruchomieniem środowiska developerskiego należy wyeksportować dwie zmienne środowiskowe:

```bash
export SPRING_DATASOURCE_DEV=mandates_dev
export SPRING_DATASOURCE_PASSWORD=this_is_strong_password_1
```
> **Uwaga:** Wartości powyższe są przykładami. Użyj własnych wartości zgodnych z konfiguracją projektu

---

### **2. Uruchomienie Bazy Danych SQL Server**
Aby uruchomić kontener z bazą danych SQL Server, użyj poniższej komendy:

```bash
docker run -d \
    --name mssql-server \
    -e "ACCEPT_EULA=Y" \
    -e "SA_PASSWORD=${SPRING_DATASOURCE_PASSWORD}" \
    -e "MSSQL_DATABASE=${SPRING_DATASOURCE_DEV}" \
    -p 1433:1433 \
    -v sql_data:/var/opt/mssql \
    mcr.microsoft.com/mssql/server:2022-latest || docker start mssql-server
```

> **Uwaga:** Komenda może wymagać uprawnień administratora (`sudo`).

---

### **3. Uruchomienie Aplikacji w Trybie Developerskim**

Aby uruchomić aplikację w trybie developerskim, użyj:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

### **4. Dostęp do Aplikacji**
Po uruchomieniu aplikacja będzie dostępna pod adresem:

[http://localhost:8080/](http://localhost:8080/)

---

### **5. Ważne Informacje o Środowisku Developerskim**
- **Brak trwałości danych:** Dane nie są przechowywane między restartami aplikacji
- **Przykładowe dane:** Generowane automatycznie przy uruchomieniu
- **Symulacja e-maili:** Aplikacja nie wysyła prawdziwych e-maili — wiadomości są drukowane w konsoli

> Jeśli napotkasz problemy, skontaktuj się ze mną

---
**Miłego odpalania! 🚀**


